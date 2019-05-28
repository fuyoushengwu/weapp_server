package cn.aijiamuyingfang.server.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [描述]:
 * <p>
 * 微服务注册中心
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-08 22:23:09
 */
@EnableEurekaServer
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
public class RegisterApplication {
  public static void main(String[] args) {
    SpringApplication.run(RegisterApplication.class, args);
  }
}
