package cn.aijiamuyingfang.client.rest.bean;

import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_CONNECT_TIMEOUT;
import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_READ_TIMEOUT;
import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_WRITE_TIMEOUT;

import java.util.HashMap;
import java.util.Map;

import cn.aijiamuyingfang.client.commons.utils.RetrofitUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * [描述]:
 * <p>
 * HttpService注解的属性实例化工程
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 21:09:49
 */
public class HttpServiceBeanFactory {

  // key:请求地址 value:当前请求地址下class所对应的service（key:class value:service）
  private static final Map<String, HttpServiceBean> serviceBeans = new HashMap<>();

  private HttpServiceBeanFactory() {
  }

  /**
   * 创建service服务实体
   * 
   * @param baseurl
   * @param serviceClass
   * @param interceptorClasses
   * @return
   */
  public static Object putBean(String baseurl, Class<?> serviceClass, Class<?>... interceptorClasses) {
    if (StringUtils.isEmpty(baseurl)) {
      return null;
    }
    HttpServiceBean httpServiceBean = serviceBeans.get(baseurl);
    if (null == httpServiceBean) {
      httpServiceBean = new HttpServiceBean();
      Retrofit retrofit = RetrofitUtils.getRetrofitBuilder(baseurl).client(getOkHttpClient(interceptorClasses)).build();
      httpServiceBean.setRetrofit(retrofit);
      serviceBeans.put(baseurl, httpServiceBean);
    }

    Retrofit retrofit = httpServiceBean.getRetrofit();

    Object service = retrofit.create(serviceClass);
    httpServiceBean.getServices().put(serviceClass, service);
    return service;
  }

  /**
   * 获取OkHttpClient
   * 
   * @param baseurl
   * @param interceptorClasses
   * @return
   */
  private static OkHttpClient getOkHttpClient(Class<?>... interceptorClasses) {
    OkHttpClient.Builder clientBuilder = RetrofitUtils.getOkHttpClientBuilder(DEFAULT_CONNECT_TIMEOUT,
        DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT);

    // 添加服务拦截器实例
    for (Class<?> interceptorClass : interceptorClasses) {
      if (Interceptor.class.isAssignableFrom(interceptorClass)) {
        try {
          clientBuilder.addInterceptor((Interceptor) interceptorClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
          //异常不需要处理
        }
      }
    }

    return clientBuilder.build();
  }
}
