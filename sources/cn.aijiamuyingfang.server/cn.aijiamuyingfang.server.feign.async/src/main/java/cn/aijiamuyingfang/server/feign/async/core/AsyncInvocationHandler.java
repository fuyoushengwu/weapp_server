package cn.aijiamuyingfang.server.feign.async.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import feign.InvocationHandlerFactory;
import feign.Target;

/**
 * [描述]:
 * <p>
 * 异步方法调用处理器
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-21 00:49:24
 */
public class AsyncInvocationHandler implements InvocationHandler {
  private final Target<?> target;

  private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

  private final AsyncMethodCall asyncMethodCall;

  private final Executor executor;

  public AsyncInvocationHandler(Target<?> target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,
      AsyncMethodCall asyncMethodCall, Executor executor) {
    this.target = target;
    this.dispatch = dispatch;
    this.asyncMethodCall = asyncMethodCall;
    this.executor = executor;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.isDefault()) {
      return this.dispatch.get(method).invoke(args);
    } else if (Future.class.isAssignableFrom(method.getReturnType())) {
      return this.asyncMethodCall.create(this.dispatch, method, args, this.executor);
    } else {
      if (method.getDeclaringClass() == Object.class) {
        if (method.getName().equals("equals")) {
          if (args[0] == null) {
            return Boolean.valueOf(false);
          }
          try {
            InvocationHandler e = Proxy.getInvocationHandler(args[0]);
            if (e.getClass().equals(AsyncInvocationHandler.class)) {
              AsyncInvocationHandler that = (AsyncInvocationHandler) e;
              return Boolean.valueOf(this.target.equals(that.target));
            }
          } catch (IllegalArgumentException var6) {
          }
          return Boolean.valueOf(false);
        }
        if (method.getName().equals("hashCode")) {
          return Integer.valueOf(this.hashCode());
        }
        if (method.getName().equals("toString")) {
          return this.toString();
        }
      }
      return this.dispatch.get(method).invoke(args);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj != null && this.getClass() == obj.getClass()) {
      AsyncInvocationHandler that = (AsyncInvocationHandler) obj;
      return this.target.equals(that.target);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.target.hashCode();
  }

  @Override
  public String toString() {
    return this.target.toString();
  }

}
