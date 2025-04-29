package fun.timu.cloud.net.app.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import fun.timu.cloud.net.app.model.DO.DeviceInfo;
import fun.timu.cloud.net.app.model.DO.ShortLinkWide;
import fun.timu.cloud.net.app.util.DeviceUtil;
import fun.timu.cloud.net.app.util.KafkaUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

public class DwmShortLinWideApp {
    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_short_link_group";

    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度为1
        env.setParallelism(1);

        // 从Kafka中获取数据源
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        // 格式转换，补齐设备信息
        SingleOutputStreamOperator<ShortLinkWide> deviceWideDS = ds.flatMap(new FlatMapFunction<String, ShortLinkWide>() {
            @Override
            public void flatMap(String value, Collector<ShortLinkWide> out) throws Exception {
                // 还原JSON对象
                JSONObject jsonObject = JSON.parseObject(value);
                String userAgent = jsonObject.getJSONObject("data").getString("user-agent");

                // 获取设备信息
                DeviceInfo deviceInfoDO = DeviceUtil.getDeviceInfo(userAgent);
                String udid = jsonObject.getString("udid");
                deviceInfoDO.setUdid(udid);

                // 构建短链基本信息宽表
                ShortLinkWide shortLinkWideDO = ShortLinkWide.builder()
                        // 短链访问基本信息
                        .visitTime(jsonObject.getLong("ts"))
                        .accountNo(jsonObject.getJSONObject("data").getLong("accountNo"))
                        .code(jsonObject.getString("bizId"))
                        .referer(jsonObject.getString("referer"))
                        .isNew(jsonObject.getInteger("is_new"))
                        .ip(jsonObject.getString("ip"))

                        // 设备信息补齐
                        .browserName(deviceInfoDO.getBrowserName())
                        .os(deviceInfoDO.getOs())
                        .osVersion(deviceInfoDO.getOsVersion())
                        .deviceType(deviceInfoDO.getDeviceType())
                        .deviceManufacturer(deviceInfoDO.getDeviceManufacturer())
                        .udid(deviceInfoDO.getUdid())
                        .build();

                out.collect(shortLinkWideDO);
            }
        });

        // 打印设备信息宽表
        deviceWideDS.print("设备信息宽表补齐");

        // 执行环境
        env.execute();
    }
}
