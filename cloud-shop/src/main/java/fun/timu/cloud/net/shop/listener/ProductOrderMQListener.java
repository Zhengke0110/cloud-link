package fun.timu.cloud.net.shop.listener;

import com.rabbitmq.client.Channel;
import fun.timu.cloud.net.common.exception.BizException;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.shop.controller.ProductOrderController;
import fun.timu.cloud.net.shop.service.ProductOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = {@Queue("order.close.queue"), @Queue("order.update.queue")})
public class ProductOrderMQListener {
    private static Logger logger = LoggerFactory.getLogger(ProductOrderController.class);

    private final ProductOrderService productOrderService;

    public ProductOrderMQListener(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }


    /**
     * 处理产品订单的消息监听器
     * 该方法主要用于监听和处理产品订单相关事件消息
     *
     * @param eventMessage 事件消息对象，包含订单事件的具体信息
     * @param message      RabbitMQ消息对象，用于获取消息的详细信息
     * @param channel      RabbitMQ通道对象，用于消息的确认或拒绝
     */
    @RabbitHandler
    public void productOrderHandler(EventMessage eventMessage, Message message, Channel channel) {
        // 记录接收到的消息内容
        logger.info("监听到消息ProductOrderMQListener messsage消息内容:{}", message);

        try {
            productOrderService.handleProductOrderMessage(eventMessage);
        } catch (Exception e) {
            // 记录消费者失败日志
            logger.error("消费者失败:{}", eventMessage);
            // 抛出业务异常，表示消息消费失败
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }

        // 记录消费成功日志
        logger.info("消费成功:{}", eventMessage);
    }


}
