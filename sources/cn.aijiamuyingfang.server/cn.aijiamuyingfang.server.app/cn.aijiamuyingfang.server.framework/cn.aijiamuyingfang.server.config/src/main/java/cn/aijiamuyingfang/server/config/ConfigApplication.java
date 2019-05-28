package cn.aijiamuyingfang.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
public class ConfigApplication {
  public static void main(String[] args) {
    SpringApplication.run(ConfigApplication.class, args);
  }
}
