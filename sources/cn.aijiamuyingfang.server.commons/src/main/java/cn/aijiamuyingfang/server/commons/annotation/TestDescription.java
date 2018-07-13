package cn.aijiamuyingfang.server.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [描述]:
 * <p>
 * 测试用例描述
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-29 11:29:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface TestDescription {
  /**
   * 测试用例预期
   * 
   * @return
   */
  String expected() default "";

  /**
   * 测试用例条件
   * 
   * @return
   */
  String condition() default "";

  /**
   * 测试用例描述
   * 
   * @return
   */
  String description() default "";
}
