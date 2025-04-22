package fun.timu.cloud.net.account.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitMQConfig {

    /**
     * 配置消息转换器
     *
     * 此方法用于定义一个Bean，该Bean是一个消息转换器，用于将消息转换为JSON格式
     * 它利用Jackson库来实现Java对象和JSON数据之间的相互转换
     *
     * @return MessageConverter的实例，用于消息的序列化和反序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
