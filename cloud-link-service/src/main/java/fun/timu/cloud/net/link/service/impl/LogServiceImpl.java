package fun.timu.cloud.net.link.service.impl;

import fun.timu.cloud.net.common.enums.LogTypeEnum;
import fun.timu.cloud.net.common.model.LogRecord;
import fun.timu.cloud.net.common.util.CommonUtil;
import fun.timu.cloud.net.common.util.JsonUtil;
import fun.timu.cloud.net.link.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    private static Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final String TOPIC_NAME = "ods_link_visit_topic";
    private final KafkaTemplate kafkaTemplate;

    public LogServiceImpl(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void recordShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo) {
        //ip、浏览器信息
        String ip = CommonUtil.getIpAddr(request);

        //全部请求头
        Map<String, String> headerMap = CommonUtil.getAllRequestHeader(request);


        Map<String, String> availableMap = new HashMap<>();
        availableMap.put("user-agent", headerMap.get("user-agent"));
        availableMap.put("referer", headerMap.get("referer"));
        availableMap.put("accountNo", accountNo.toString());

        LogRecord logRecord = LogRecord.builder()
                //日志类型
                .event(LogTypeEnum.SHORT_LINK_TYPE.name())
                //日志内容
                .data(availableMap)
                //客户端ip
                .ip(ip)
                //产生时间
                .ts(CommonUtil.getCurrentTimestamp())
                //业务唯一标识
                .bizId(shortLinkCode).build();


        String jsonLog = JsonUtil.obj2Json(logRecord);

        //打印控制台
        logger.info(jsonLog);

        //发送kafka
        kafkaTemplate.send(TOPIC_NAME, jsonLog);
    }
}
