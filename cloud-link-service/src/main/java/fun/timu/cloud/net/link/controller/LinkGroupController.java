package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.link.controller.request.LinkGroupAddRequest;
import fun.timu.cloud.net.link.controller.request.LinkGroupUpdateRequest;
import fun.timu.cloud.net.link.model.VO.LinkGroupVO;
import fun.timu.cloud.net.link.service.LinkGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group/v1")
public class LinkGroupController {

    private final LinkGroupService linkGroupService;

    public LinkGroupController(LinkGroupService linkGroupService) {
        this.linkGroupService = linkGroupService;
    }

    /**
     * 处理添加链接组的请求
     *
     * @param addRequest 包含要添加的链接组信息的请求体
     * @return 返回一个包含操作结果的JsonData对象
     */
    @PostMapping("/add")
    public JsonData add(@RequestBody LinkGroupAddRequest addRequest) {

        // 调用服务层方法尝试添加链接组
        int rows = linkGroupService.add(addRequest);

        // 根据添加操作的结果，构建并返回相应的JsonData对象
        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.GROUP_ADD_FAIL);

    }

    /**
     * 处理删除链接分组的请求
     *
     * @param groupId 需要删除的分组的ID
     * @return 返回一个包含删除操作结果的JsonData对象
     */
    @DeleteMapping("/del/{groupId}")
    public JsonData del(@PathVariable("groupId") Long groupId) {

        // 调用服务层方法，执行删除操作
        int rows = linkGroupService.del(groupId);

        // 根据删除操作的结果，构建并返回相应的JsonData对象
        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.GROUP_NOT_EXIST);

    }

    /**
     * 获取指定ID的友链分组详细信息
     *
     * @param groupId 友链分组的唯一标识符
     * @return 包含友链分组详细信息的JsonData对象
     */
    @GetMapping("detail/{groupId}")
    public JsonData detail(@PathVariable("groupId") Long groupId) {

        // 调用服务层方法获取友链分组详细信息
        LinkGroupVO linkGroupVO = linkGroupService.detail(groupId);

        // 构建并返回成功响应，包含友链分组详细信息
        return JsonData.buildSuccess(linkGroupVO);

    }

    /**
     * 获取所有链接分组信息
     *
     * 该方法通过GET请求处理[list]路径，返回所有链接分组的列表
     * 它调用了LinkGroupService中的listAllGroup方法来获取数据，并将结果封装到JsonData中返回
     *
     * @return JsonData 包含所有链接分组列表的Json响应对象
     */
    @GetMapping("list")
    public JsonData findUserAllLinkGroup() {

        // 调用服务层方法，获取所有链接分组列表
        List<LinkGroupVO> list = linkGroupService.listAllGroup();

        // 将获取到的链接分组列表封装到Json响应对象中，并返回成功信息
        return JsonData.buildSuccess(list);

    }

    /**
     * 处理更新链接分组的请求
     *
     * @param request 包含链接分组更新信息的请求对象
     * @return 返回更新操作的结果，成功或失败
     */
    @PutMapping("update")
    public JsonData update(@RequestBody LinkGroupUpdateRequest request) {
        // 执行链接分组的更新操作
        int rows = linkGroupService.updateById(request);
        // 根据更新结果构建并返回相应的Json数据
        return rows == 1 ? JsonData.buildSuccess() : JsonData.buildResult(BizCodeEnum.GROUP_OPER_FAIL);
    }


}
