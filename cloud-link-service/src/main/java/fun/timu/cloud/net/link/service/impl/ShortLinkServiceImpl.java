package fun.timu.cloud.net.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.common.enums.EventMessageType;
import fun.timu.cloud.net.common.interceptor.LoginInterceptor;
import fun.timu.cloud.net.common.model.EventMessage;
import fun.timu.cloud.net.common.util.IDUtil;
import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.link.config.RabbitMQConfig;
import fun.timu.cloud.net.link.controller.request.ShortLinkAddRequest;
import fun.timu.cloud.net.link.manager.ShortLinkManager;
import fun.timu.cloud.net.link.mapper.ShortLinkMapper;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【short_link】的数据库操作Service实现
 * @createDate 2025-04-15 11:23:41
 */
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements ShortLinkService {

    private final ShortLinkManager shortLinkManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public ShortLinkServiceImpl(ShortLinkManager shortLinkManager) {
        this.shortLinkManager = shortLinkManager;
    }


    /**
     * 解析短链接代码
     * <p>
     * 本方法通过短链接代码查询对应的短链接信息，如果找到则返回该信息，否则返回null
     * 主要用于处理短链接的解析请求，将短链接代码转换为可访问的短链接信息
     *
     * @param shortLinkCode 短链接代码，用于查询对应的短链接信息
     * @return ShortLinkVO 如果找到对应的短链接信息则返回，否则返回null
     */
    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        // 根据短链接代码查询数据库中的短链接对象
        ShortLink shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        // 如果查询结果为空，则直接返回null
        if (shortLinkDO == null) {
            return null;
        }
        // 创建一个短链接视图对象，用于封装查询到的短链接信息
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        // 将短链接对象的属性复制到视图对象中
        BeanUtils.copyProperties(shortLinkDO, shortLinkVO);
        // 返回填充好的短链接视图对象
        return shortLinkVO;
    }

    /**
     * 创建短链接
     *
     * 此方法接收一个ShortLinkAddRequest对象作为请求参数，用于生成短链接信息它通过RabbitMQ异步处理短链接的创建过程
     *
     * @param request 包含短链接相关信息的请求对象
     * @return 返回一个表示操作结果的JsonData对象
     */
    @Override
    public JsonData createShortLink(ShortLinkAddRequest request) {
        // 获取当前登录用户的账号编号
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        // 构建事件消息对象，用于发送到RabbitMQ
        EventMessage eventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .content(JsonUtil.obj2Json(request))
                .messageId(IDUtil.geneSnowFlakeID().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .build();

        // 将事件消息发送到指定的RabbitMQ交换机和路由键
        rabbitTemplate.convertAndSend(rabbitMQConfig.getShortLinkEventExchange(), rabbitMQConfig.getShortLinkAddRoutingKey(), eventMessage);

        // 返回成功响应数据
        return JsonData.buildSuccess();
    }
}




