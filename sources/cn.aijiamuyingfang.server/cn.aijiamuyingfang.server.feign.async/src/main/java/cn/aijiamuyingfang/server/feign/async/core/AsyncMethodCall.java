package cn.aijiamuyingfang.server.feign.async.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import cn.aijiamuyingfang.server.feign.async.exception.AsyncInvokeException;
import feign.InvocationHandlerFactory;

/**
 * [描述]:
 * <p>
 * 异步方法调用
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 11:09:47
 */

public interface AsyncMethodCall {
  Future<Object> create(Map<Method, InvocationHandlerFactory.MethodHandler> dispath, Method method, Object[] args,
      Executor executor);

  public static class Default implements AsyncMethodCall {

    @Override
    public Future<Object> create(Map<Method, InvocationHandlerFactory.MethodHandler> dispath, Method method,
        Object[] args, Executor executor) {
      return CompletableFuture.supplyAsync(() -> {
        try {
          return dispath.get(method).invoke(args);
        } catch (Throwable e) {
          throw new AsyncInvokeException(method, e);
        }
      }, executor);
    }

  }
}
