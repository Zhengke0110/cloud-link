package fun.timu.cloud.net.link.listener;

import com.rabbitmq.client.Channel;
import fun.timu.cloud.net.common.model.EventMessage;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RabbitListener(queuesToDeclare = {@Queue("short_link.error.queue")})
public class ShortLinkErrorMQListener {


    /**
     * 处理短链接错误事件的消息监听器方法
     * 该方法用于接收和处理来自RabbitMQ的消息，特别关注短链接错误事件
     *
     * @param eventMessage 事件消息对象，包含短链接错误的详细信息
     * @param message      RabbitMQ消息对象，包含消息的元数据
     * @param channel      RabbitMQ通道对象，用于消息的发送和接收
     * @throws IOException 当消息处理过程中发生I/O错误时抛出
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的短链接错误事件消息内容
        log.error("告警：监听到消息ShortLinkErrorMQListener eventMessage消息内容:{}", eventMessage);
        // 记录RabbitMQ消息的详细信息
        log.error("告警：Message:{}", message);
        // 当处理成功时，记录告警日志并发送通知短信
        log.error("告警成功，发送通知短信");
    }


}