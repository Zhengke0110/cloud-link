package fun.timu.cloud.net.account.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.fastjson.TypeReference;

import fun.timu.cloud.net.account.config.RabbitMQConfig;
import fun.timu.cloud.net.account.controller.request.TrafficPageRequest;
import fun.timu.cloud.net.account.controller.request.UseTrafficRequest;
import fun.timu.cloud.net.account.feign.ProductFeignService;
import fun.timu.cloud.net.account.feign.ShortLinkFeignService;
import fun.timu.cloud.net.account.manager.AccountManager;
import fun.timu.cloud.net.account.manager.TrafficManager;
import fun.timu.cloud.net.account.manager.TrafficTaskManager;
import fun.timu.cloud.net.account.mapper.TrafficMapper;
import fun.timu.cloud.net.account.model.DO.Traffic;
import fun.timu.cloud.net.account.model.DO.TrafficTask;
import fun.timu.cloud.net.account.model.VO.ProductVO;
import fun.timu.cloud.net.account.model.VO.TrafficVO;
import fun.timu.cloud.net.account.model.VO.UseTrafficVO;
import fun.timu.cloud.net.account.service.TrafficService;
import fun.timu.cloud.net.common.constant.RedisKey;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.enums.TaskStateEnum;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.model.LoginUser;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhengke
 * @description 针对表【traffic】的数据库操作Service实现
 * @createDate 2025-04-22 15:18:25
 */
@Service
public class TrafficServiceImpl extends ServiceImpl<TrafficMapper, Traffic> implements TrafficService {
    private static Logger logger = LoggerFactory.getLogger(TrafficService.class);

    private final TrafficManager trafficManager;
    private final ProductFeignService productFeignService;
    private final TrafficTaskManager trafficTaskManager;
    private final RabbitTemplate rabbitTemplate;
    private final ShortLinkFeignService shortLinkFeignService;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public TrafficServiceImpl(TrafficManager trafficManager, ProductFeignService productFeignService, TrafficTaskManager trafficTaskManager, RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig, ShortLinkFeignService shortLinkFeignService) {
        this.trafficManager = trafficManager;
        this.productFeignService = productFeignService;
        this.trafficTaskManager = trafficTaskManager;
        this.rabbitTemplate = rabbitTemplate;
        this.shortLinkFeignService = shortLinkFeignService;
    }

    /**
     * 重写处理流量信息的方法
     * 该方法用于处理产品订单支付事件，当订单支付成功后，为用户添加相应的流量包
     *
     * @param eventMessage 事件消息，包含订单支付的相关信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void handleTrafficMessage(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();

        // 获取消息类型
        String messageType = eventMessage.getEventMessageType();
        // 判断消息类型是否为产品订单支付
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(messageType)) {
            // 订单已经支付，新增流量

            String content = eventMessage.getContent();
            Map<String, Object> orderInfoMap = JsonUtil.json2Obj(content, Map.class);

            // 还原订单商品信息
            String outTradeNo = (String) orderInfoMap.get("outTradeNo");
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");
            String productStr = (String) orderInfoMap.get("product");
            ProductVO productVO = JsonUtil.json2Obj(productStr, ProductVO.class);
            logger.info("商品信息:{}", productVO);

            // 流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(productVO.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // 构建流量包对象
            Traffic trafficDO = Traffic.builder().accountNo(accountNo).dayLimit(productVO.getDayTimes() * buyNum).dayUsed(0).totalLimit(productVO.getTotalTimes()).pluginType(productVO.getPluginType()).level(productVO.getLevel()).productId(productVO.getId()).outTradeNo(outTradeNo).expiredDate(date).build();

            int rows = trafficManager.add(trafficDO);
            logger.info("消费消息新增流量包:rows={},trafficDO={}", rows, trafficDO);

            //新增流量包，应该删除这个key
            String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
            redisTemplate.delete(totalTrafficTimesKey);
        } else if (EventMessageType.TRAFFIC_FREE_INIT.name().equalsIgnoreCase(messageType)) {
            // 发放免费流量包
            Long productId = Long.valueOf(eventMessage.getBizId());

            JsonData jsonData = productFeignService.detail(productId);

            ProductVO productVO = jsonData.getData(new TypeReference<ProductVO>() {
            });
            // 构建流量包对象
            Traffic trafficDO = Traffic.builder().accountNo(accountNo).dayLimit(productVO.getDayTimes()).dayUsed(0).totalLimit(productVO.getTotalTimes()).pluginType(productVO.getPluginType()).level(productVO.getLevel()).productId(productVO.getId()).outTradeNo("free_init").expiredDate(new Date()).build();

            trafficManager.add(trafficDO);
        } else if (EventMessageType.TRAFFIC_USED.name().equalsIgnoreCase(messageType)) {
            //流量包使用，检查是否成功使用
            //检查task是否存在
            //检查短链是否成功
            //如果不成功，则恢复流量包
            //删除task (也可以更新task状态，定时删除就行)

            Long trafficTaskId = Long.valueOf(eventMessage.getBizId());
            TrafficTask trafficTaskDO = trafficTaskManager.findByIdAndAccountNo(trafficTaskId, accountNo);

            //非空且锁定
            if (trafficTaskDO != null && trafficTaskDO.getLockState().equalsIgnoreCase(TaskStateEnum.LOCK.name())) {

                JsonData jsonData = shortLinkFeignService.check(trafficTaskDO.getBizId());

                if (jsonData.getCode() != 0) {

                    logger.error("创建短链失败，流量包回滚");

                    String useDateStr = TimeUtil.format(trafficTaskDO.getGmtCreate(), "yyyy-MM-dd");

                    trafficManager.releaseUsedTimes(accountNo, trafficTaskDO.getTrafficId(), 1, useDateStr);

                    //恢复流量包，应该删除这个key（也可以让这个key递增）
                    String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
                    redisTemplate.delete(totalTrafficTimesKey);

                }

                //多种方式处理task，不立刻删除，可以更新状态，然后定时删除也行
                trafficTaskManager.deleteByIdAndAccountNo(trafficTaskId, accountNo);

            }
        }
    }


    /**
     * 根据请求分页查询可用的流量包信息
     *
     * @param request 分页查询请求对象，包含页码和每页大小等信息
     * @return 返回一个包含分页信息和流量包数据的映射对象
     */
    @Override
    public Map<String, Object> pageAvailable(TrafficPageRequest request) {
        //获取请求中的每页大小和页码
        int size = request.getSize();
        int page = request.getPage();
        //从线程局部变量中获取当前登录用户信息
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        //调用trafficManager的分页查询方法，获取分页后的流量包数据
        IPage<Traffic> trafficDOIPage = trafficManager.pageAvailable(page, size, loginUser.getAccountNo());

        //获取流量包列表
        List<Traffic> records = trafficDOIPage.getRecords();

        //将查询到的流量包数据转换为视图对象列表
        List<TrafficVO> trafficVOList = records.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());

        //创建一个映射对象，用于存储分页信息和流量包数据
        Map<String, Object> pageMap = new HashMap<>(3);
        //将总记录数、总页数和当前页数据放入映射对象中
        pageMap.put("total_record", trafficDOIPage.getTotal());
        pageMap.put("total_page", trafficDOIPage.getPages());
        pageMap.put("current_data", trafficVOList);

        //返回包含分页信息和流量包数据的映射对象
        return pageMap;
    }

    /**
     * 根据交通违规记录ID获取详细信息
     * 此方法首先从线程局部变量中获取当前登录用户信息，以确定操作的上下文
     * 然后调用trafficManager的findByIdAndAccountNo方法，根据交通违规记录ID和用户账号查询交通违规记录详情
     * 最后，将查询到的交通违规记录对象处理并转换为TrafficVO对象返回
     *
     * @param trafficId 交通违规记录的唯一标识符，用于查询特定的交通违规记录
     * @return 返回一个包含交通违规记录详细信息的TrafficVO对象
     */
    @Override
    public TrafficVO detail(long trafficId) {
        // 获取当前登录用户信息，用于后续操作的权限验证和上下文确定
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        // 根据交通违规记录ID和用户账号查询交通违规记录详情
        Traffic trafficDO = trafficManager.findByIdAndAccountNo(trafficId, loginUser.getAccountNo());

        // 将查询到的交通违规记录对象处理并转换为TrafficVO对象返回
        return beanProcess(trafficDO);
    }

    /**
     * 删除过期的流量记录
     * <p>
     * 此方法旨在清理系统中不再需要的过期流量记录，以维护数据的时效性和减少存储空间的占用
     * 它调用了trafficManager的deleteExpireTraffic方法来实现实际的删除操作
     *
     * @return 删除操作的结果，如果删除成功则返回true，否则返回false
     */
    @Override
    public boolean deleteExpireTraffic() {
        return trafficManager.deleteExpireTraffic();
    }

    /**
     * 减少流量方法
     * 该方法主要用于处理用户流量的扣减逻辑
     * 使用事务确保数据一致性，当发生异常时回滚事务
     *
     * @param trafficRequest 用户流量请求对象，包含账户编号等信息
     * @return 返回一个JsonData对象，表示操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public JsonData reduce(UseTrafficRequest trafficRequest) {

        Long accountNo = trafficRequest.getAccountNo();

        //处理流量包，筛选出未更新流量包，当前使用的流量包
        UseTrafficVO useTrafficVO = processTrafficList(accountNo);

        logger.info("今天可用总次数:{},当前使用流量包:{}", useTrafficVO.getDayTotalLeftTimes(), useTrafficVO.getCurrentTrafficDO());
        if (useTrafficVO.getCurrentTrafficDO() == null) {
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }

        logger.info("待更新流量包列表:{}", useTrafficVO.getUnUpdatedTrafficIds());

        //如果有未更新的流量包，进行批量更新
        if (useTrafficVO.getUnUpdatedTrafficIds().size() > 0) {
            //更新今日流量包
            trafficManager.batchUpdateUsedTimes(accountNo, useTrafficVO.getUnUpdatedTrafficIds());
        }

        //先更新，再扣减当前使用的流量包
        int rows = trafficManager.addDayUsedTimes(accountNo, useTrafficVO.getCurrentTrafficDO().getId(), 1);

        TrafficTask trafficTaskDO = TrafficTask.builder().accountNo(accountNo).bizId(trafficRequest.getBizId()).useTimes(1).trafficId(useTrafficVO.getCurrentTrafficDO().getId()).lockState(TaskStateEnum.LOCK.name()).build();

        trafficTaskManager.add(trafficTaskDO);
        if (rows != 1) {
            throw new BizException(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }

        //往redis设置下总流量包次数，短链服务那边递减即可； 如果有新增流量包，则删除这个key
        long leftSeconds = TimeUtil.getRemainSecondsOneDay(new Date());

        String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);

        redisTemplate.opsForValue().set(totalTrafficTimesKey, useTrafficVO.getDayTotalLeftTimes() - 1, leftSeconds, TimeUnit.SECONDS);

        EventMessage trafficUseEventMessage = EventMessage.builder().accountNo(accountNo).bizId(trafficTaskDO.getId() + "").eventMessageType(EventMessageType.TRAFFIC_USED.name()).build();

        //发送延迟消息，用于异常回滚
        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(), rabbitMQConfig.getTrafficReleaseDelayRoutingKey(), trafficUseEventMessage);
        return JsonData.buildSuccess();
    }

    /**
     * 处理流量包列表
     * 根据账户编号获取可用的流量包信息，计算当天剩余可用总次数，并选择一个可用的流量包
     * 如果没有可用的流量包，则抛出业务异常
     *
     * @param accountNo 账户编号
     * @return 返回包含当天剩余可用总次数、当前使用的流量包和未更新的流量包ID列表的UseTrafficVO对象
     * @throws BizException 如果没有可用的流量包，则抛出业务异常
     */
    private UseTrafficVO processTrafficList(Long accountNo) {

        //全部流量包
        List<Traffic> list = trafficManager.selectAvailableTraffics(accountNo);
        if (list == null || list.size() == 0) {
            throw new BizException(BizCodeEnum.TRAFFIC_EXCEPTION);
        }

        //天剩余可用总次数
        Integer dayTotalLeftTimes = 0;

        //当前使用
        Traffic currentTrafficDO = null;

        //没过期，但是今天没更新的流量包id列表
        List<Long> unUpdatedTrafficIds = new ArrayList<>();

        //今天日期
        String todayStr = TimeUtil.format(new Date(), "yyyy-MM-dd");

        for (Traffic trafficDO : list) {
            String trafficUpdateDate = TimeUtil.format(trafficDO.getGmtModified(), "yyyy-MM-dd");
            if (todayStr.equalsIgnoreCase(trafficUpdateDate)) {
                //已经更新  天剩余可用总次数 = 总次数 - 已用
                int dayLeftTimes = trafficDO.getDayLimit() - trafficDO.getDayUsed();
                dayTotalLeftTimes = dayTotalLeftTimes + dayLeftTimes;

                //选取当次使用流量包
                if (dayLeftTimes > 0 && currentTrafficDO == null) {
                    currentTrafficDO = trafficDO;
                }

            } else {
                //未更新
                dayTotalLeftTimes = dayTotalLeftTimes + trafficDO.getDayLimit();
                //记录未更新的流量包
                unUpdatedTrafficIds.add(trafficDO.getId());

                //选取当次使用流量包
                if (currentTrafficDO == null) {
                    currentTrafficDO = trafficDO;
                }
            }
        }

        UseTrafficVO useTrafficVO = new UseTrafficVO(dayTotalLeftTimes, currentTrafficDO, unUpdatedTrafficIds);
        return useTrafficVO;
    }

    /**
     * 将Traffic实体类转换为TrafficVO视图类
     * <p>
     * 此方法的作用是将从数据库中获取的Traffic对象转换为用于展示层的TrafficVO对象
     * 它利用Apache Commons BeanUtils库中的copyProperties方法进行属性复制，以简化代码并提高可维护性
     *
     * @param trafficDO Traffic实体类对象，包含从数据库中获取的流量包数据
     * @return 返回一个新创建的TrafficVO对象，其属性与传入的Traffic实体类对象相同
     */
    private TrafficVO beanProcess(Traffic trafficDO) {
        TrafficVO trafficVO = new TrafficVO();
        BeanUtils.copyProperties(trafficDO, trafficVO);

        //惰性更新，前端显示的问题，根据更新时间进行判断是否需要显示最新的流量包
        //通过比较流量包数据的更新时间和当前时间，决定是否重置日使用流量

        String todayStr = TimeUtil.format(new Date(), "yyyy-MM-dd");
        String trafficUpdateStr = TimeUtil.format(trafficDO.getGmtModified(), "yyyy-MM-dd");

        if (!todayStr.equalsIgnoreCase(trafficUpdateStr)) {
            trafficVO.setDayUsed(0);
        }

        return trafficVO;
    }

}




