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

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

//        DataStream<String> ds = env.socketTextStream("127.0.0.1", 8888);

        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);

        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        ds.print();

        //数据补齐
        SingleOutputStreamOperator<JSONObject> jsonDS = ds.flatMap(new FlatMapFunction<String, JSONObject>() {
            @Override
            public void flatMap(String value, Collector<JSONObject> out) throws Exception {

                JSONObject jsonObject = JSON.parseObject(value);
                //生成设备唯一id
                String udid = getDeviceId(jsonObject);
                jsonObject.put("udid", udid);

                String referer = getReferer(jsonObject);
                jsonObject.put("referer", referer);

                out.collect(jsonObject);
            }
        });

        //分组
        KeyedStream<JSONObject, String> keyedStream = jsonDS.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject value) throws Exception {
                return value.getString("udid");
            }
        });


        //识别 richMap open函数，做状态存储的初始化

        SingleOutputStreamOperator<String> jsonDSWithVisitorState = keyedStream.map(new VistorMapFunction());


        jsonDSWithVisitorState.print("ods新老访客");

        //存储到dwd
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);

        jsonDSWithVisitorState.addSink(kafkaProducer);

        env.execute();

    }


    /**
     * 提取referer
     *
     * @param jsonObject
     * @return
     */
    public static String getReferer(JSONObject jsonObject) {

        JSONObject dataJsonObj = jsonObject.getJSONObject("data");
        if (dataJsonObj.containsKey("referer")) {

            String referer = dataJsonObj.getString("referer");
            if (StringUtils.isNotBlank(referer)) {
                try {
                    URL url = new URL(referer);
                    return url.getHost();
                } catch (MalformedURLException e) {
                    log.error("提取referer失败:{}", e);
                }
            }

        }
        return "";
    }


    /**
     * 生成设备唯一id
     *
     * @param jsonObject
     * @return
     */
    public static String getDeviceId(JSONObject jsonObject) {
        Map<String, String> map = new TreeMap<>();

        try {
            map.put("ip", jsonObject.getString("ip"));
            map.put("event", jsonObject.getString("event"));
            map.put("bizId", jsonObject.getString("bizId"));
            String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
            map.put("userAgent", userAgent);
            String deviceId = DeviceUtil.geneWebUniqueDeviceId(map);
            return deviceId;

        } catch (Exception e) {

            log.error("生成唯一deviceid异常:{}", jsonObject);
            return null;
        }

    }


}
