package cn.aijiamuyingfang.commons.utils;

import java.lang.reflect.Method;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * 反射工具
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-21 00:29:47
 */
@UtilityClass
public class ReflectUtils {

  /**
   * 方法签名
   * 
   * @param method
   * @return
   */
  public static String getSignature(Method method) {
    return getSignature(method, false);
  }

  /**
   * 方法签名
   * 
   * @param method
   * @param longTypeNames
   * @return
   */
  public static String getSignature(Method method, boolean longTypeNames) {
    return method.getName() + "(" + parametersAsString(method, longTypeNames) + ")";
  }

  /**
   * 方法参数签名
   * 
   * @param method
   * @return
   */
  public static String parametersAsString(Method method) {
    return parametersAsString(method, false);
  }

  /**
   * 方法参数签名
   * 
   * @param method
   * @param longTypeNames
   * @return
   */
  public static String parametersAsString(Method method, boolean longTypeNames) {
    Class<?>[] parameterTypes = method.getParameterTypes();
    if (parameterTypes.length == 0)
      return "";
    StringBuilder paramString = new StringBuilder();
    paramString.append(longTypeNames ? parameterTypes[0].getName() : parameterTypes[0].getSimpleName());
    for (int i = 1; i < parameterTypes.length; i++) {
      paramString.append(",").append(longTypeNames ? parameterTypes[i].getName() : parameterTypes[i].getSimpleName());
    }
    return paramString.toString();
  }

}
