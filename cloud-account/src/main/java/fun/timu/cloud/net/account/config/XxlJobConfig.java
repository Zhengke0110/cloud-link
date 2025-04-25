package fun.timu.cloud.net.account.config;


import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class XxlJobConfig {


    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    /**
     * 配置并初始化XxlJob的Spring执行器
     *
     * 该方法是一个Spring的@Bean注解修饰的方法，用于创建并配置一个XxlJobSpringExecutor实例
     * 这个执行器用于与XxlJob的Admin后台进行通信，并处理调度过来的任务
     *
     * @return XxlJobSpringExecutor实例，用于执行XxlJob任务
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        // 初始化日志记录，包含关键配置信息
        log.info("流量包 xxl job 配置初始化, adminAddresses: {}, appName: {}, ip: {}, port: {}, logPath: {}, logRetentionDays: {}",
                adminAddresses, appName, ip, port, logPath, logRetentionDays);

        // 创建XxlJobSpringExecutor实例
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        // 设置Admin后台地址
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        // 设置应用名称
        xxlJobSpringExecutor.setAppname(appName);
        // 设置执行器IP
        xxlJobSpringExecutor.setIp(ip);
        // 设置执行器端口
        xxlJobSpringExecutor.setPort(port);
        // 设置访问令牌，用于安全认证
        xxlJobSpringExecutor.setAccessToken(accessToken);
        // 设置日志文件路径
        xxlJobSpringExecutor.setLogPath(logPath);
        // 设置日志保留天数
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        // 返回配置好的XxlJobSpringExecutor实例
        return xxlJobSpringExecutor;
    }


}