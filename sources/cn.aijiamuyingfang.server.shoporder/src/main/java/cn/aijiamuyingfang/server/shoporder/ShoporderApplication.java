package cn.aijiamuyingfang.server.shoporder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * [描述]:
 * <p>
 * ShopOrder模块的启动程序
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 19:06:46
 */
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.client", "cn.aijiamuyingfang.server" })
@EnableJpaRepositories(basePackages = { "cn.aijiamuyingfang.server.domain" })
@EntityScan(basePackages = { "cn.aijiamuyingfang.commons", "cn.aijiamuyingfang.server" })
public class ShoporderApplication {
  public static void main(String[] args) {
    SpringApplication.run(ShoporderApplication.class, args);
  }
}
