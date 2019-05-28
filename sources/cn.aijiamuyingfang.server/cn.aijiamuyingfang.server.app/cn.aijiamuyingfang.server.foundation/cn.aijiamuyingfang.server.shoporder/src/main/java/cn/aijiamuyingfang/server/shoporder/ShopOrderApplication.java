package cn.aijiamuyingfang.server.shoporder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
@EnableFeignClients(basePackages = "cn.aijiamuyingfang.server")
@EnableEurekaClient
public class ShopOrderApplication {
  public static void main(String[] args) {
    SpringApplication.run(ShopOrderApplication.class, args);
  }
}