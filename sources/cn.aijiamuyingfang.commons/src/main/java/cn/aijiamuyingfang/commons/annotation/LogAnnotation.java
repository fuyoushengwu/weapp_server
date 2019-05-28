package cn.aijiamuyingfang.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [描述]:
 * <p>
 * 日志注解
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-03 17:18:55
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {
  String module();

  /**
   * 记录参数<br>
   * 尽量记录普通参数类型的方法，和能序列化的对象
   * 
   * @return
   */
  boolean recordParam() default true;
}
