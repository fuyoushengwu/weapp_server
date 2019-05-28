package cn.aijiamuyingfang.server.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import cn.aijiamuyingfang.server.it.config.DynamicDataSourceRegister;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang" })
@Import({ DynamicDataSourceRegister.class }) // 注册动态多数据源
public class ITApplication {
  public static void main(String[] args) {
    SpringApplication.run(ITApplication.class, args);
  }
}
