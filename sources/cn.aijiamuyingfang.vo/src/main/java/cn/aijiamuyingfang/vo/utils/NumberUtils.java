package cn.aijiamuyingfang.vo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {

  public static int toInt(String str, int defaultValue) {
    if (str == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return defaultValue;
    }
  }
}
