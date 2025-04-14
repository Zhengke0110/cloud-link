package fun.timu.cloud.net.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.link.controller.request.LinkGroupAddRequest;
import fun.timu.cloud.net.link.controller.request.LinkGroupUpdateRequest;
import fun.timu.cloud.net.link.manager.LinkGroupManager;
import fun.timu.cloud.net.link.mapper.LinkGroupMapper;
import fun.timu.cloud.net.link.model.DO.LinkGroup;
import fun.timu.cloud.net.link.model.VO.LinkGroupVO;
import fun.timu.cloud.net.link.service.LinkGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengke
 * @description 针对表【link_group】的数据库操作Service实现
 * @createDate 2025-04-14 16:02:39
 */
@Service
public class LinkGroupServiceImpl extends ServiceImpl<LinkGroupMapper, LinkGroup> implements LinkGroupService {
    private static Logger logger = LoggerFactory.getLogger(LinkGroupService.class);

    private final LinkGroupManager linkGroupManager;

    public LinkGroupServiceImpl(LinkGroupManager linkGroupManager) {
        this.linkGroupManager = linkGroupManager;
    }

    /**
     * 添加新的链接组
     * <p>
     * 此方法用于处理链接组的添加请求它从线程本地变量中获取当前用户的账户编号，
     * 并使用请求中的标题创建一个新的链接组对象，然后将其插入到数据库中
     *
     * @param addRequest 包含要添加的链接组信息的请求对象
     * @return 返回插入操作影响的行数，表示添加是否成功
     */
    @Override
    public int add(LinkGroupAddRequest addRequest) {

        // 获取当前用户的账户编号，用于关联新创建的链接组
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 创建一个新的链接组对象，并设置其标题和所属用户账户编号
        LinkGroup linkGroupDO = new LinkGroup();
        linkGroupDO.setTitle(addRequest.getTitle());
        linkGroupDO.setAccountNo(accountNo);

        // 调用链接组管理器的添加方法，将新的链接组插入数据库
        int rows = linkGroupManager.add(linkGroupDO);

        // 返回插入操作的结果
        return rows;
    }

    /**
     * 删除链接分组
     * <p>
     * 该方法用于删除一个特定的链接分组它首先从ThreadLocal中获取当前用户的账户编号，
     * 然后调用linkGroupManager的del方法，传入分组ID和账户编号来执行删除操作
     *
     * @param groupId 分组ID，用于标识要删除的链接分组
     * @return 返回删除操作的结果，通常是一个表示影响行数的整数
     */
    @Override
    public int del(Long groupId) {
        // 从ThreadLocal中获取当前用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用linkGroupManager的del方法执行删除操作
        return linkGroupManager.del(groupId, accountNo);
    }

    /**
     * 根据组ID获取链接组详细信息
     * <p>
     * 此方法首先从LoginInterceptor的threadLocal中获取当前用户的账户编号，
     * 然后调用linkGroupManager的detail方法根据组ID和账户编号查询链接组信息，
     * 最后将查询到的信息转换为LinkGroupVO对象并返回
     *
     * @param groupId 链接组的ID，用于查询特定链接组的详细信息
     * @return LinkGroupVO对象，包含链接组的详细信息
     */
    @Override
    public LinkGroupVO detail(Long groupId) {
        // 从threadLocal中获取当前用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用linkGroupManager的detail方法，根据组ID和账户编号查询链接组信息
        LinkGroup linkGroupDO = linkGroupManager.detail(groupId, accountNo);

        // 创建LinkGroupVO对象，用于存储链接组的详细信息
        LinkGroupVO linkGroupVO = new LinkGroupVO();

        // 使用MapStruct工具将linkGroupDO对象的属性复制到linkGroupVO对象中
        BeanUtils.copyProperties(linkGroupDO, linkGroupVO);

        // 返回包含链接组详细信息的LinkGroupVO对象
        return linkGroupVO;
    }

    /**
     * 获取当前用户的所有友链分组列表
     * <p>
     * 本方法首先从ThreadLocal中获取当前用户的账户编号，然后调用linkGroupManager的listAllGroup方法
     * 获取数据库中的友链分组信息列表，最后将这些信息转换为LinkGroupVO对象列表并返回
     *
     * @return 当前用户的友链分组列表
     */
    @Override
    public List<LinkGroupVO> listAllGroup() {
        // 从ThreadLocal中获取当前用户的账户编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 调用linkGroupManager的listAllGroup方法，获取当前用户的所有友链分组信息列表
        List<LinkGroup> linkGroupDOList = linkGroupManager.listAllGroup(accountNo);

        // 将获取到的友链分组信息列表转换为LinkGroupVO对象列表
        List<LinkGroupVO> groupVOList = linkGroupDOList.stream().map(obj -> {
            // 创建一个新的LinkGroupVO对象，并将友链分组信息复制到该对象中
            LinkGroupVO linkGroupVO = new LinkGroupVO();
            BeanUtils.copyProperties(obj, linkGroupVO);
            return linkGroupVO;
        }).collect(Collectors.toList());

        // 返回转换后的友链分组列表
        return groupVOList;
    }

    /**
     * 根据ID更新链接组信息
     * <p>
     * 此方法首先从ThreadLocal中获取当前登录用户的账号信息，然后将更新请求中的数据转换为LinkGroup对象，
     * 并调用linkGroupManager的updateById方法来执行更新操作
     *
     * @param request 包含要更新的链接组信息的请求对象，包括标题和ID
     * @return 返回受影响的行数，表示更新操作是否成功
     */
    @Override
    public int updateById(LinkGroupUpdateRequest request) {
        // 获取当前登录用户的账号信息，用于记录是谁进行了更新操作
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 创建一个LinkGroup对象，准备更新数据库中的记录
        LinkGroup linkGroupDO = new LinkGroup();
        // 设置链接组的新标题
        linkGroupDO.setTitle(request.getTitle());
        // 设置链接组的ID，标识是哪个链接组
        linkGroupDO.setId(request.getId());
        // 设置账号信息，表明是哪个用户进行了更新
        linkGroupDO.setAccountNo(accountNo);

        // 调用linkGroupManager的updateById方法，执行数据库更新操作
        int rows = linkGroupManager.updateById(linkGroupDO);

        // 返回受影响的行数，告知调用者更新结果
        return rows;
    }
}




