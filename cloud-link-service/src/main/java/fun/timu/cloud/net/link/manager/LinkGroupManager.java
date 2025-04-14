package fun.timu.cloud.net.link.manager;

import fun.timu.cloud.net.link.model.DO.LinkGroup;
import fun.timu.cloud.net.link.model.VO.LinkGroupVO;

import java.util.List;

/**
 * LinkGroupManager接口定义了对链接组(LinkGroup)的操作规范
 * 它提供了添加、删除、查询和更新链接组的方法
 */
public interface LinkGroupManager {

    /**
     * 添加一个新的链接组
     *
     * @param linkGroupDO 链接组的数据对象，包含链接组的详细信息
     * @return 插入操作的影响行数，表示添加成功与否
     */
    int add(LinkGroup linkGroupDO);

    /**
     * 删除指定用户下的某个链接组
     *
     * @param groupId   链接组的唯一标识符
     * @param accountNo 用户的账号编号，用于确认删除操作的执行者
     * @return 删除操作的影响行数，表示删除成功与否
     */
    int del(Long groupId, Long accountNo);

    /**
     * 获取指定用户下某个链接组的详细信息
     *
     * @param groupId   链接组的唯一标识符
     * @param accountNo 用户的账号编号，用于确认查询操作的执行者
     * @return 返回链接组的详细信息对象，如果没有找到，则返回null
     */
    LinkGroup detail(Long groupId, Long accountNo);

    /**
     * 列出指定用户下的所有链接组
     *
     * @param accountNo 用户的账号编号，用于确认查询操作的执行者
     * @return 包含用户所有链接组的列表，如果没有任何链接组，则返回空列表
     */
    List<LinkGroup> listAllGroup(Long accountNo);

    /**
     * 根据链接组的标识符更新链接组的信息
     *
     * @param linkGroupDO 包含更新后链接组信息的数据对象
     * @return 更新操作的影响行数，表示更新成功与否
     */
    int updateById(LinkGroup linkGroupDO);
}

