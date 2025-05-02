package fun.timu.cloud.net.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.cloud.net.data.model.DO.VisitStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【visit_stats】的数据库操作Mapper
 * @createDate 2025-05-02 22:15:26
 * @Entity generator.domain.VisitStats
 */
public interface VisitStatsMapper extends BaseMapper<VisitStats> {
    /**
     * 分页查询
     *
     * @param code
     * @param accountNo
     * @param from
     * @param size
     * @return
     */
    List<VisitStats> pageVisitRecord(@Param("code") String code, @Param("accountNo") Long accountNo,
                                     @Param("from") int from, @Param("size") int size);

    /**
     * 计算总条数
     *
     * @param code
     * @param accountNo
     * @return
     */
    int countTotal(@Param("code") String code, @Param("accountNo") Long accountNo);


}




