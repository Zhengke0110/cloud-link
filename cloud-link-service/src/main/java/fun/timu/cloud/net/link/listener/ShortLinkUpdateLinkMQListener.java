package fun.timu.cloud.net.link.listener;

import com.rabbitmq.client.Channel;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queuesToDeclare = {@Queue("short_link.update.link.queue")})
public class ShortLinkUpdateLinkMQListener {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkUpdateLinkMQListener.class);

    private final ShortLinkService shortLinkService;

    public ShortLinkUpdateLinkMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接更新事件的消息处理器
     * 该方法监听特定消息队列中的事件消息，当检测到短链接更新事件时进行处理
     *
     * @param eventMessage 事件消息对象，包含事件的详细信息
     * @param message      RabbitMQ消息对象，包含消息的原始信息
     * @param channel      RabbitMQ通道对象，用于消息的发送和接收
     *                     <p>
     *                     当监听到消息时，首先记录日志，然后尝试设置事件消息类型
     *                     如果在处理过程中遇到异常，则记录错误日志并抛出业务异常
     *                     最后，如果一切正常，记录消费成功的日志
     * @throws IOException  当消息处理过程中发生I/O错误时抛出
     * @throws BizException 当消息消费失败时抛出业务异常
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkUpdateLinkMQListener message消息内容:{}", message);
        try {
            // 设置事件消息类型为短链接更新链接
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_UPDATE_LINK.name());
            shortLinkService.handleUpdateShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        // 记录消费成功的日志
        logger.info("消费成功:{}", eventMessage);
    }


}
