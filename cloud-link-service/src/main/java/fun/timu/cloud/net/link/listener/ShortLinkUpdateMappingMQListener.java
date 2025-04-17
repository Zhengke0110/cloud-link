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
@RabbitListener(queuesToDeclare = {@Queue("short_link.update.mapping.queue")})
public class ShortLinkUpdateMappingMQListener {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkUpdateMappingMQListener.class);

    private final ShortLinkService shortLinkService;

    public ShortLinkUpdateMappingMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接更新映射的消息
     * 该方法监听特定消息队列中的事件消息，当接收到消息时，更新短链接的相关映射信息
     *
     * @param eventMessage 事件消息对象，包含消息的具体内容和相关信息
     * @param message      消息对象，包含消息的元数据，如消息ID、标签等
     * @param channel      消息通道对象，用于确认消息已被成功消费
     * @throws IOException 当消息消费过程中发生I/O错误时抛出此异常
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkUpdateMappingMQListener message消息内容:{}", message);
        try {
            // 设置事件消息类型，以便后续处理
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_UPDATE_MAPPING.name());
            shortLinkService.handleUpdateShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        // 记录消息消费成功日志
        logger.info("消费成功:{}", eventMessage);

    }
}
