package fun.timu.cloud.net.app.dwd;

import fun.timu.cloud.net.app.func.VistorMapFunction;
import fun.timu.cloud.net.app.util.DeviceUtil;
import fun.timu.cloud.net.app.util.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class DwdShortLinkLogApp {

    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "ods_link_visit_topic";

    /**
     * 定义sink topic
     */
    public static final String SINK_TOPIC = "dwd_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwd_short_link_group";


    /**
     * 提取referer
     * <p>
     * 该方法旨在从给定的JSON对象中提取出referer信息，并返回其主机名部分
     * 如果referer信息不存在或者提取过程中发生错误，则返回空字符串
     *
     * @param jsonObject 包含referer信息的JSON对象
     * @return referer的主机名部分，如果提取失败则返回空字符串
     */
    public static String getReferer(JSONObject jsonObject) {

        // 获取"data"键下的JSON对象
        JSONObject dataJsonObj = jsonObject.getJSONObject("data");
        // 检查"data"对象中是否包含"referer"键
        if (dataJsonObj.containsKey("referer")) {

            // 提取"referer"值为字符串
            String referer = dataJsonObj.getString("referer");
            // 检查referer字符串是否非空且非空白
            if (StringUtils.isNotBlank(referer)) {
                try {
                    // 将referer字符串转换为URL对象，以便提取主机名
                    URL url = new URL(referer);
                    // 返回referer的主机名部分
                    return url.getHost();
                } catch (MalformedURLException e) {
                    // 捕获并记录MalformedURLException异常，表示referer不是有效的URL格式
                    log.error("提取referer失败:{}", e);
                }
            }

        }
        // 如果referer信息不存在或者提取过程中发生错误，返回空字符串
        return "";
    }


    /**
     * 生成设备唯一id
     *
     * @param jsonObject 包含设备信息的JSON对象
     * @return 设备唯一id，如果生成失败则返回null
     */
    public static String getDeviceId(JSONObject jsonObject) {
        // 使用TreeMap以确保键值对按字母顺序排序
        Map<String, String> map = new TreeMap<>();

        try {
            // 从JSON对象中提取必要的信息并放入map中
            map.put("ip", jsonObject.getString("ip"));
            map.put("event", jsonObject.getString("event"));
            map.put("bizId", jsonObject.getString("bizId"));
            // 提取user-agent信息
            String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
            map.put("userAgent", userAgent);
            // 调用工具类方法生成唯一设备id
            String deviceId = DeviceUtil.geneWebUniqueDeviceId(map);
            return deviceId;

        } catch (Exception e) {
            // 日志记录异常信息
            log.error("生成唯一deviceid异常:{}", jsonObject);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度为1
        env.setParallelism(1);

        // 从socket获取数据流
//        DataStream<String> ds = env.socketTextStream("127.0.0.1", 8888);

        // 以下代码被注释掉，用于从Kafka获取数据流
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        // 打印原始数据流
        ds.print();

        // 数据补齐：对数据流进行处理，为每个事件添加缺失的字段
        SingleOutputStreamOperator<JSONObject> jsonDS = ds.flatMap(new FlatMapFunction<String, JSONObject>() {
            @Override
            public void flatMap(String value, Collector<JSONObject> out) throws Exception {
                // 解析JSON对象
                JSONObject jsonObject = JSON.parseObject(value);
                // 生成设备唯一id
                String udid = getDeviceId(jsonObject);
                jsonObject.put("udid", udid);

                // 获取并设置referer
                String referer = getReferer(jsonObject);
                jsonObject.put("referer", referer);

                // 输出处理后的JSON对象
                out.collect(jsonObject);
            }
        });

        // 分组：根据udid对数据流进行分组
        KeyedStream<JSONObject, String> keyedStream = jsonDS.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject value) throws Exception {
                return value.getString("udid");
            }
        });

        // 识别 richMap open函数，做状态存储的初始化
        SingleOutputStreamOperator<String> jsonDSWithVisitorState = keyedStream.map(new VistorMapFunction());

        // 打印处理后的数据流
        jsonDSWithVisitorState.print("ods新老访客");

        // 存储到dwd：将处理后的数据流存储到Kafka
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);
        jsonDSWithVisitorState.addSink(kafkaProducer);

        // 执行环境
        env.execute();
    }


}
