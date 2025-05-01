package fun.timu.cloud.net.app.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fun.timu.cloud.net.app.model.DO.ShortLinkWide;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class AsyncLocationRequestFunction extends RichAsyncFunction<ShortLinkWide, String> {

    private static final String IP_PARSE_URL = "https://restapi.amap.com/v3/ip?ip=%s&output=json&key=cd38725b30a91567d8da6f3ae65c747d";


    private CloseableHttpAsyncClient httpAsyncClient;

    @Override
    public void open(Configuration parameters) throws Exception {

        this.httpAsyncClient = createAsyncHttpClient();
    }

    @Override
    public void close() throws Exception {
        if (httpAsyncClient != null) {
            httpAsyncClient.close();
        }
    }

    /**
     * 异步调用方法，根据短链信息中的IP地址查询地理位置信息
     *
     * @param shortLinkWideDO 短链信息对象，包含IP地址等信息
     * @param resultFuture    用于返回处理结果的未来对象
     * @throws Exception 如果在执行过程中遇到异常则抛出
     */
    @Override
    public void asyncInvoke(ShortLinkWide shortLinkWideDO, ResultFuture<String> resultFuture) throws Exception {
        // 根据IP地址构造查询URL
        String ip = shortLinkWideDO.getIp();
        String url = String.format(IP_PARSE_URL, ip);
        HttpGet httpGet = new HttpGet(url);

        // 异步执行HTTP请求
        Future<HttpResponse> future = httpAsyncClient.execute(httpGet, null);

        // 使用CompletableFuture进行异步处理
        CompletableFuture<ShortLinkWide> completableFuture = CompletableFuture.supplyAsync(new Supplier<ShortLinkWide>() {
            @Override
            public ShortLinkWide get() {
                try {
                    // 获取HTTP响应并解析地理位置信息
                    HttpResponse response = future.get();
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity, "UTF-8");
                        JSONObject locationObj = JSON.parseObject(result);
                        String city = locationObj.getString("city");
                        String province = locationObj.getString("province");

                        // 将解析结果设置到短链信息对象中
                        shortLinkWideDO.setProvince(province);
                        shortLinkWideDO.setCity(city);
                        return shortLinkWideDO;
                    }
                } catch (InterruptedException | ExecutionException | IOException e) {
                    // 日志记录IP解析错误信息
                    log.error("ip解析错误,value={},msg={}", shortLinkWideDO, e.getMessage());
                }
                shortLinkWideDO.setProvince("-");
                shortLinkWideDO.setCity("-");
                return shortLinkWideDO;
            }
        });

        // 处理完成后，使用结果对象完成处理结果
        completableFuture.thenAccept(new Consumer<ShortLinkWide>() {
            @Override
            public void accept(ShortLinkWide shortLinkWideDO) {
                resultFuture.complete(Collections.singleton(JSON.toJSONString(shortLinkWideDO)));
            }
        });
    }


    /**
     * 创建异步HTTP客户端
     * <p>
     * 本方法旨在构建一个配置化的CloseableHttpAsyncClient实例，用于异步HTTP请求
     * 它配置了请求的超时参数和连接池参数，以优化网络请求的性能和资源管理
     *
     * @return CloseableHttpAsyncClient 实例化的异步HTTP客户端，如果初始化失败则返回null
     */
    private CloseableHttpAsyncClient createAsyncHttpClient() {
        try {
            //构建请求配置
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置返回数据的超时时间
                    .setSocketTimeout(20000)
                    //设置连接上服务器的超时时间
                    .setConnectTimeout(10000)
                    //设置从连接池中获取连接的超时时间
                    .setConnectionRequestTimeout(1000)
                    .build();

            //初始化IO反应器，用于处理I/O操作
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();

            //创建连接管理器，用于管理HTTP客户端连接
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            //设置连接池最大是500个连接
            connManager.setMaxTotal(500);
            //设置每个主机的最大并发连接数是300
            connManager.setDefaultMaxPerRoute(300);

            //构建并配置HTTP异步客户端
            CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            //启动HTTP客户端
            httpClient.start();
            //返回构建好的HTTP客户端实例
            return httpClient;

        } catch (IOReactorException e) {
            //记录初始化HTTP客户端时的异常信息
            log.error("初始化 CloseableHttpAsyncClient异常:{}", e.getMessage());
            //异常情况下返回null
            return null;
        }

    }
}
