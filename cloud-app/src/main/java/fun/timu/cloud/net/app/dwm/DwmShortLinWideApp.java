package fun.timu.cloud.net.app.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import fun.timu.cloud.net.app.func.AsyncLocationRequestFunction;
import fun.timu.cloud.net.app.func.DeviceMapFunction;
import fun.timu.cloud.net.app.model.DO.ShortLinkWide;
import fun.timu.cloud.net.app.util.KafkaUtil;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.concurrent.TimeUnit;


import java.util.concurrent.TimeUnit;

public class DwmShortLinWideApp {
    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_short_link_group";


    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_link_visit_topic";

    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度为1
//        env.setParallelism(1);

        // 从Kafka中获取数据流
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        // 对获取到的数据流进行格式转换，以补全设备信息
        SingleOutputStreamOperator<ShortLinkWide> deviceWideDS = ds.map(new DeviceMapFunction());
        deviceWideDS.print("设备信息宽表补齐");

        // 补充地理位置信息
        SingleOutputStreamOperator<String> shortLinkWideDS = AsyncDataStream.unorderedWait(deviceWideDS, new AsyncLocationRequestFunction(), 1000, TimeUnit.MILLISECONDS, 200);
        shortLinkWideDS.print("地理位置信息宽表补齐");

        // 将处理后的数据流写入Kafka
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);
        shortLinkWideDS.addSink(kafkaProducer);

        // 执行环境
        env.execute();
    }
}
