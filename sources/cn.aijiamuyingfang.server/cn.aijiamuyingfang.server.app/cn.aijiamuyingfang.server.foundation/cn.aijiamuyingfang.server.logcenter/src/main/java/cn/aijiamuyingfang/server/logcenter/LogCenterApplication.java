package cn.aijiamuyingfang.server.logcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
public class LogCenterApplication {
  public static void main(String[] args) {
    SpringApplication.run(LogCenterApplication.class, args);
  }

}
