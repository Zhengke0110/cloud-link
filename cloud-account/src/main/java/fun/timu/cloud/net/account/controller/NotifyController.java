package fun.timu.cloud.net.account.controller;


import com.google.code.kaptcha.Producer;
import fun.timu.cloud.net.account.controller.request.SendCodeRequest;
import fun.timu.cloud.net.account.service.NotifyService;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.SendCodeEnum;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/notify/v1")
public class NotifyController {
    private static Logger logger = LoggerFactory.getLogger(NotifyController.class);

    private final NotifyService notifyService;

    private final Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 验证码过期时间
     */
    private static final long CAPTCHA_CODE_EXPIRED = 1000 * 10 * 60;


    public NotifyController(NotifyService notifyService, Producer captchaProducer) {
        this.notifyService = notifyService;
        this.captchaProducer = captchaProducer;
    }


    private String getCaptchaKey(HttpServletRequest request) {
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "account-service:captcha:" + CommonUtil.MD5(ip + userAgent);
        logger.info("验证码key:{}", key);
        return key;
    }


    /**
     * 生成验证码
     *
     * @param request  HTTP请求对象，用于获取生成验证码的会话信息
     * @param response HTTP响应对象，用于输出生成的验证码图片
     */
    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        // 生成验证码文本
        String captchaText = captchaProducer.createText();
        // 记录日志，方便调试和追踪
        logger.info("验证码内容:{}", captchaText);

        // 将验证码文本存储到Redis中，并设置过期时间
        // 这里使用了Redis的字符串操作，将验证码与会话关联，以便后续验证
        redisTemplate.opsForValue().set(getCaptchaKey(request), captchaText, CAPTCHA_CODE_EXPIRED, TimeUnit.MILLISECONDS);

        // 根据验证码文本生成验证码图片
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);

        // 尝试使用响应流输出验证码图片
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 将生成的验证码图片以JPEG格式写入响应流
            ImageIO.write(bufferedImage, "jpg", outputStream);
            // 刷新响应流，确保验证码图片被正确输出
            outputStream.flush();
        } catch (IOException e) {
            // 捕获IO异常，并记录错误日志
            logger.error("获取流出错:{}", e.getMessage());
        }
    }

    /**
     * 处理发送验证码请求
     * 该方法用于验证用户提交的验证码，并在验证成功后发送新的验证码
     *
     * @param sendCodeRequest 包含用户提交的验证码和接收新验证码的目标地址
     * @param request         HTTP请求对象，用于获取会话信息
     * @return 返回一个JsonData对象，包含发送结果
     */
    @PostMapping("send_code")
    public JsonData sendCode(@RequestBody SendCodeRequest sendCodeRequest, HttpServletRequest request) {
        //获取验证码的缓存键
        String key = getCaptchaKey(request);

        //从缓存中获取存储的验证码
        String cacheCaptcha = redisTemplate.opsForValue().get(key);

        //获取用户提交的验证码
        String captcha = sendCodeRequest.getCaptcha();

        //比较用户提交的验证码和缓存中的验证码
        if (captcha != null && cacheCaptcha != null && cacheCaptcha.equalsIgnoreCase(captcha)) {
            //验证码匹配，删除缓存中的验证码
            redisTemplate.delete(key);
            //调用服务发送新的验证码，并返回发送结果
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER, sendCodeRequest.getTo());
            return jsonData;
        } else {
            //验证码不匹配，返回错误信息
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }

}
