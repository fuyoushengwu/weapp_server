package cn.aijiamuyingfang.server.client.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
	private static final Logger LOGGER = LogManager.getLogger(HttpServiceBeanFactory.class);

	// key:请求地址 value:当前请求地址下class所对应的service（key:class value:service）
	private static final Map<String, HttpServiceBean> serviceBeans = new HashMap<>();

	/**
	 * 读超时时间
	 */
	private static final int READ_TIMEOUT = 15;

	/**
	 * 写超时时间
	 */
	private static final int WRITE_TIMEOUT = 15;

	/**
	 * 连接超时时间
	 */
	private static final int CONNECT_TIMEOUT = 15;

	private HttpServiceBeanFactory() {
	}

	/**
	 * 获得service服务实体
	 * 
	 * @param requiredType
	 * @return
	 */
	public static Object getBean(Class<?> requiredType) {
		if (null == requiredType) {
			return null;
		}
		for (HttpServiceBean serviceBean : serviceBeans.values()) {
			for (Map.Entry<Class<?>, Object> entry : serviceBean.getServices().entrySet()) {
				if (requiredType.equals(entry.getKey())) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 创建service服务实体
	 * 
	 * @param baseUrl
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

			OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
					.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
					.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
			String hostname = baseurl.replace("https://", "").replace("/", "").split(":")[0];

			// 支持https,添加证书指纹,验证域名
			clientBuilder
					.certificatePinner(new CertificatePinner.Builder()
							.add(hostname, "sha256/jrDEZDX1N+Szu2sVh3jIPvp1ikCwEY2x4mQiRBKvKU4=")
							.add(hostname, "sha256/IiSbZ4pMDEyXvtl7Lg8K3FNmJcTAhKUTrB2FQOaAO/s=")
							.add(hostname, "sha256/JbQbUG5JMJUoI6brnx0x3vZF6jilxsapbXGVfjhN8Fg=").build())
					.hostnameVerifier((str, session) -> hostname.equals(str));

			// 添加服务拦截器实例
			for (Class<?> interceptorClass : interceptorClasses) {
				if (Interceptor.class.isAssignableFrom(interceptorClass)) {
					try {
						clientBuilder.addInterceptor((Interceptor) interceptorClass.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}

			Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl).client(clientBuilder.build())
					.addConverterFactory(ScalarsConverterFactory.create())
					.addConverterFactory(GsonConverterFactory.create()).build();
			httpServiceBean.setRetrofit(retrofit);
			serviceBeans.put(baseurl, httpServiceBean);
		}
		Retrofit retrofit = httpServiceBean.getRetrofit();
		Object service = retrofit.create(serviceClass);
		httpServiceBean.getServices().put(serviceClass, service);
		return service;
	}
}
