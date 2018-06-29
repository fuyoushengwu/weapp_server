package cn.aijiamuyingfang.server.client.bean;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * [描述]:
 * <p>
 * 某个Host下的服务实例
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 21:10:00
 */
public class HttpServiceBean {
	/**
	 * retrofit实例,用来创建HttpService注解的服务实例
	 */
	private Retrofit retrofit;

	/**
	 * HttpService服务实例的缓存.key:实例类型;value:服务实例
	 */
	private Map<Class<?>, Object> services = new HashMap<>();

	public Retrofit getRetrofit() {
		return retrofit;
	}

	public void setRetrofit(Retrofit retrofit) {
		this.retrofit = retrofit;
	}

	public Map<Class<?>, Object> getServices() {
		return services;
	}

	public void setServices(Map<Class<?>, Object> services) {
		this.services = services;
	}

}
