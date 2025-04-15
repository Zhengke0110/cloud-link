package fun.timu.cloud.net.link.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQErrorConfig {


    /**
     * 异常交换机
     */
    private String shortLinkErrorExchange = "short_link.error.exchange";

    /**
     * 异常队列
     */
    private String shortLinkErrorQueue = "short_link.error.queue";

    /**
     * 异常routing.key
     */
    private String shortLinkErrorRoutingKey = "short_link.error.routing.key";


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建异常交换机
     *
     * 该方法用于声明一个异常处理的Topic交换机
     * Topic交换机允许消息根据主题中的模式进行路由，提供了一种灵活的消息路由方式
     * 此处的交换机被配置为持久化和非自动删除，确保即使RabbitMQ服务重启，交换机配置也能保留
     *
     * @return TopicExchange对象，用于处理异常消息的路由
     */
    @Bean
    public TopicExchange errorTopicExchange() {

        // 创建一个名为shortLinkErrorExchange的Topic交换机，设置为持久化且不自动删除
        return new TopicExchange(shortLinkErrorExchange, true, false);
    }

    /**
     * 创建异常队列
     *
     * @return 异常队列实例
     */
    @Bean
    public Queue errorQueue() {
        // durable设为true以确保队列在RabbitMQ重启后仍然存在
        return new Queue(shortLinkErrorQueue, true);
    }


    /**
     * 建立绑定关系
     *
     * 此方法定义了一个Bean，用于创建一个绑定关系，该绑定关系将错误队列与错误主题交换机绑定在一起
     * 绑定过程使用了RabbitMQ提供的BindingBuilder工具类，通过指定队列、交换机和路由键来完成绑定
     *
     * @return 返回建立的绑定关系对象
     */
    @Bean
    public Binding bindingErrorQueueAndExchange() {

        // 使用BindingBuilder工具类建立绑定关系，将错误队列绑定到错误主题交换机上，并指定路由键
        return BindingBuilder.bind(errorQueue()).to(errorTopicExchange()).with(shortLinkErrorRoutingKey);
    }


    /**
     * 配置  RepublishMessageRecoverer
     * <p>
     * 消费消息重试一定次数后，用特定的routingKey转发到指定的交换机中，方便后续排查和告警
     *
     * @return
     */
    @Bean
    public MessageRecoverer messageRecoverer() {

        return new RepublishMessageRecoverer(rabbitTemplate, shortLinkErrorExchange, shortLinkErrorRoutingKey);
    }


}
