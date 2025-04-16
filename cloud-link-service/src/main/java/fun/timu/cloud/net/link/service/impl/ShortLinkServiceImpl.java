package fun.timu.cloud.net.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.common.enums.DomainTypeEnum;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.enums.ShortLinkStateEnum;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.IDUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.link.component.ShortLinkComponent;
import fun.timu.cloud.net.link.config.RabbitMQConfig;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkPageRequest;
import fun.timu.cloud.net.link.manager.DomainManager;
import fun.timu.cloud.net.link.manager.GroupCodeMappingManager;
import fun.timu.cloud.net.link.manager.LinkGroupManager;
import fun.timu.cloud.net.link.manager.ShortLinkManager;
import fun.timu.cloud.net.link.mapper.ShortLinkMapper;
import fun.timu.cloud.net.link.model.DO.Domain;
import fun.timu.cloud.net.link.model.DO.GroupCodeMapping;
import fun.timu.cloud.net.link.model.DO.LinkGroup;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;
import fun.timu.cloud.net.link.service.DomainService;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengke
 * @description 针对表【short_link】的数据库操作Service实现
 * @createDate 2025-04-15 11:23:41
 */

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements ShortLinkService {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkService.class);

    private final ShortLinkManager shortLinkManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private final DomainManager domainManager;

    private final LinkGroupManager linkGroupManager;

    private final ShortLinkComponent shortLinkComponent;

    private final GroupCodeMappingManager groupCodeMappingManager;

    public ShortLinkServiceImpl(ShortLinkManager shortLinkManager, DomainManager domainManager, LinkGroupManager linkGroupManager, ShortLinkComponent shortLinkComponent, GroupCodeMappingManager groupCodeMappingManager) {
        this.shortLinkManager = shortLinkManager;
        this.domainManager = domainManager;
        this.linkGroupManager = linkGroupManager;
        this.shortLinkComponent = shortLinkComponent;
        this.groupCodeMappingManager = groupCodeMappingManager;
    }


    /**
     * 解析短链接代码
     * <p>
     * 本方法通过短链接代码查询对应的短链接信息，如果找到则返回该信息，否则返回null
     * 主要用于处理短链接的解析请求，将短链接代码转换为可访问的短链接信息
     *
     * @param shortLinkCode 短链接代码，用于查询对应的短链接信息
     * @return ShortLinkVO 如果找到对应的短链接信息则返回，否则返回null
     */
    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        // 根据短链接代码查询数据库中的短链接对象
        ShortLink shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        // 如果查询结果为空，则直接返回null
        if (shortLinkDO == null) {
            return null;
        }
        // 创建一个短链接视图对象，用于封装查询到的短链接信息
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        // 将短链接对象的属性复制到视图对象中
        BeanUtils.copyProperties(shortLinkDO, shortLinkVO);
        // 返回填充好的短链接视图对象
        return shortLinkVO;
    }

    /**
     * 创建短链接
     * <p>
     * 此方法接收一个ShortLinkAddRequest对象作为请求参数，用于生成短链接信息它通过RabbitMQ异步处理短链接的创建过程
     *
     * @param request 包含短链接相关信息的请求对象
     * @return 返回一个表示操作结果的JsonData对象
     */
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 确保原始URL具有正确的格式，如果缺少http或https前缀，则添加之
        String newOriginalUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
        request.setOriginalUrl(newOriginalUrl);

        // 构建事件消息对象，用于发送到RabbitMQ
        EventMessage eventMessage = EventMessage.builder().accountNo(accountNo).content(JsonUtil.obj2Json(request)).messageId(IDUtil.geneSnowFlakeID().toString()).eventMessageType(EventMessageType.SHORT_LINK_ADD.name()).build();

        // 将事件消息发送到指定的RabbitMQ交换机和路由键
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkAddRoutingKey(), eventMessage);

        // 返回成功响应，表示短链接创建请求已成功发送
        return JsonData.buildSuccess();
    }

    /**
     * 处理短链新增逻辑
     * <p>
     * 本方法负责处理短链的新增请求，包括验证域名和组名的合法性，生成长链的摘要和短链码，
     * 检查短链码的唯一性，并根据不同的消息类型构建相应的对象进行保存
     *
     * @param eventMessage 包含短链新增请求信息的事件消息对象
     * @return 返回短链新增是否成功的布尔值，成功为true，失败为false
     */
    @Override
    public boolean handlerAddShortLink(EventMessage eventMessage) {

        // 获取事件消息中的账户编号和消息类型
        Long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();

        // 将事件消息内容转换为ShortLinkAddRequest对象
        ShortLinkAddRequest addRequest = JsonUtil.json2Obj(eventMessage.getContent(), ShortLinkAddRequest.class);

        // 短链域名校验
        Domain domainDO = checkDomain(addRequest.getDomainType(), addRequest.getDomainId(), accountNo);

        // 校验组是否合法
        LinkGroup linkGroupDO = checkLinkGroup(addRequest.getGroupId(), accountNo);

        // 长链摘要
        String originalUrlDigest = CommonUtil.MD5(addRequest.getOriginalUrl());

        //短链码重复标记
        boolean duplicateCodeFlag = false;

        // 生成短链码
        String shortLinkCode = shortLinkComponent.createShortLinkCode(addRequest.getOriginalUrl());

        // TODO 加锁
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" + " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" + " else return 0; end;";

        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(shortLinkCode), accountNo, 100);

        //加锁成功
        if (result > 0) {

            //C端处理
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(messageType)) {


                //先判断是否短链码被占用
                ShortLink shortLinCodeDOInDB = shortLinkManager.findByShortLinCode(shortLinkCode);

                if (shortLinCodeDOInDB == null) {
                    ShortLink shortLinkDO = ShortLink.builder().accountNo(accountNo).code(shortLinkCode).title(addRequest.getTitle()).originalUrl(addRequest.getOriginalUrl()).domain(domainDO.getValue()).groupId(linkGroupDO.getId()).expired(addRequest.getExpired()).sign(originalUrlDigest).state(ShortLinkStateEnum.ACTIVE.name()).del(0).build();
                    shortLinkManager.addShortLink(shortLinkDO);
                    return true;
                } else {
                    logger.error("C端短链码重复:{}", eventMessage);
                    duplicateCodeFlag = true;
                }


            } else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(messageType)) {
                //B端处理
                GroupCodeMapping groupCodeMappingDOInDB = groupCodeMappingManager.findByCodeAndGroupId(shortLinkCode, linkGroupDO.getId(), accountNo);

                if (groupCodeMappingDOInDB == null) {

                    GroupCodeMapping groupCodeMappingDO = GroupCodeMapping.builder().accountNo(accountNo).code(shortLinkCode).title(addRequest.getTitle()).originalUrl(addRequest.getOriginalUrl()).domain(domainDO.getValue()).groupId(linkGroupDO.getId()).expired(addRequest.getExpired()).sign(originalUrlDigest).state(ShortLinkStateEnum.ACTIVE.name()).del(0).build();

                    groupCodeMappingManager.add(groupCodeMappingDO);
                    return true;

                } else {
                    logger.error("B端短链码重复:{}", eventMessage);
                    duplicateCodeFlag = true;
                }

            }

        } else {

            //加锁失败，自旋100毫秒，再调用； 失败的可能是短链码已经被占用，需要重新生成
            logger.error("加锁失败:{}", eventMessage);

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }

            duplicateCodeFlag = true;

        }

        if (duplicateCodeFlag) {
            String newOriginalUrl = CommonUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
            addRequest.setOriginalUrl(newOriginalUrl);
            eventMessage.setContent(JsonUtil.obj2Json(addRequest));
            logger.warn("短链码报错失败，重新生成:{}", eventMessage);
            handlerAddShortLink(eventMessage);
        }
        return false;

    }

    /**
     * 从B端查找，group_code_mapping表
     * 根据 groupId 分页查询短链接信息
     * 此方法主要用于处理来自B端的请求，根据 groupId 和分页参数获取相应的短链接信息
     * 它利用了登录拦截器中的线程局部变量来获取当前用户的账户编号，并将其作为查询条件之一
     *
     * @param request 包含分页信息和 groupId 的请求对象，用于指定查询条件
     * @return 返回一个包含短链接信息的Map对象，其中具体结构取决于查询结果
     */
    @Override
    public Map<String, Object> pageByGroupId(ShortLinkPageRequest request) {
        // 获取当前登录用户的账户编号，用于后续查询条件
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用groupCodeMappingManager的分页查询方法，传入请求的页码、每页大小、用户账户编号和groupId，获取查询结果
        Map<String, Object> result = groupCodeMappingManager.pageShortLinkByGroupId(request.getPage(), request.getSize(), accountNo, request.getGroupId());

        // 返回查询结果
        return result;
    }


    /**
     * 校验域名
     *
     * @param domainType 域名类型，如自定义域名或官方域名
     * @param domainId   域名ID，用于查询特定的域名信息
     * @param accountNo  账户编号，用于关联域名到特定的账户，仅在自定义域名时需要
     * @return 返回查询到的域名对象
     * <p>
     * 本方法根据域名类型和ID查询对应的域名信息如果域名类型是自定义域名，
     * 则还需提供账户编号以确定域名归属确保域名对象不为空，否则抛出异常
     */
    private Domain checkDomain(String domainType, Long domainId, Long accountNo) {

        Domain domainDO;

        // 根据域名类型查询域名信息
        if (DomainTypeEnum.CUSTOM.name().equalsIgnoreCase(domainType)) {
            // 自定义域名，根据域名ID和账户编号查询域名信息
            domainDO = domainManager.findById(domainId, accountNo);

        } else {
            // 官方域名，仅根据域名ID查询域名信息
            domainDO = domainManager.findByDomainTypeAndID(domainId, DomainTypeEnum.OFFICIAL);
        }
        // 确保查询到的域名对象不为空
        Assert.notNull(domainDO, "短链域名不合法");
        return domainDO;
    }

    /**
     * 校验组名
     * <p>
     * 该方法用于校验给定组ID和账户号对应的组是否存在且有效
     * 如果组不存在或不合法，将抛出异常
     *
     * @param groupId   组ID，用于标识特定的组
     * @param accountNo 账户号，用于关联特定的用户账户
     * @return 如果组存在且合法，返回该组的对象
     */
    private LinkGroup checkLinkGroup(Long groupId, Long accountNo) {

        // 根据组ID和账户号获取组详情
        LinkGroup linkGroupDO = linkGroupManager.detail(groupId, accountNo);
        // 校验组是否存在，如果不存在则抛出异常
        Assert.notNull(linkGroupDO, "组名不合法");
        // 返回获取的组对象
        return linkGroupDO;
    }
}




