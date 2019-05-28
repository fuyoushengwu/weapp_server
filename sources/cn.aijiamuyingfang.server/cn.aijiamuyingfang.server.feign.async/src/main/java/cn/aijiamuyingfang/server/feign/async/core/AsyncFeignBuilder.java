package cn.aijiamuyingfang.server.feign.async.core;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import feign.Feign;

/**
 * [描述]:
 * <p>
 * 支持异步的Feign
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 20:05:44
 */
public class AsyncFeignBuilder extends Feign.Builder {

  private Executor executor = ForkJoinPool.commonPool();

  @Override
  public Feign build() {
    AsyncMethodCall asyncMethodCall = new AsyncMethodCall.Default();
    super.invocationHandlerFactory(
        (target, dispatch) -> new AsyncInvocationHandler(target, dispatch, asyncMethodCall, this.executor));
    return super.build();
  }

}
