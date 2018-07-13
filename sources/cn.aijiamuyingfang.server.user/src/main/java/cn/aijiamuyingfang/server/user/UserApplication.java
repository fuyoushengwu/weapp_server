package cn.aijiamuyingfang.server.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * [描述]:
 * <p>
 * User模块的启动程序
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:27:08
 */
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
@EnableJpaRepositories(basePackages = { "cn.aijiamuyingfang.server" })
@EntityScan(basePackages = { "cn.aijiamuyingfang.server" })
public class UserApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }
}