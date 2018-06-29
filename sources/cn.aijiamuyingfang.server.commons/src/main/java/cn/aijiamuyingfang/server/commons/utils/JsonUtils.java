package cn.aijiamuyingfang.server.commons.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * [描述]:
 * <p>
 * JSON工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 20:58:54
 */
public final class JsonUtils {

	// 线程局部变量，每个线程有自己的ObjectMapper变量，空间换取时间。
	private static final ThreadLocal<ObjectMapper> threadLocal = ThreadLocal.withInitial(() -> {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 输出Date的格式,因为SimpleDateFormat不是线程安全的,所以在每个ThreadLocal中都实例化一个SimpleDateFormat
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		return objectMapper;
	});

	/**
	 * 数据为null时返回的JSON String
	 */
	private static final String NULL_DATA_STRING = "";

	private JsonUtils() {
	}

	/**
	 * object转换为json字符串
	 * 
	 * @param bean
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String bean2Json(Object bean) {
		if (null == bean) {
			return NULL_DATA_STRING;
		}
		ObjectMapper objectMapper = threadLocal.get();
		try {
			return objectMapper.writeValueAsString(bean);
		} catch (JsonProcessingException e) {
			return NULL_DATA_STRING;
		}
	}

	/**
	 * map类型的键值对转换为json字符串
	 * 
	 * @param map
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String map2Json(Map<?, ?> map) {
		if (null == map) {
			return NULL_DATA_STRING;
		}
		ObjectMapper objectMapper = threadLocal.get();
		try {
			return objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			return NULL_DATA_STRING;
		}
	}

	/**
	 * list转换为json格式字符串
	 * 
	 * @param list
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String list2Json(List<?> list) {
		if (null == list) {
			return NULL_DATA_STRING;
		}
		ObjectMapper objectMapper = threadLocal.get();
		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return NULL_DATA_STRING;
		}
	}

	/**
	 * json字符串转化为javaBean对象
	 * 
	 * @param json
	 *            json格式字符串
	 * @param beanClass
	 *            要转换成的JavaBean类型
	 * @return
	 * @throws IOException
	 */
	public static <T> T json2Bean(String json, Class<T> beanClass) throws IOException {
		if (StringUtils.isEmpty(json) || null == beanClass) {
			return null;
		}
		ObjectMapper objectMapper = threadLocal.get();
		return objectMapper.readValue(json, beanClass);
	}

	/**
	 * json字符串转换为javaBean的链表
	 * 
	 * @param json
	 * @param beanClass
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> json2List(String json, Class<T> beanClass) throws IOException {
		if (StringUtils.isEmpty(json) || null == beanClass) {
			return Collections.emptyList();
		}
		ObjectMapper objectMapper = threadLocal.get();
		CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, beanClass);
		return objectMapper.readValue(json, collectionType);
	}

	/**
	 * json字符串转换为map
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static <K, V> Map<K, V> json2Map(String json, Class<K> keyClass, Class<V> valueClass) throws IOException {
		if (StringUtils.isEmpty(json) || null == keyClass || null == valueClass) {
			return Collections.emptyMap();
		}
		ObjectMapper objectMapper = threadLocal.get();
		MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
		return objectMapper.readValue(json, mapType);
	}

}
