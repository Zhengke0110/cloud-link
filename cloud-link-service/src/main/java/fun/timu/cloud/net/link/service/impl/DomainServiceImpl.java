package fun.timu.cloud.net.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.link.manager.DomainManager;
import fun.timu.cloud.net.link.mapper.DomainMapper;
import fun.timu.cloud.net.link.model.DO.Domain;
import fun.timu.cloud.net.link.model.VO.DomainVO;
import fun.timu.cloud.net.link.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengke
 * @description 针对表【domain】的数据库操作Service实现
 * @createDate 2025-04-15 15:46:08
 */
@Service
public class DomainServiceImpl extends ServiceImpl<DomainMapper, Domain>
        implements DomainService {
    private static Logger logger = LoggerFactory.getLogger(DomainService.class);
    private final DomainManager domainManager;

    public DomainServiceImpl(DomainManager domainManager) {
        this.domainManager = domainManager;
    }

    /**
     * 列出所有域名信息，包括自定义域名和官方域名
     * <p>
     * 该方法首先获取当前登录用户的账户编号，然后从域名管理器中获取该用户的所有自定义域名和所有官方域名
     * 最后，将这两种域名列表合并，并转换为DomainVO对象列表返回
     *
     * @return 包含所有域名信息的DomainVO对象列表
     */
    @Override
    public List<DomainVO> listAll() {
        // 获取当前登录用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 获取当前用户的所有自定义域名
        List<Domain> customDomainList = domainManager.listCustomDomain(accountNo);
        // 获取所有官方域名
        List<Domain> officialDomainList = domainManager.listOfficialDomain();

        // 将自定义域名和官方域名列表合并
        customDomainList.addAll(officialDomainList);

        // 将合并后的域名列表转换为DomainVO对象列表并返回
        return customDomainList.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
    }

    /**
     * 将Domain实体对象转换为DomainVO视图对象
     * <p>
     * 此方法的作用是将传入的Domain实体对象中的属性值复制到DomainVO视图对象中
     * 主要利用了Apache Commons BeanUtils库中的copyProperties方法，简化了属性复制的过程
     *
     * @param domainDO Domain实体对象，包含需要转换的数据
     * @return DomainVO视图对象，转换后包含与Domain实体对象相同的数据
     */
    private DomainVO beanProcess(Domain domainDO) {
        // 创建一个新的DomainVO对象实例
        DomainVO domainVO = new DomainVO();
        // 将Domain实体对象的属性值复制到DomainVO对象中
        BeanUtils.copyProperties(domainDO, domainVO);
        // 返回填充好数据的DomainVO对象
        return domainVO;
    }
}




