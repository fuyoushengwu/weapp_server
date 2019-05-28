package cn.aijiamuyingfang.server.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
@EnableFeignClients(basePackages = "cn.aijiamuyingfang.server")
@EnableEurekaClient
public class AuthApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }
}
