package cn.aijiamuyingfang.commons.annotation;

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
public @interface UseCaseDescription {
  /**
   * 
   * @return 测试用例预期
   */
  String expected() default "";

  /**
   * 
   * @return 测试用例条件
   */
  String condition() default "";

  /**
   * 
   * @return 测试用例描述
   */
  String description() default "";
}