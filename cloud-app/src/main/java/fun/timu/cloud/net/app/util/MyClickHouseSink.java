package fun.timu.cloud.net.app.util;

import fun.timu.cloud.net.app.model.DO.ShortLinkVisitStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class MyClickHouseSink {
    /**
     * ClickHouse地址
     */
    private static String CLICK_HOUSE_SERVER = null;
    private static String CLICK_HOUSE_USER = null;
    private static String CLICK_HOUSE_PASSWORD = null;

    static {
        Properties properties = new Properties();

        InputStream in = KafkaUtil.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            log.error("加载ClickHouse配置文件失败,{}", e);
        }

        //获取key配置对应的value
        CLICK_HOUSE_SERVER = properties.getProperty("clickhouse.servers");
        CLICK_HOUSE_USER = properties.getProperty("clickhouse.username");
        CLICK_HOUSE_PASSWORD  = properties.getProperty("clickhouse.password");
    }

    /**
     * 获取想ClikcHouseServer写入数据的sinkFunction
     *
     * @param sql 插入数据的SQL语句
     * @return 返回一个SinkFunction对象，用于将数据写入ClickHouseServer
     */
    public static SinkFunction getJdbcSink(String sql) {

        // 创建一个SinkFunction，用于将ShortLinkVisitStats对象的数据写入数据库
        SinkFunction<ShortLinkVisitStats> sinkFunction = JdbcSink.sink(sql, new JdbcStatementBuilder<ShortLinkVisitStats>() {
                    @Override
                    public void accept(PreparedStatement ps, ShortLinkVisitStats obj) throws SQLException {
                        // 设置预编译语句的参数值
                        ps.setObject(1, obj.getCode());
                        ps.setObject(2, obj.getReferer());
                        ps.setObject(3, obj.getIsNew());
                        ps.setObject(4, obj.getAccountNo());
                        ps.setObject(5, obj.getProvince());
                        ps.setObject(6, obj.getCity());
                        ps.setObject(7, obj.getIp());

                        ps.setObject(8, obj.getBrowserName());
                        ps.setObject(9, obj.getOs());
                        ps.setObject(10, obj.getDeviceType());

                        ps.setObject(11, obj.getPv());
                        ps.setObject(12, obj.getUv());
                        ps.setObject(13, obj.getStartTime());
                        ps.setObject(14, obj.getEndTime());
                        ps.setObject(15, obj.getVisitTime());
                    }
                },
                // 控制批量写入大小
                new JdbcExecutionOptions.Builder().withBatchSize(3).build()

                ,
                // 连接配置
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl(CLICK_HOUSE_SERVER)
                        .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                        .withUsername(CLICK_HOUSE_USER)
                        .withPassword(CLICK_HOUSE_PASSWORD)
                        .build());

        return sinkFunction;

    }

}
