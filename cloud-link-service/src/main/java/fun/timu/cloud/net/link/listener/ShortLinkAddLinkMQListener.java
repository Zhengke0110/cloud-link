package fun.timu.cloud.net.link.listener;

import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import com.rabbitmq.client.Channel;

import fun.timu.cloud.net.link.service.ShortLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import java.io.IOException;


@Component
@RabbitListener(queues = "short_link.add.link.queue")
public class ShortLinkAddLinkMQListener {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkAddLinkMQListener.class);
    private final ShortLinkService shortLinkService;

    public ShortLinkAddLinkMQListener(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    /**
     * 处理短链接添加事件的消息处理器
     *
     * @param eventMessage 事件消息对象，包含事件的具体信息
     * @param message      RabbitMQ消息对象，用于获取消息的属性和确认消息消费
     * @param channel      RabbitMQ通道对象，用于消息确认
     * @throws IOException 当消息消费失败时可能抛出的异常
     */
    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        // 记录接收到的消息内容
        logger.info("监听到消息ShortLinkAddLinkMQListener message消息内容:{}", message);
        try {
            // 设置事件消息类型
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_ADD_LINK.name());
            // 调用服务处理添加短链接
            shortLinkService.handleAddShortLink(eventMessage);

        } catch (Exception e) {
            // 处理业务异常，还有进行其他操作，比如记录失败原因
            logger.error("消费失败:{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        // 记录消息消费成功
        logger.info("消费成功:{}", eventMessage);
    }

}
