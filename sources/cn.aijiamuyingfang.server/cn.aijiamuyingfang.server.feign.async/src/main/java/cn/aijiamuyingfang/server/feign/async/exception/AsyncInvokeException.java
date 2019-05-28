package cn.aijiamuyingfang.server.feign.async.exception;

import java.lang.reflect.Method;

import cn.aijiamuyingfang.commons.utils.ReflectUtils;

/**
 * [描述]:
 * <p>
 * 异步调用失败
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 20:17:49
 */
public class AsyncInvokeException extends RuntimeException {
  private static final long serialVersionUID = 7608324398059424193L;

  private Method method;

  public AsyncInvokeException(Method method) {
    super();
    this.method = method;
  }

  public AsyncInvokeException(Method method, Throwable throwable) {
    super(throwable);
    this.method = method;
  }

  public AsyncInvokeException(Method method, String message, Throwable throwable) {
    super(message, throwable);
    this.method = method;
  }

  public Method method() {
    return method;
  }

  @Override
  public String getMessage() {
    String methodSiguature = "";
    if (method != null) {
      methodSiguature = ReflectUtils.getSignature(method);
    }
    return String.format("invoke method[%s] failed,cause:%s", methodSiguature, super.getMessage());
  }
}
