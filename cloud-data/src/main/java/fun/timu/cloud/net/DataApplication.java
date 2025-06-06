package fun.timu.cloud.net;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("fun.timu.cloud.net.data.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class DataApplication {


    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }

}
