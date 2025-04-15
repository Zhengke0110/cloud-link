package fun.timu.cloud.net.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.link.model.DO.Domain;
import fun.timu.cloud.net.link.model.VO.DomainVO;

import java.util.List;

/**
* @author zhengke
* @description 针对表【domain】的数据库操作Service
* @createDate 2025-04-15 15:46:08
*/
public interface DomainService extends IService<Domain> {
    /**
     * 列举全部可用域名
     * @return
     */
    List<DomainVO> listAll();
}
