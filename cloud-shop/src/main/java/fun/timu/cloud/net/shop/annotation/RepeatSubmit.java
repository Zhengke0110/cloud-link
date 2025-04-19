package fun.timu.cloud.net.shop.annotation;

import java.lang.annotation.*;
/**
 * RepeatSubmit注解用于防止接口的重复提交
 * 它通过在方法上添加注解来实现防重复提交的逻辑
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 防重提交，支持两种，一个是方法参数，一个是令牌
     */
    enum Type {PARAM, TOKEN}

    /**
     * 默认防重提交，是方法参数
     *
     * @return
     */
    Type limitType() default Type.PARAM;

    /**
     * 加锁过期时间，默认是5秒
     *
     * @return
     */
    long lockTime() default 5;

}
