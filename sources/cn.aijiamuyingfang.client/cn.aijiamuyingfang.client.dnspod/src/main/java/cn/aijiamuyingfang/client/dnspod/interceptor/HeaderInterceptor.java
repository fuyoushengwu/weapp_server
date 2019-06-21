package cn.aijiamuyingfang.client.dnspod.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * [描述]:
 * <p>
 * 添加访问DNSPOD必须的Header
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 05:48:46
 */
public class HeaderInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    request = request.newBuilder()
        .removeHeader("User-Agent")
        .removeHeader("Accept")
        .addHeader("User-Agent", "WEAPP/2.0.0(shiweideyouxiang@sina.cn)")
        .addHeader("Accept", "application/json")
        .build();
    return chain.proceed(request);
  }

}
