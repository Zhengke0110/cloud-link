package fun.timu.cloud.net.app.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.model.DO.ShortLinkWide;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.flink.configuration.Configuration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

@Slf4j
public class LocationMapFunction extends RichMapFunction<ShortLinkWide, String> {
    private static final String IP_PARSE_URL = "https://restapi.amap.com/v3/ip?ip=%s&output=json&key=cd38725b30a91567d8da6f3ae65c747d";

    private CloseableHttpClient httpClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        this.httpClient = createHttpClient();
    }

    @Override
    public void close() throws Exception {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    /**
     * 处理ShortLinkWide对象
     * 该方法主要通过IP地址解析地理位置信息，并将其设置到输入对象中
     *
     * @param value ShortLinkWide对象，包含IP地址信息
     * @return 返回更新了地理位置信息的ShortLinkWide对象的JSON字符串表示
     * @throws Exception 如果在执行HTTP请求或解析JSON时发生错误，则抛出异常
     */
    @Override
    public String map(ShortLinkWide value) throws Exception {
        // 获取IP地址
        String ip = value.getIp();

        // 根据IP地址构造地理位置解析的URL
        String url = String.format(IP_PARSE_URL, ip);

        // 创建HTTP GET请求
        HttpGet httpGet = new HttpGet(url);

        // 执行HTTP请求并处理响应
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 检查HTTP响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 如果响应成功，获取并解析响应实体
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                JSONObject locationObj = JSON.parseObject(result);

                // 从解析结果中提取省份和城市信息
                String province = locationObj.getString("province");
                String city = locationObj.getString("city");
                // 将省份和城市信息设置到输入对象中
                value.setProvince(province);
                value.setCity(city);
            }
        } catch (Exception e) {
            // 如果发生异常，记录错误日志
            log.error("ip解析错误,value={},msg={}", value, e.getMessage());
        }

        // 返回更新后的对象的JSON字符串表示
        return JSON.toJSONString(value);
    }


    /**
     * 创建HttpClient实例
     * <p>
     * 该方法用于配置并创建一个CloseableHttpClient实例，用于发送HTTP请求。
     * 它通过自定义连接管理器和请求配置来优化HttpClient的性能和可靠性。
     *
     * @return CloseableHttpClient 实例，用于执行HTTP请求
     */
    private CloseableHttpClient createHttpClient() {

        // 创建一个连接工厂注册表，用于处理HTTP和HTTPS连接
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();

        // 创建一个连接池管理器，用于管理HTTP连接
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

        // 设置每个主机的最大并发连接数为300
        connectionManager.setDefaultMaxPerRoute(300);
        // 设置连接池的最大连接数为500
        connectionManager.setMaxTotal(500);

        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置读取数据的超时时间为20秒
                .setSocketTimeout(20000)
                // 设置连接服务器的超时时间为10秒
                .setConnectTimeout(10000)
                // 设置从连接池获取连接的超时时间为1秒
                .setConnectionRequestTimeout(1000).build();

        // 创建HttpClient实例
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();

        // 返回HttpClient实例
        return closeableHttpClient;
    }


}
