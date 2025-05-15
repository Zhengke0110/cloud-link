package fun.timu.cloud.net.data.config;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor())
                //添加拦截的路径
                .addPathPatterns("/api/visitStats/*/**");

        //排除不拦截
        //.excludePathPatterns("/api/product/*/**");


    }
}
