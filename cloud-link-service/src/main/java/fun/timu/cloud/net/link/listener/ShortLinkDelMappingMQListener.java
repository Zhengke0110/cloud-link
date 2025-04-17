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
@RabbitListener(queuesToDeclare = {@Queue("short_link.del.mapping.queue")})
public class ShortLinkDelMappingMQListener {

    private static Logger logger = LoggerFactory.getLogger(ShortLinkDelMappingMQListener.class);

    private final ShortLinkService shortLinkService;

    public ShortLinkDelMappingMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接删除映射消息的监听器方法
     * 该方法专门用于处理短链接相关的消息，以便进行删除映射的操作
     *
     * @param eventMessage 事件消息对象，包含消息的详细信息
     * @param message      RabbitMQ消息对象，用于获取消息的原始内容
     * @param channel      RabbitMQ通道对象，用于消息的确认或拒绝
     * @throws IOException 当消息处理失败时抛出IOException
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkDelMappingMQListener message消息内容:{}", message);
        try {
            // 设置事件消息类型为短链接删除映射
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_DEL_MAPPING.name());
            shortLinkService.handleDelShortLink(eventMessage);
        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        // 记录成功消费的消息
        logger.info("消费成功:{}", eventMessage);
    }


}