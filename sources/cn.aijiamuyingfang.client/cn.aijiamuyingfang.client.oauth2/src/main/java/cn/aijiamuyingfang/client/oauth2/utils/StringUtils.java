package cn.aijiamuyingfang.client.oauth2.utils;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * String工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 21:33:42
 */
@UtilityClass
public class StringUtils {

  /**
   * 
   * @param str
   *          要判断的字符串
   * @return 字符串是否为空
   */
  public static boolean isEmpty(String str) {
    return (null == str || "".equals(str));
  }

  /**
   * 
   * @param str
   *          要判断的字符串
   * @return 字符串是否有值
   */
  public static boolean hasContent(String str) {
    return (str != null && !"".equals(str));
  }

  public static boolean isBlank(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }

  public static boolean hasLength(String str) {
    return (str != null && !str.isEmpty());
  }

  public static boolean hasText(CharSequence str) {
    return (hasLength(str) && containsText(str));
  }

  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  private static boolean containsText(CharSequence str) {
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }
}
