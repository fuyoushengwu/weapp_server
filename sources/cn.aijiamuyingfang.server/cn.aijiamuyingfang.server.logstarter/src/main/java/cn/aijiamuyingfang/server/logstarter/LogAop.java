package cn.aijiamuyingfang.server.logstarter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import cn.aijiamuyingfang.commons.annotation.LogAnnotation;
import cn.aijiamuyingfang.commons.constants.LogConstants;
import cn.aijiamuyingfang.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.logstarter.domain.Log;
import cn.aijiamuyingfang.server.logstarter.domain.User;
import cn.aijiamuyingfang.server.logstarter.utils.UserUtils;

/**
 * [描述]:
 * <p>
 * AOP实现日志
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-04 01:35:33
 */
public class LogAop {
  private static final Logger logger = LoggerFactory.getLogger(LogAop.class);

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Around("@annotation(cn.aijiamuyingfang.commons.annotation.LogAnnotation)")
  public Object logSave(ProceedingJoinPoint joinPont) throws Throwable {
    Log log = new Log();
    log.setCreateTime(new Date());
    User currentUser = UserUtils.currentUser();
    if (currentUser != null) {
      log.setUsername(currentUser.getUsername());
    }

    MethodSignature methodSignature = (MethodSignature) joinPont.getSignature();
    LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
    log.setModule(logAnnotation.module());

    if (logAnnotation.recordParam()) {
      String[] paramNames = methodSignature.getParameterNames();
      if (paramNames != null && paramNames.length != 0) {
        Object[] args = joinPont.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
          params.put(paramNames[i], args[i]);
        }

        try {
          log.setParams(JsonUtils.toJson(params));
        } catch (Exception e) {
          logger.error("记录参数失败:{}", e.getMessage());
        }
      }
    }
    try {
      Object object = joinPont.proceed();
      log.setFlag(Boolean.TRUE);
      return object;
    } catch (Exception e) {
      log.setFlag(Boolean.FALSE);
      log.setRemark(e.getMessage());
      throw e;
    } finally {
      CompletableFuture.runAsync(() -> {
        amqpTemplate.convertAndSend(LogConstants.LOG_QUEUE, log);
        logger.info("发送日志到队列:{}", log);
      });
    }
  }

}
