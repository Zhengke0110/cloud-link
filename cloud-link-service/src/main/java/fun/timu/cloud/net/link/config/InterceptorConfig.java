package fun.timu.cloud.net.link.config;

import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final RedisTemplate<Object, Object> redisTemplate;

    public InterceptorConfig(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(redisTemplate);
    }

    /**
     * 添加拦截器配置
     * <p>
     * 该方法用于向应用程序添加拦截器，以在请求处理之前或之后执行特定逻辑
     * 主要用于配置登录拦截器，以确保只有经过身份验证的用户才能访问特定的API端点
     *
     * @param registry InterceptorRegistry的实例，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册一个登录拦截器实例
        registry.addInterceptor(loginInterceptor())
                // 添加需要拦截的路径模式
                // 这里指定了API的link、group和domain相关路径将被拦截
                .addPathPatterns("/api/link/*/**", "/api/group/*/**", "/api/domain/*/**")
                // 排除不需要拦截的路径模式
                // 当前未指定任何排除路径，所有匹配上述模式的请求都将被拦截
                .excludePathPatterns("");
    }
}
