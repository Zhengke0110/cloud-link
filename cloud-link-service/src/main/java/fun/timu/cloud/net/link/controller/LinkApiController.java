package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.enums.ShortLinkStateEnum;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;
import fun.timu.cloud.net.link.service.LogService;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短链接API控制器
 */
@RestController
public class LinkApiController {

    private static Logger logger = LoggerFactory.getLogger(LinkApiController.class);

    private final ShortLinkService shortLinkService;
    private final LogService logService;

    public LinkApiController(ShortLinkService shortLinkService, LogService logService) {
        this.shortLinkService = shortLinkService;
        this.logService = logService;
    }


    /**
     * 转发请求
     * 解析 301还是302，这边是返回http code是302
     * <p>
     * 知识点一，为什么要用 301 跳转而不是 302 呐？
     * <p>
     * 301 是永久重定向，302 是临时重定向。
     * <p>
     * 短地址一经生成就不会变化，所以用 301 是同时对服务器压力也会有一定减少
     * <p>
     * 但是如果使用了 301，无法统计到短地址被点击的次数。
     * <p>
     * 所以选择302虽然会增加服务器压力，但是有很多数据可以获取进行分析
     *
     * @param shortLinkCode 短链接代码
     * @param request       HTTP请求对象
     * @param response      HTTP响应对象
     */
    @GetMapping(path = "/{shortLinkCode}")
    public void dispatch(@PathVariable(name = "shortLinkCode") String shortLinkCode, HttpServletRequest request, HttpServletResponse response) {

        //尝试解析并重定向短链接，同时处理可能的异常
        try {
            logger.info("短链码:{}", shortLinkCode);
            //判断短链码是否合规
            if (isLetterDigit(shortLinkCode)) {
                //查找短链
                ShortLinkVO shortLinkVO = shortLinkService.parseShortLinkCode(shortLinkCode);

                //记录短链访问日志
                if (shortLinkVO != null) {
                    logService.recordShortLinkLog(request, shortLinkCode, shortLinkVO.getAccountNo());
                }

                //判断是否过期和可用
                if (isVisitable(shortLinkVO)) {

                    String originalUrl = CommonUtil.removeUrlPrefix(shortLinkVO.getOriginalUrl());

                    //设置重定向地址
                    response.setHeader("Location", originalUrl);

                    //302跳转
                    response.setStatus(HttpStatus.FOUND.value());

                } else {
                    //短链无效或过期，返回404
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    return;
                }
            }
        } catch (Exception e) {
            //处理异常，返回500错误
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 检查字符串是否仅由字母和数字组成
     *
     * @param str 待检查的字符串
     * @return 如果字符串仅包含字母和数字，则返回true；否则返回false
     */
    private static boolean isLetterDigit(String str) {
        // 定义一个正则表达式，用于匹配仅由字母和数字组成的字符串
        String regex = "^[a-z0-9A-Z]+$";
        // 使用定义好的正则表达式进行匹配，并返回匹配结果
        return str.matches(regex);
    }

    private static long FOREVER_TIME = 315504000000L;

    /**
     * 检查短链接是否可访问
     *
     * @param shortLinkVO 短链接信息对象，包含短链接的相关信息
     * @return 如果短链接可访问返回true，否则返回false
     */
    private static boolean isVisitable(ShortLinkVO shortLinkVO) {
        // 检查短链接对象是否非空且未过期
        if ((shortLinkVO != null && shortLinkVO.getExpired().getTime() > CommonUtil.getCurrentTimestamp())) {
            // 检查短链接状态是否为激活状态
            if (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        } else if ((shortLinkVO != null && shortLinkVO.getExpired().getTime() < FOREVER_TIME)) {
            // 检查短链接状态是否为激活状态
            if (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        }

        return false;
    }

}
