package fun.timu.cloud.net.account.listener;

import com.rabbitmq.client.Channel;
import fun.timu.cloud.net.account.service.TrafficService;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.model.EventMessage;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = {@Queue("order.traffic.queue")})
@Slf4j
public class TrafficMQListener {

    private final TrafficService trafficService;

    public TrafficMQListener(TrafficService trafficService) {
        this.trafficService = trafficService;
    }


    /**
     * 处理交通相关消息的处理器
     * 该方法通过RabbitMQ监听交通相关的消息，并进行处理
     *
     * @param eventMessage 消息内容，包含交通相关的信息
     * @param message      RabbitMQ传递的原始消息对象，通常用于获取消息的详细属性
     * @param channel      消息通道，用于在处理过程中与RabbitMQ进行交互，如确认消息接收
     */
    @RabbitHandler
    public void trafficHandler(EventMessage eventMessage, Message message, Channel channel) {

        // 记录接收到的消息内容
        log.info("监听到消息trafficHandler:{}", eventMessage);

        try {
            // 调用业务服务处理交通消息
            trafficService.handleTrafficMessage(eventMessage);
        } catch (Exception e) {
            // 记录消费失败的消息，并抛出自定义异常，表示MQ消费异常
            log.error("消费者失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }

        // 记录消息消费成功
        log.info("消费成功:{}", eventMessage);

    }
}
