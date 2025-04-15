package fun.timu.cloud.net.link.manager;

import fun.timu.cloud.net.common.enums.DomainTypeEnum;
import fun.timu.cloud.net.link.model.DO.Domain;

import java.util.List;

public interface DomainManager {

    /**
     * 查找详情
     * @param id
     * @param accountNO
     * @return
     */
    Domain findById(Long id, Long accountNO);


    /**
     * 查找详情
     * @param id
     * @param domainTypeEnum
     * @return
     */
    Domain findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum);


    /**
     * 新增
     * @param domainDO
     * @return
     */
    int addDomain(Domain domainDO);


    /**
     * 列举全部官方域名
     * @return
     */
    List<Domain> listOfficialDomain();


    /**
     * 列举全部自定义域名
     * @return
     */
    List<Domain> listCustomDomain(Long accountNo);
}
