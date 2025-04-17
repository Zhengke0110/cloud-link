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
@RabbitListener(queuesToDeclare = {@Queue("short_link.del.link.queue")})
public class ShortLinkDelLinkMQListener {

    private static Logger logger = LoggerFactory.getLogger(ShortLinkDelLinkMQListener.class);

    private final ShortLinkService shortLinkService;

    public ShortLinkDelLinkMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接删除事件的消息处理器
     * 该方法专门用于处理短链接删除的事件消息，当接收到相应的消息时执行特定的业务逻辑
     *
     * @param eventMessage 事件消息对象，包含事件的详细信息
     * @param message      RabbitMQ消息对象，包含消息的原始信息
     * @param channel      RabbitMQ通道对象，用于消息的发送和接收
     * @throws IOException 当消息处理过程中发生I/O错误时抛出
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkDelLinkMQListener message消息内容:{}", message);
        try {
            // 设置事件消息类型
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_DEL_LINK.name());
            // 调用服务处理删除短链接的业务逻辑
            shortLinkService.handleDelShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        // 记录消息消费成功日志
        logger.info("消费成功:{}", eventMessage);
    }


}
