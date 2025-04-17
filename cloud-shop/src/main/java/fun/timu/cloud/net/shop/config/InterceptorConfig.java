package fun.timu.cloud.net.shop.config;

import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器配置
     * <p>
     * 该方法用于向应用程序添加拦截器，以在请求处理之前或之后执行特定逻辑
     * 主要用于配置登录拦截器，以确保未登录的用户无法访问受保护的资源
     *
     * @param registry InterceptorRegistry的实例，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册一个登录拦截器实例
        registry.addInterceptor(new LoginInterceptor())
                // 添加拦截的路径
                .addPathPatterns("")
                // 排除不拦截的路径
                .excludePathPatterns("/api/product/*/**");
    }
}
