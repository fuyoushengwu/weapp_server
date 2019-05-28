package cn.aijiamuyingfang.server.it.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.context.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * [描述]:
 * <p>
 * 切换动态数据源的切面
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-14 01:24:17
 */
@Aspect
@Order(-1)
@Component
@Slf4j
public class DynamicDataSourceAspect {

  public DynamicDataSourceAspect() {
    System.out.println("DynamicDataSourceAspect");
  }

  @Before("@annotation(targetDataSource)")
  public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
    String datasourceName = targetDataSource.name();
    if (!DynamicDataSourceContextHolder.containsDataSource(datasourceName)) {
      log.error("数据源[{}]不存在，使用默认数据源 > {}", targetDataSource.name(), point.getSignature());
    } else {
      log.debug("Use DataSource : {} > {}", targetDataSource.name(), point.getSignature());
      DynamicDataSourceContextHolder.setDataSourceType(targetDataSource.name());
    }
  }

  @After("@annotation(targetDataSource)")
  public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
    log.debug("Revert DataSource : {} > {}", targetDataSource.name(), point.getSignature());
    DynamicDataSourceContextHolder.clearDataSourceType();
  }
}
