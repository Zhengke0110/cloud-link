package fun.timu.cloud.net.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkDelRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkPageRequest;
import fun.timu.cloud.net.link.controller.request.ShortLinkUpdateRequest;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;

import java.util.Map;

/**
 * @author zhengke
 * @description 针对表【short_link】的数据库操作Service
 * @createDate 2025-04-15 11:23:41
 */
public interface ShortLinkService extends IService<ShortLink> {
    /**
     * 解析短链
     *
     * @param shortLinkCode
     * @return
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    /**
     * 创建短链
     *
     * @param request
     * @return
     */
    JsonData createShortLink(ShortLinkAddRequest request);


    /**
     * 分页查找短链
     *
     * @param request
     * @return
     */
    Map<String, Object> pageByGroupId(ShortLinkPageRequest request);

    /**
     * 删除短链
     *
     * @param request
     * @return
     */
    JsonData del(ShortLinkDelRequest request);

    /**
     * 更新
     *
     * @param request
     * @return
     */
    JsonData update(ShortLinkUpdateRequest request);

    /**
     * 处理新增短链消息
     *
     * @param eventMessage
     * @return
     */
    boolean handleAddShortLink(EventMessage eventMessage);


    /**
     * 更新短链
     *
     * @param eventMessage
     * @return
     */
    boolean handleUpdateShortLink(EventMessage eventMessage);


    /**
     * 删除短链
     *
     * @param eventMessage
     * @return
     */
    boolean handleDelShortLink(EventMessage eventMessage);

    /**
     * 查询任务状态
     *
     * @param taskId 任务ID
     * @return 返回任务状态的JsonData对象
     */
    JsonData queryTaskStatus(String taskId);
}
