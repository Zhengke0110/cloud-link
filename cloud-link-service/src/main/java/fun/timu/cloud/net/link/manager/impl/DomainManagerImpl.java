package fun.timu.cloud.net.link.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.common.enums.DomainTypeEnum;
import fun.timu.cloud.net.link.manager.DomainManager;
import fun.timu.cloud.net.link.mapper.DomainMapper;
import fun.timu.cloud.net.link.model.DO.Domain;
import fun.timu.cloud.net.link.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainManagerImpl implements DomainManager {
    private static Logger logger = LoggerFactory.getLogger(DomainManager.class);
    private final DomainMapper domainMapper;

    public DomainManagerImpl(DomainMapper domainMapper) {
        this.domainMapper = domainMapper;
    }

    /**
     * 根据ID和账户编号查询域对象
     *
     * @param id        域对象的唯一标识符
     * @param accountNO 账户编号，用于关联特定的账户
     * @return 返回查询到的域对象，如果没有找到，则返回null
     *
     * 此方法使用MyBatis-Plus的QueryWrapper构建查询条件，通过domainMapper执行数据库查询
     * 它体现了通过主键和账户编号进行数据检索的业务需求，确保数据的准确性和关联性
     */
    @Override
    public Domain findById(Long id, Long accountNO) {
        return domainMapper.selectOne(new QueryWrapper<Domain>().eq("id", id).eq("account_no", accountNO));
    }

    /**
     * 根据域名类型和ID查找域名实体
     *
     * @param id 域名的唯一标识符
     * @param domainTypeEnum 域名类型枚举，用于区分不同的域名类型
     * @return 返回匹配的域名对象，如果不存在则返回null
     *
     * 此方法通过组合域名类型和ID来查询数据库中的域名记录
     * 它利用了MyBatis-Plus的QueryWrapper构建查询条件，以实现精确查询
     */
    @Override
    public Domain findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum) {
        return domainMapper.selectOne(new QueryWrapper<Domain>().eq("id", id).eq("domain_type", domainTypeEnum.name()));
    }

    /**
     * 添加域名对象到数据库
     *
     * @param domainDO 域名对象，包含域名的相关信息
     * @return 返回插入操作的影响行数
     */
    @Override
    public int addDomain(Domain domainDO) {
        // 调用Mapper接口的insert方法，将域名对象插入数据库
        return domainMapper.insert(domainDO);
    }

    /**
     * 列出官方域名
     *
     * 此方法用于从数据库中选择并返回所有官方域名该方法利用 MyBatis-Plus 框架的 QueryWrapper 构建查询条件，
     * 特别是针对 domain_type 字段，设置为其等于 DomainTypeEnum.OFFICIAL.name()，以筛选出官方域名
     *
     * @return 返回一个包含所有官方域名的列表
     */
    @Override
    public List<Domain> listOfficialDomain() {
        return domainMapper.selectList(new QueryWrapper<Domain>().eq("domain_type", DomainTypeEnum.OFFICIAL.name()));
    }

    /**
     * 列出指定账户下的自定义域名列表
     * <p>
     * 本方法通过查询数据库中指定账户编号（accountNo）的所有自定义域名（custom domain）来实现功能
     * 使用了MyBatis-Plus的QueryWrapper构造查询条件，限定域名类型为自定义，并且属于指定的账户
     *
     * @param accountNo 账户编号，用于查询属于该账户的自定义域名
     * @return 返回一个Domain对象列表，包含所有符合条件的自定义域名
     */
    @Override
    public List<Domain> listCustomDomain(Long accountNo) {
        return domainMapper.selectList(new QueryWrapper<Domain>()
                .eq("domain_type", DomainTypeEnum.CUSTOM.name())
                .eq("account_no", accountNo));
    }
}
