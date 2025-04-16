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
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "short_link.add.mapping.queue")
public class ShortLinkAddMappingMQListener {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkAddMappingMQListener.class);

    private final ShortLinkService shortLinkService;

    public ShortLinkAddMappingMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接添加映射的MQ消息
     * 当接收到短链接添加映射的消息时，此方法会被调用
     *
     * @param eventMessage 消息内容，包含事件相关信息
     * @param message      消息对象，包含消息的元属性
     * @param channel      消息通道，用于确认消息消费成功
     * @throws IOException 当消息消费成功后，确认消息时可能抛出的异常
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkAddMappingMQListener message消息内容:{}", message);

        try {

            // 设置消息类型
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_ADD_MAPPING.name());
            // 调用服务处理添加短链接的请求
            shortLinkService.handlerAddShortLink(eventMessage);

        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }

        // 记录消息消费成功
        logger.info("消费成功:{}", eventMessage);
    }
}
