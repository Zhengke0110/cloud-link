package fun.timu.cloud.net.app.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class KafkaUtil {


    /**
     * kafka的broker地址
     */
    private static String KAFKA_SERVER = null;

    static {
        Properties properties = new Properties();

        InputStream in = KafkaUtil.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            log.error("加载kafka配置文件失败,{}", e);
        }

        //获取key配置对应的value
        KAFKA_SERVER = properties.getProperty("kafka.servers");

    }


    /**
     * 获取flink的kafka消费者
     *
     * @param topic 主题名，消费者订阅的kafka主题
     * @param groupId 消费者组ID，用于区分不同的消费者组
     * @return 返回配置好的FlinkKafkaConsumer实例，用于从kafka指定主题中消费消息
     */
    public static FlinkKafkaConsumer<String> getKafkaConsumer(String topic, String groupId) {
        // 创建消费者配置属性
        Properties props = new Properties();
        // 设置消费者组ID
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // 设置kafka服务器地址
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        // 返回配置好的Flink Kafka消费者，使用SimpleStringSchema进行消息序列化
        return new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);
    }


    /**
     * 获取flink的kafka生产者
     *
     * 此方法用于创建并返回一个Flink Kafka生产者实例，该实例可用于将数据流写入Kafka主题
     * 它需要一个主题名称作为参数，并返回一个配置好的FlinkKafkaProducer对象
     *
     * @param topic Kafka主题名，用于指定生产者写入的Kafka主题
     * @return 返回一个配置好的FlinkKafkaProducer实例，用于向指定的Kafka主题发送消息
     */
    public static FlinkKafkaProducer<String> getKafkaProducer(String topic) {
        // 创建并返回一个FlinkKafkaProducer实例，指定Kafka服务器地址、主题名和序列化方式
        return new FlinkKafkaProducer<String>(KAFKA_SERVER, topic, new SimpleStringSchema());
    }

}
