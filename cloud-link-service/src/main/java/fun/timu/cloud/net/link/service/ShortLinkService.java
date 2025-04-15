package fun.timu.cloud.net.link.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;

/**
 * @author zhengke
 * @description 针对表【short_link】的数据库操作Service
 * @createDate 2025-04-15 11:23:41
 */
public interface ShortLinkService extends IService<ShortLink> {

    /**
     * 解析短链
     *
     * @param shortLinkCode 短链码
     * @return 短链信息
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    /**
     * 创建短链
     *
     * @param request
     * @return
     */
    JsonData createShortLink(ShortLinkAddRequest request);
}
