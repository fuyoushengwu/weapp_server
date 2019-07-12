package cn.aijiamuyingfang.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Validate {
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }
}
