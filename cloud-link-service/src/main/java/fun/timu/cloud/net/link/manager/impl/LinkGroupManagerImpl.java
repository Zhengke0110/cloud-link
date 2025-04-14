package fun.timu.cloud.net.link.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.link.manager.LinkGroupManager;
import fun.timu.cloud.net.link.mapper.LinkGroupMapper;
import fun.timu.cloud.net.link.model.DO.LinkGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkGroupManagerImpl implements LinkGroupManager {
    private final LinkGroupMapper linkGroupMapper;

    public LinkGroupManagerImpl(LinkGroupMapper linkGroupMapper) {
        this.linkGroupMapper = linkGroupMapper;
    }

    /**
     * 添加新的链接分组
     * <p>
     * 此方法通过调用linkGroupMapper的insert方法，将一个LinkGroup对象插入到数据库中
     * 选择@Override注解是为了表明这是一个重写的方法，提高了代码的可读性和可维护性
     *
     * @param linkGroupDO 待插入的链接分组对象，包含分组的相关信息
     * @return 返回插入操作的结果，通常是一个表示受影响行数的整数
     */
    @Override
    public int add(LinkGroup linkGroupDO) {
        return linkGroupMapper.insert(linkGroupDO);
    }

    /**
     * 删除指定的链接分组
     * <p>
     * 该方法通过MyBatis Plus的Mapper接口，根据提供的分组ID和账户编号来删除对应的链接分组
     * 主要用于维护链接分组数据的一致性和完整性
     *
     * @param groupId   分组ID，用于标识一个具体的链接分组
     * @param accountNo 账户编号，用于标识执行删除操作的用户账户
     * @return 返回删除操作影响的行数，通常为0（未删除）或1（删除成功）
     */
    public int del(Long groupId, Long accountNo) {
        return linkGroupMapper.delete(new QueryWrapper<LinkGroup>().eq("id", groupId).eq("account_no", accountNo));
    }

    /**
     * 根据组ID和账户编号详细查询链接组信息
     *
     * @param groupId   组ID，用于标识特定的链接组
     * @param accountNo 账户编号，用于标识特定的账户
     * @return 返回查询到的链接组对象，如果未找到，则返回null
     */
    @Override
    public LinkGroup detail(Long groupId, Long accountNo) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件，查询指定组ID和账户编号对应的链接组信息
        return linkGroupMapper.selectOne(new QueryWrapper<LinkGroup>().eq("id", groupId).eq("account_no", accountNo));
    }

    /**
     * 列出所有链接组
     * <p>
     * 此方法根据用户账号查询并返回该用户所有的链接组
     * 它通过调用LinkGroupMapper的selectList方法，并传入一个根据账户编号查询的QueryWrapper对象来实现
     *
     * @param accountNo 用户账号，用于查询链接组的依据
     * @return List<LinkGroup> 返回一个LinkGroup对象列表，包含用户所有的链接组
     */
    @Override
    public List<LinkGroup> listAllGroup(Long accountNo) {
        return linkGroupMapper.selectList(new QueryWrapper<LinkGroup>().eq("account_no", accountNo));
    }

    /**
     * 根据ID更新友链分组信息
     *
     * @param linkGroupDO 友链分组的实体类对象，包含要更新的友链分组的信息
     * @return 更新操作影响的行数，表示更新是否成功
     */
    @Override
    public int updateById(LinkGroup linkGroupDO) {
        // 执行更新操作，根据友链分组的ID和账号编号来定位要更新的记录
        return linkGroupMapper.update(linkGroupDO, new QueryWrapper<LinkGroup>().eq("id", linkGroupDO.getId()).eq("account_no", linkGroupDO.getAccountNo()));
    }
}
