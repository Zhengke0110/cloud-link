package fun.timu.cloud.net.shop.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class RabbitMQConfig {


    /**
     * 交换机
     */
    private String orderEventExchange = "order.event.exchange";


    /**
     * 延迟队列，不能被消费者监听
     */
    private String orderCloseDelayQueue = "order.close.delay.queue";

    /**
     * 关单队列，延迟队列的消息过期后转发的队列，用于被消费者监听
     */
    private String orderCloseQueue = "order.close.queue";


    /**
     * 进入到延迟队列的routingKey
     */
    private String orderCloseDelayRoutingKey = "order.close.delay.routing.key";


    /**
     * 进入死信队列的routingKey，消息过期进入死信队列的key
     */
    private String orderCloseRoutingKey = "order.close.delay.key";


    /**
     * 过期时间，毫秒单位，临时改为1分钟过期
     */
    private Integer ttl = 1000 * 60;


    /**
     * 配置消息转换器
     * <p>
     * 此方法用于定义一个Bean，该Bean是一个消息转换器，用于将消息转换为JSON格式
     * 它利用Jackson库来实现Java对象和JSON数据之间的相互转换
     *
     * @return MessageConverter的实例，用于消息的序列化和反序列化
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 创建交换机，topic类型，一般一个业务一个交换机
     *
     * @return
     */
    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange(orderEventExchange, true, false);
    }


    /**
     * 延迟队列
     *
     * @return
     */
    @Bean
    public Queue orderCloseDelayQueue() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", orderEventExchange);
        args.put("x-dead-letter-routing-key", orderCloseRoutingKey);
        args.put("x-message-ttl", ttl);
        return new Queue(orderCloseDelayQueue, true, false, false, args);

    }


    /**
     * 死信队列，是一个普通队列，用于被监听
     *
     * @return
     */
    @Bean
    public Queue orderCloseQueue() {
        return new Queue(orderCloseQueue, true, false, false);
    }


    /**
     * 创建一个绑定，将延迟队列与交换机进行绑定
     *
     * @return 返回一个绑定对象，用于定义队列与交换机之间的绑定关系
     */
    @Bean
    public Binding orderCloseDelayBinding() {
        // 实例化一个Binding对象，参数分别为：队列名称、绑定类型、交换机名称、路由键和额外参数
        // 此处将orderCloseDelayQueue队列与orderEventExchange交换机按照orderCloseDelayRoutingKey路由键进行绑定
        return new Binding(orderCloseDelayQueue, Binding.DestinationType.QUEUE, orderEventExchange, orderCloseDelayRoutingKey, null);
    }


    /**
     * 创建死信队列与死信交换机之间的绑定
     * <p>
     * 此方法定义了如何将死信队列绑定到死信交换机上，以便处理特定路由键的消息
     * 死信队列用于接收和处理那些因为某些原因（如：消息过期、队列满等）无法被正常消费的消息
     * 通过此绑定，具有特定路由键的消息将被定向到该死信队列，进行后续处理
     *
     * @return Binding对象，代表死信队列与死信交换机的绑定关系
     */
    @Bean
    public Binding orderCloseBinding() {

        // 创建并返回一个Binding对象，参数分别为：
        // - 队列名称（orderCloseQueue）
        // - 绑定的目标类型（QUEUE表示这是一个队列）
        // - 交换机名称（orderEventExchange）
        // - 路由键（orderCloseRoutingKey）
        // - 其他参数（null表示没有其他特殊参数）
        return new Binding(orderCloseQueue, Binding.DestinationType.QUEUE, orderEventExchange, orderCloseRoutingKey, null);
    }

    //=============订单支付成功配置===================

    /**
     * 更新订单 队列
     */
    private String orderUpdateQueue = "order.update.queue";

    /**
     * 订单发放流量包 队列
     */
    private String orderTrafficQueue = "order.traffic.queue";


    /**
     * 微信回调发送通知的routing key 【发送消息用】
     */
    private String orderUpdateTrafficRoutingKey = "order.update.traffic.routing.key";


    /**
     * topic类型的 用于绑定订单队列和交换机的
     */
    private String orderUpdateBindingKey = "order.update.*.routing.key";


    /**
     * topic类型的 用于绑定流量包发放队列和交换机
     */
    private String orderTrafficBindingKey = "order.*.traffic.routing.key";


    /**
     * 订单更新队列 和 交换机建立绑定关系
     *
     * @return
     */
    @Bean
    public Binding orderUpdateBinding() {

        // 创建一个绑定，将订单更新队列与订单事件交换机绑定，以便接收订单更新事件
        return new Binding(orderUpdateQueue, Binding.DestinationType.QUEUE, orderEventExchange, orderUpdateBindingKey, null);
    }


    /**
     * 发放流量包队列 和 交换机建立绑定关系
     *
     * @return
     */
    @Bean
    public Binding orderTrafficBinding() {
        // 创建一个绑定，将发放流量包队列与订单事件交换机进行绑定
        // 参数说明：
        // - orderTrafficQueue: 队列名称，表示要绑定的队列
        // - Binding.DestinationType.QUEUE: 指定目标类型为队列
        // - orderEventExchange: 交换机名称，表示要绑定的交换机
        // - orderTrafficBindingKey: 绑定键，用于路由消息到特定队列
        // - null: 表示没有额外的绑定参数
        return new Binding(orderTrafficQueue, Binding.DestinationType.QUEUE, orderEventExchange, orderTrafficBindingKey, null);
    }


    /**
     * 更新订单队列，普通队列，用于被监听消费
     *
     * @return
     */
    @Bean
    public Queue orderUpdateQueue() {
        // 创建一个名为orderUpdateQueue的队列，设置队列的持久化属性为true，排他性为false，自动删除为false
        return new Queue(orderUpdateQueue, true, false, false);
    }


    /**
     * 发放流量包队列，普通队列，用于被监听消费
     *
     * @return
     */
    @Bean
    public Queue orderTrafficQueue() {
        // 创建一个名为orderTrafficQueue的队列，设置队列的持久化属性为true，排他性为false，自动删除为false
        return new Queue(orderTrafficQueue, true, false, false);
    }


}
