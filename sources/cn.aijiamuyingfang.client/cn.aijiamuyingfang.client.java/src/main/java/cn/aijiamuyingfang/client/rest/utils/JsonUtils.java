package cn.aijiamuyingfang.client.rest.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import cn.aijiamuyingfang.client.commons.utils.StringUtils;
import cn.aijiamuyingfang.client.domain.message.UserMessageType;
import cn.aijiamuyingfang.client.domain.shoporder.SendType;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.client.domain.user.Gender;
import cn.aijiamuyingfang.client.domain.user.UserAuthority;

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
    builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
    builder.registerTypeHierarchyAdapter(Gender.class, new TypeAdapter<Gender>() {

      @Override
      public void write(JsonWriter out, Gender value) throws IOException {
        out.value(value == null ? null : value.getValue());
      }

      @Override
      public Gender read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return Gender.UNKNOW;
        }
        int source = in.nextInt();
        for (Gender gender : Gender.values()) {
          if (gender.getValue() == source) {
            return gender;
          }
        }
        return Gender.UNKNOW;
      }
    });

    builder.registerTypeHierarchyAdapter(SendType.class, new TypeAdapter<SendType>() {

      @Override
      public void write(JsonWriter out, SendType value) throws IOException {
        out.value(value == null ? null : value.getValue());
      }

      @Override
      public SendType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return SendType.UNKNOW;
        }
        int source = in.nextInt();
        for (SendType sendType : SendType.values()) {
          if (sendType.getValue() == source) {
            return sendType;
          }
        }
        return SendType.UNKNOW;
      }
    });

    builder.registerTypeHierarchyAdapter(ShopOrderStatus.class, new TypeAdapter<ShopOrderStatus>() {

      @Override
      public void write(JsonWriter out, ShopOrderStatus value) throws IOException {
        out.value(value == null ? null : value.getValue());
      }

      @Override
      public ShopOrderStatus read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return ShopOrderStatus.UNKNOW;
        }
        int source = in.nextInt();
        for (ShopOrderStatus status : ShopOrderStatus.values()) {
          if (status.getValue() == source) {
            return status;
          }
        }
        return ShopOrderStatus.UNKNOW;
      }
    });

    builder.registerTypeHierarchyAdapter(UserMessageType.class, new TypeAdapter<UserMessageType>() {

      @Override
      public void write(JsonWriter out, UserMessageType value) throws IOException {
        out.value(value == null ? null : value.getValue());
      }

      @Override
      public UserMessageType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return UserMessageType.UNKNOW;
        }
        int source = in.nextInt();
        for (UserMessageType type : UserMessageType.values()) {
          if (type.getValue() == source) {
            return type;
          }
        }
        return UserMessageType.UNKNOW;
      }
    });
    builder.registerTypeAdapter(UserAuthority.class, new TypeAdapter<UserAuthority>() {

      @Override
      public void write(JsonWriter out, UserAuthority value) throws IOException {
        if (value != null) {
          out.value(value.getValue());
        } else {
          out.value(UserAuthority.UNKNOW.getValue());
        }
      }

      @Override
      public UserAuthority read(JsonReader in) throws IOException {
        int value = in.nextInt();
        return UserAuthority.fromValue(value);
      }
    });
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
    return gson.fromJson(json, TypeToken.getParameterized(List.class, beanClass).getType());
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
    return gson.fromJson(json, TypeToken.getParameterized(HashMap.class, keyClass, valueClass).getType());
  }

}
