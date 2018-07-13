package cn.aijiamuyingfang.server.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
  private static final ThreadLocal<Gson> threadLocal = ThreadLocal.withInitial(() -> {
    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat("yyyy-MM-dd HH:mm");
    return builder.create();
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
   */
  public static String bean2Json(Object bean) {
    if (null == bean) {
      return NULL_DATA_STRING;
    }
    Gson gson = threadLocal.get();
    return gson.toJson(bean);
  }

  /**
   * map类型的键值对转换为json字符串
   * 
   * @param map
   * @return
   */
  public static String map2Json(Map<?, ?> map) {
    if (null == map) {
      return NULL_DATA_STRING;
    }
    Gson gson = threadLocal.get();
    return gson.toJson(map);
  }

  /**
   * list转换为json格式字符串
   * 
   * @param list
   * @return
   */
  public static String list2Json(List<?> list) {
    if (null == list) {
      return NULL_DATA_STRING;
    }
    Gson gson = threadLocal.get();
    return gson.toJson(list);
  }

  /**
   * json字符串转化为javaBean对象
   * 
   * @param json
   *          json格式字符串
   * @param beanClass
   *          要转换成的JavaBean类型
   * @return
   */
  public static <T> T json2Bean(String json, Class<T> beanClass) {
    if (StringUtils.isEmpty(json) || null == beanClass) {
      return null;
    }
    Gson gson = threadLocal.get();
    return gson.fromJson(json, beanClass);
  }

  /**
   * json字符串转换为javaBean的链表
   * 
   * @param json
   * @param beanClass
   * @return
   */
  public static <T> List<T> json2List(String json, Class<T> beanClass) {
    if (StringUtils.isEmpty(json) || null == beanClass) {
      return Collections.emptyList();
    }
    Gson gson = threadLocal.get();
    return gson.fromJson(json, new TypeToken<List<T>>() {
    }.getType());

  }

  /**
   * json字符串转换为map
   * 
   * @param json
   * @param keyClass
   * @param valueClass
   * @return
   */
  public static <K, V> Map<K, V> json2Map(String json, Class<K> keyClass, Class<V> valueClass) {
    if (StringUtils.isEmpty(json) || null == keyClass || null == valueClass) {
      return Collections.emptyMap();
    }
    Gson gson = threadLocal.get();
    return gson.fromJson(json, new TypeToken<Map<K, V>>() {
    }.getType());
  }

}
