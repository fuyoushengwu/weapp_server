package cn.aijiamuyingfang.server.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
public class MonitorApplication {
  public static void main(String[] args) {
    SpringApplication.run(MonitorApplication.class, args);
  }
}
