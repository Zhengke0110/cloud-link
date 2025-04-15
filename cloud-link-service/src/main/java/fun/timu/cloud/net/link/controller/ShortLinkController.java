package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    public ShortLinkController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }


    /**
     * 创建短链接的控制器方法
     * 该方法接收一个HTTP POST请求，用于添加新的短链接信息
     *
     * @param request 包含短链接相关数据的请求对象，由请求体自动转换而来
     * @return 返回一个JsonData对象，包含处理结果和可能的数据
     */
    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest request) {

        // 调用服务层方法创建短链接
        JsonData jsonData = shortLinkService.createShortLink(request);

        // 返回处理结果
        return jsonData;
    }


}