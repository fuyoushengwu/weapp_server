package cn.aijiamuyingfang.server.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * [描述]:
 * <p>
 * Goods模块的启动程序
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 19:06:46
 */
@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
@EnableFeignClients(basePackages = "cn.aijiamuyingfang.server")
@EnableEurekaClient
public class GoodsApplication {
  public static void main(String[] args) {
    SpringApplication.run(GoodsApplication.class, args);
  }

}
