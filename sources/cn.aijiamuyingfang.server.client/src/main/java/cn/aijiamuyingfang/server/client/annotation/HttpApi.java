package cn.aijiamuyingfang.server.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [描述]:
 * <p>
 * 用于标识服务接口的注解
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 20:20:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpApi {
  /**
   * 提供服务的baseurl
   * 
   * @return
   */
  String baseurl() default "localhost:8080";

  /**
   * 服务拦截器类
   * 
   * @return
   */
  Class<?>[] interceptor() default {};

}