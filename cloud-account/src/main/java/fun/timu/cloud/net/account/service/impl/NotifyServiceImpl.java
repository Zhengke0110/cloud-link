package fun.timu.cloud.net.account.service.impl;

import fun.timu.cloud.net.account.service.NotifyService;
import fun.timu.cloud.net.constant.RedisKey;
import fun.timu.cloud.net.enums.BizCodeEnum;
import fun.timu.cloud.net.enums.SendCodeEnum;
import fun.timu.cloud.net.util.CheckUtil;
import fun.timu.cloud.net.util.CommonUtil;
import fun.timu.cloud.net.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class NotifyServiceImpl implements NotifyService {
    private static Logger logger = LoggerFactory.getLogger(NotifyService.class);

    private static final int CODE_EXPIRED = 60 * 1000 * 10;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送验证码方法
     * 根据发送验证码的类型和目标地址，生成验证码，并发送
     * 该方法首先会检查是否有未过期的验证码存在，如果存在且时间间隔不足60秒，则不允许重复发送
     * 验证码生成后，会根据目标地址类型（邮箱或手机）选择合适的发送方式
     *
     * @param sendCodeEnum 发送验证码的类型，如注册、找回密码等
     * @param to           目标地址，可以是邮箱或手机号
     * @return 返回一个JsonData对象，包含发送结果
     */
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        //根据验证码类型和目标地址生成缓存键
        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);

        //从Redis中获取缓存的验证码值
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        //如果不为空，再判断是否是60秒内重复发送 0122_232131321314132
        if (StringUtils.isNotBlank(cacheValue)) {
            //解析验证码中的时间戳
            long ttl = Long.parseLong(cacheKey.split("_")[1]);
            //当前时间戳-验证码发送的时间戳，如果小于60秒，则不给重复发送
            long leftTime = CommonUtil.getCurrentTimestamp() - ttl;
            if (leftTime < (1000 * 60)) {
                //记录日志并返回错误信息
                logger.info("重复发送短信验证码，时间间隔:{}秒", leftTime);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        //生成6位随机验证码
        String code = CommonUtil.getRandomCode(6);
        //生成拼接好验证码
        String value = code + "_" + CommonUtil.getCurrentTimestamp();
        //将验证码和时间戳存入Redis，并设置过期时间
        redisTemplate.opsForValue().set(cacheKey, value, CODE_EXPIRED, TimeUnit.MILLISECONDS);

        //根据目标地址类型选择发送方式
        if (CheckUtil.isEmail(to)) {
            //TODO 发送邮箱验证码
        } else if (CheckUtil.isPhone(to)) {
            //TODO 发送手机验证码
        }
        //返回成功信息
        return JsonData.buildSuccess("验证码发送成功:" + code);
    }
}
