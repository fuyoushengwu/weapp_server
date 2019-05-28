package cn.aijiamuyingfang.client.rest.utils;

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

  /**
   * <p>
   * Checks if a String is whitespace, empty ("") or null.
   * </p>
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if the String is null, empty or whitespace
   * @since 2.0
   */
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
}
