package fun.timu.cloud.net.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.link.controller.request.LinkGroupAddRequest;
import fun.timu.cloud.net.link.controller.request.LinkGroupUpdateRequest;
import fun.timu.cloud.net.link.model.DO.LinkGroup;
import fun.timu.cloud.net.link.model.VO.LinkGroupVO;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【link_group】的数据库操作Service
 * @createDate 2025-04-14 16:02:39
 */
public interface LinkGroupService extends IService<LinkGroup> {

    /**
     * 新增分组
     *
     * @param addRequest
     * @return
     */
    int add(LinkGroupAddRequest addRequest);

    /**
     * 删除分组
     *
     * @param groupId
     * @return
     */
    int del(Long groupId);

    /**
     * 详情
     *
     * @param groupId
     * @return
     */
    LinkGroupVO detail(Long groupId);

    /**
     * 列出用户全部分组
     *
     * @return
     */
    List<LinkGroupVO> listAllGroup();

    /**
     * 更新组名
     *
     * @param request
     * @return
     */
    int updateById(LinkGroupUpdateRequest request);
}
