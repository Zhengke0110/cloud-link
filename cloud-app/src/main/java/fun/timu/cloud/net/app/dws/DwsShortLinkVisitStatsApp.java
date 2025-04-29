package fun.timu.cloud.net.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import fun.timu.cloud.net.app.model.DO.ShortLinkVisitStats;
import fun.timu.cloud.net.app.util.KafkaUtil;
import fun.timu.cloud.net.app.util.TimeUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class DwsShortLinkVisitStatsApp {

    /**
     * 宽表
     */
    public static final String SHORT_LINK_SOURCE_TOPIC = "dwm_link_visit_topic";

    public static final String SHORT_LINK_SOURCE_GROUP = "dws_link_visit_group";

    /**
     * uv的数据流
     */
    public static final String UNIQUE_VISITOR_SOURCE_TOPIC = "dwm_unique_visitor_topic";

    public static final String UNIQUE_VISITOR_SOURCE_GROUP = "dws_unique_visitor_group";


    public static void main(String[] args) throws Exception {
        // 设置执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为1，确保数据处理的顺序性
        env.setParallelism(1);

        //1、获取多个数据源
        // 从Kafka中获取短链数据源
        FlinkKafkaConsumer<String> shortLinkSource = KafkaUtil.getKafkaConsumer(SHORT_LINK_SOURCE_TOPIC, SHORT_LINK_SOURCE_GROUP);
        DataStreamSource<String> shortLinkDS = env.addSource(shortLinkSource);
        // 从Kafka中获取唯一访问者数据源
        FlinkKafkaConsumer<String> uniqueVisitorSource = KafkaUtil.getKafkaConsumer(UNIQUE_VISITOR_SOURCE_TOPIC, UNIQUE_VISITOR_SOURCE_GROUP);
        DataStreamSource<String> uniqueVisitorDS = env.addSource(uniqueVisitorSource);

        //2、结构转换 uniqueVisitorDS、shortLinkDS
        // 将短链数据源转换为访问统计对象，设置PV为1，UV为0
        SingleOutputStreamOperator<ShortLinkVisitStats> shortLinkMapDS = shortLinkDS.map(new MapFunction<String, ShortLinkVisitStats>() {
            @Override
            public ShortLinkVisitStats map(String value) throws Exception {
                ShortLinkVisitStats visitStatsDO = parseVisitStats(value);
                visitStatsDO.setPv(1L);
                visitStatsDO.setUv(0L);
                return visitStatsDO;
            }
        });
        // 将唯一访问者数据源转换为访问统计对象，设置PV为0，UV为1
        SingleOutputStreamOperator<ShortLinkVisitStats> uniqueVisitorMapDS = uniqueVisitorDS.map(new MapFunction<String, ShortLinkVisitStats>() {
            @Override
            public ShortLinkVisitStats map(String value) throws Exception {
                ShortLinkVisitStats visitStatsDO = parseVisitStats(value);
                visitStatsDO.setPv(0L);
                visitStatsDO.setUv(1L);
                return visitStatsDO;
            }
        });

        //3、多流合并（合并相同结构的流）
        // 合并短链数据流和唯一访问者数据流
        DataStream<ShortLinkVisitStats> unionDS = shortLinkMapDS.union(uniqueVisitorMapDS);

        //4、设置WaterMark
        // 为合并后的数据流设置水印，允许最大3秒的乱序，并指定事件时间列
        SingleOutputStreamOperator<ShortLinkVisitStats> watermarkDS = unionDS.assignTimestampsAndWatermarks(WatermarkStrategy
                .<ShortLinkVisitStats>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                .withTimestampAssigner((event, timestamp) -> event.getVisitTime()));

        //5、多维度、多个字段分组
        // 按照code、referer、isNew、province、city、ip、browserName、os、deviceType进行分组
        KeyedStream<ShortLinkVisitStats, Tuple9<String, String, Integer, String, String, String, String, String, String>> keyedStream = watermarkDS.keyBy(new KeySelector<ShortLinkVisitStats, Tuple9<String, String, Integer, String, String, String, String, String, String>>() {
            @Override
            public Tuple9<String, String, Integer, String, String, String, String, String, String> getKey(ShortLinkVisitStats obj) throws Exception {
                return Tuple9.of(obj.getCode(), obj.getReferer(), obj.getIsNew(),
                        obj.getProvince(), obj.getCity(), obj.getIp(),
                        obj.getBrowserName(), obj.getOs(), obj.getDeviceType());
            }
        });

        //6、开窗 10秒一次数据插入到 ck
        // 对分组后的数据流进行窗口操作，每10秒一个窗口
        WindowedStream<ShortLinkVisitStats, Tuple9<String, String, Integer, String, String, String, String, String, String>, TimeWindow> windowedStream =
                keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        //7、聚合统计(补充统计起止时间)
        // 在窗口中对数据进行聚合统计，并补充统计起止时间
        SingleOutputStreamOperator<Object> reduceDS = windowedStream.reduce(new ReduceFunction<ShortLinkVisitStats>() {
            @Override
            public ShortLinkVisitStats reduce(ShortLinkVisitStats value1, ShortLinkVisitStats value2) throws Exception {
                value1.setPv(value1.getPv() + value2.getPv());
                value1.setUv(value1.getUv() + value2.getUv());
                return value1;
            }
        }, new ProcessWindowFunction<ShortLinkVisitStats, Object, Tuple9<String, String, Integer, String, String, String, String, String, String>, TimeWindow>() {
            @Override
            public void process(Tuple9<String, String, Integer, String, String, String, String, String, String> tuple,
                                Context context, Iterable<ShortLinkVisitStats> elements, Collector<Object> out) throws Exception {
                for (ShortLinkVisitStats visitStatsDO : elements) {
                    // 窗口开始和结束时间
                    String startTime = TimeUtil.formatWithTime(context.window().getStart());
                    String endTime = TimeUtil.formatWithTime(context.window().getEnd());
                    visitStatsDO.setStartTime(startTime);
                    visitStatsDO.setEndTime(endTime);
                    out.collect(visitStatsDO);
                }
            }
        });

        // 打印结果到控制台
        reduceDS.print(">>>>>>");

        //8、输出Clickhouse
        // 执行环境，开始执行数据处理流程
        env.execute();
    }


    /**
     * 解析访问统计信息
     * 该方法将给定的字符串值解析为ShortLinkVisitStats对象
     * 字符串应为JSON格式，包含短链接的访问统计信息
     *
     * @param value 包含访问统计信息的JSON字符串
     * @return 解析后的ShortLinkVisitStats对象
     */
    private static ShortLinkVisitStats parseVisitStats(String value) {

        // 将输入的字符串解析为JSONObject
        JSONObject jsonObj = JSON.parseObject(value);

        // 使用builder模式构建ShortLinkVisitStats对象，并设置属性
        ShortLinkVisitStats visitStatsDO = ShortLinkVisitStats.builder()
                .code(jsonObj.getString("code"))
                .accountNo(jsonObj.getLong("accountNo"))
                .visitTime(jsonObj.getLong("visitTime"))
                .referer(jsonObj.getString("referer"))
                .isNew(jsonObj.getInteger("isNew"))
                .udid(jsonObj.getString("udid"))

                // 设置地理位置信息
                .province(jsonObj.getString("province"))
                .city(jsonObj.getString("city"))
                .isp(jsonObj.getString("isp"))
                .ip(jsonObj.getString("ip"))

                // 设置设备信息
                .browserName(jsonObj.getString("browserName"))
                .os(jsonObj.getString("os"))
                .osVersion(jsonObj.getString("osVersion"))
                .deviceType(jsonObj.getString("deviceType"))

                .build();

        // 返回构建好的ShortLinkVisitStats对象
        return visitStatsDO;
    }
}
