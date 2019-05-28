package cn.aijiamuyingfang.server.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aijiamuyingfang.commons.constants.LogConstants;

/**
 * [描述]:
 * <p>
 * rabbitmq配置
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 18:24:13
 */
@Configuration
public class RabbitmqConfig {
  @Bean
  public Queue logQueue() {
    return new Queue(LogConstants.LOG_QUEUE);
  }
}
