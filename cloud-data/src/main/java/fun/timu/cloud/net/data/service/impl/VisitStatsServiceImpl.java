package fun.timu.cloud.net.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.data.controller.request.VisitRecordPageRequest;
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




