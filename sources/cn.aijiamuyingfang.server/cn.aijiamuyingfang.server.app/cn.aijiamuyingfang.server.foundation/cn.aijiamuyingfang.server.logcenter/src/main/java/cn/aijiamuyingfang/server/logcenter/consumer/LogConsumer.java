package cn.aijiamuyingfang.server.logcenter.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.aijiamuyingfang.commons.constants.LogConstants;
import cn.aijiamuyingfang.server.logcenter.dto.Log;
import cn.aijiamuyingfang.server.logcenter.service.LogService;
import lombok.extern.slf4j.Slf4j;

/**
 * [描述]:
 * <p>
 * Log信息消费者
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 18:13:28
 */
@Component
@Slf4j
@RabbitListener(queues = LogConstants.LOG_QUEUE)
public class LogConsumer {
  @Autowired
  private LogService logService;

  @RabbitHandler
  public void logHandler(Log log) {
    try {
      logService.save(log);
    } catch (Exception e) {
      LogConsumer.log.error("save log failed,log entity:{},cause:{}", log, e);
    }
  }
}
