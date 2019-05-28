package cn.aijiamuyingfang.client.oauth2.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * JSON工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 06:03:29
 */
@UtilityClass
public class JsonUtils {
  private static final Gson GSON = (new GsonBuilder()).serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }

  public static String toJson(Object obj, Type type) {
    return GSON.toJson(obj, type);
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    return GSON.fromJson(json, clazz);
  }

  public static <T> T fromJson(String json, Type typeOfT) {
    return GSON.fromJson(json, typeOfT);
  }
}