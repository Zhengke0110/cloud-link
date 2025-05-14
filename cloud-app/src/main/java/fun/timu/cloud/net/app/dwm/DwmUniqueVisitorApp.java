package fun.timu.cloud.net.app.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.func.UniqueVisitorFilterFunction;
import fun.timu.cloud.net.app.util.KafkaUtil;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;


public class DwmUniqueVisitorApp {

    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "dwm_link_visit_topic";


    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_unique_visitor_group";


    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_unique_visitor_topic";


    public static void main(String[] args) throws Exception {
        // 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度为1
        env.setParallelism(1);

        // 通过Kafka工具类获取Kafka消费者，指定源主题和消费者组ID
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);

        // 添加Kafka消费者作为数据源
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);

        // 对数据流进行转换，将每条数据解析为JSONObject
        SingleOutputStreamOperator<JSONObject> jsonDS = ds.map(jsonStr -> JSON.parseObject(jsonStr));

        // 对数据进行分组，根据"udid"字段进行键值分组
        KeyedStream<JSONObject, String> keyedStream = jsonDS.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject value) throws Exception {
                return value.getString("udid");
            }
        });

        // 对分组后的数据进行排重过滤
        SingleOutputStreamOperator<JSONObject> filterDS = keyedStream.filter(new UniqueVisitorFilterFunction());

        // 打印排重过滤后的数据
        filterDS.print("独立访客");

        // 将过滤后的数据转换为字符串格式
        SingleOutputStreamOperator<String> uniqueVisitorDS = filterDS.map(obj -> obj.toJSONString());

        // 通过Kafka工具类获取Kafka生产者，指定目标主题
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);

        // 将转换后的数据流添加到Kafka生产者，写入Kafka
        uniqueVisitorDS.addSink(kafkaProducer);

        // 执行环境，开始执行数据流处理
        env.execute();
    }


}
