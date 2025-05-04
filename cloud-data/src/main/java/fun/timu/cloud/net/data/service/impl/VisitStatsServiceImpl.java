package fun.timu.cloud.net.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.common.enums.DateTimeFieldEnum;
import fun.timu.cloud.net.common.enums.QueryDeviceEnum;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.data.controller.request.*;
import fun.timu.cloud.net.data.mapper.VisitStatsMapper;
import fun.timu.cloud.net.data.model.DO.VisitStats;
import fun.timu.cloud.net.data.model.VO.VisitStatsVO;
import fun.timu.cloud.net.data.service.VisitStatsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhengke
 * @description 针对表【visit_stats】的数据库操作Service实现
 * @createDate 2025-05-02 22:15:26
 */
@Service
public class VisitStatsServiceImpl extends ServiceImpl<VisitStatsMapper, VisitStats> implements VisitStatsService {
    private final VisitStatsMapper visitStatsMapper;

    public VisitStatsServiceImpl(VisitStatsMapper visitStatsMapper) {
        this.visitStatsMapper = visitStatsMapper;
    }

    /**
     * 根据请求分页查询访问记录
     *
     * @param request 分页查询请求对象，包含查询所需的信息如页码、每页大小和查询代码
     * @return 返回一个包含分页查询结果的Map对象，包括总记录数、当前页码、总页数和数据列表
     */
    @Override
    public Map<String, Object> pageVisitRecord(VisitRecordPageRequest request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 初始化用于存储查询结果的Map对象
        Map<String, Object> data = new HashMap<>(16);

        // 从请求中提取查询参数
        String code = request.getCode();
        int page = request.getPage();
        int size = request.getSize();

        // 计算总记录数
        int count = visitStatsMapper.countTotal(code, accountNo);
        // 计算查询的起始位置
        int from = (page - 1) * size;

        // 执行分页查询，获取访问记录列表
        List<VisitStats> list = visitStatsMapper.pageVisitRecord(code, accountNo, from, size);

        // 将查询结果转换为VO对象，便于传输和展示
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());

        // 将总记录数和当前页码存入结果Map
        data.put("total", count);
        data.put("current_page", page);

        // 计算总页数
        int totalPage = 0;
        if (count % size == 0) {
            totalPage = count / size;
        } else {
            totalPage = count / size + 1;
        }
        // 将总页数存入结果Map
        data.put("total_page", totalPage);

        // 将转换后的访问记录列表存入结果Map
        data.put("data", visitStatsVOS);
        // 返回查询结果
        return data;
    }

    /**
     * 根据地区代码和时间范围查询每日访问统计信息
     * <p>
     * 此方法实现特定于地区的访问统计信息查询它根据提供的地区代码和时间范围，
     * 以及当前登录用户的账户编号，查询该用户在指定时间内按天统计的访问信息
     *
     * @param request 包含地区代码、开始时间和结束时间的查询请求对象
     * @return 返回一个VisitStatsVO对象列表，每个对象包含一天的访问统计信息
     */
    @Override
    public List<VisitStatsVO> queryRegionWithDay(RegionQueryRequest request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 查询指定地区、账户和时间范围内的每日访问统计信息
        List<VisitStats> list = visitStatsMapper.queryRegionVisitStatsWithDay(request.getCode(), accountNo, request.getStartTime(), request.getEndTime());

        // 将查询结果转换为VisitStatsVO对象列表
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());

        // 返回转换后的访问统计信息列表
        return visitStatsVOS;
    }

    /**
     * 查询访问趋势
     * 根据给定的时间类型和时间范围，查询特定代码的访问趋势
     *
     * @param request 访问趋势查询请求对象，包含代码、时间类型和时间范围
     * @return 返回一个VisitStatsVO对象列表，表示访问趋势数据
     */
    @Override
    public List<VisitStatsVO> queryVisitTrend(VisitTrendQueryRequest request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取查询请求中的代码
        String code = request.getCode();

        // 获取查询请求中的时间类型
        String type = request.getType();

        // 获取查询请求中的开始时间和结束时间
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        List<VisitStats> list = null;

        // 根据时间类型查询访问趋势数据
        if (DateTimeFieldEnum.DAY.name().equalsIgnoreCase(type)) {
            // 如果时间类型是天，则查询多天的访问趋势数据
            list = visitStatsMapper.queryVisitTrendWithMultiDay(code, accountNo, startTime, endTime);
        } else if (DateTimeFieldEnum.HOUR.name().equalsIgnoreCase(type)) {
            // 如果时间类型是小时，则查询指定开始时间的访问趋势数据
            list = visitStatsMapper.queryVisitTrendWithHour(code, accountNo, startTime);
        } else if (DateTimeFieldEnum.MINUTE.name().equalsIgnoreCase(type)) {
            // 如果时间类型是分钟，则查询指定时间范围内的访问趋势数据
            list = visitStatsMapper.queryVisitTrendWithMinute(code, accountNo, startTime, endTime);
        }

        // 将查询结果转换为VisitStatsVO对象列表并返回
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVOS;
    }

    /**
     * 根据频繁来源请求查询访问统计信息
     * <p>
     * 此方法用于响应查询频繁访问来源的需求，通过特定条件筛选出指定时间段内最频繁的访问来源
     * 它首先从线程本地变量中获取当前用户的账户编号，然后根据请求参数中的代码、开始时间和结束时间来查询
     * 最多10条频繁访问记录，并将这些记录转换为访问统计信息视图对象返回
     *
     * @param request 包含查询条件的频繁来源请求对象，包括代码、开始时间和结束时间
     * @return 返回一个VisitStatsVO对象列表，每个对象包含一条频繁访问来源的统计信息
     */
    @Override
    public List<VisitStatsVO> queryFrequentSource(FrequentSourceRequset request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取请求参数中的代码
        String code = request.getCode();

        // 获取请求参数中的开始时间和结束时间
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();

        // 查询频繁访问来源，限定最多返回10条记录
        List<VisitStats> list = visitStatsMapper.queryFrequentSource(code, accountNo, startTime, endTime, 10);

        // 将查询结果转换为视图对象列表
        List<VisitStatsVO> visitStatsVOS = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVOS;
    }

    /**
     * 根据查询请求获取设备信息统计
     * <p>
     * 此方法首先从线程本地变量中获取当前登录用户的账户编号，然后根据查询请求中的条件，
     * 分别查询操作系统、浏览器和设备的访问统计信息最后，将这些信息转换为VO对象并封装到一个Map中返回
     *
     * @param request 查询设备信息的请求对象，包含查询所需的条件如代码、开始时间和结束时间
     * @return 返回一个Map，键分别是"os"、"browser"和"device"，值为对应的访问统计VO对象列表
     */
    @Override
    public Map<String, List<VisitStatsVO>> queryDeviceInfo(QueryDeviceRequest request) {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取查询请求中的代码字段
        String code = request.getCode();

        // 获取查询请求中的开始时间和结束时间字段
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();

        // 定义查询类型常量：操作系统、浏览器、设备
        String os = QueryDeviceEnum.OS.name().toLowerCase();
        String browser = QueryDeviceEnum.BROWSER.name().toLowerCase();
        String device = QueryDeviceEnum.DEVICE.name().toLowerCase();

        // 分别查询操作系统、浏览器和设备的访问统计信息
        List<VisitStats> osList = visitStatsMapper.queryDeviceInfo(code, accountNo, startTime, endTime, os);
        List<VisitStats> browserList = visitStatsMapper.queryDeviceInfo(code, accountNo, startTime, endTime, browser);
        List<VisitStats> deviceList = visitStatsMapper.queryDeviceInfo(code, accountNo, startTime, endTime, device);

        // 将查询结果转换为对应的VO对象列表
        List<VisitStatsVO> osVisitStatsVOS = osList.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        List<VisitStatsVO> browserVisitStatsVOS = browserList.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        List<VisitStatsVO> deviceVisitStatsVOS = deviceList.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());

        // 创建一个Map来封装所有的VO对象列表
        Map<String, List<VisitStatsVO>> map = new HashMap<>(3);
        map.put("os", osVisitStatsVOS);
        map.put("browser", browserVisitStatsVOS);
        map.put("device", deviceVisitStatsVOS);

        // 返回封装了所有访问统计信息的Map对象
        return map;
    }

    /**
     * 将VisitStats数据对象转换为VisitStatsVO视图对象
     * 此方法用于将数据库中的VisitStats实体类数据拷贝到业务逻辑层的VisitStatsVO对象中
     * 使用Spring框架的BeanUtils工具类来简化属性拷贝过程，提高代码的可读性和维护性
     *
     * @param visitStatsDO VisitStats数据对象，包含从数据库查询的原始数据
     * @return VisitStatsVO 转换后的视图对象，用于业务逻辑处理
     */
    private VisitStatsVO beanProcess(VisitStats visitStatsDO) {
        // 创建一个新的VisitStatsVO对象
        VisitStatsVO visitStatsVO = new VisitStatsVO();
        // 使用Spring的BeanUtils工具类简化属性拷贝
        BeanUtils.copyProperties(visitStatsDO, visitStatsVO);
        // 返回填充好的VisitStatsVO对象
        return visitStatsVO;
    }

}




