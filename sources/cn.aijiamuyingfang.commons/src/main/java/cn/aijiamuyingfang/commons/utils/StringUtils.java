package cn.aijiamuyingfang.commons.utils;

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
public final class StringUtils {
  /**
   * 私有的构造函数,防止实例化StringUtils
   */
  private StringUtils() {
  }

  /**
   * String是否为空
   * 
   * @param str
   * @return
   */
  public static boolean isEmpty(String str) {
    return (null == str || "".equals(str));
  }

  /**
   * String是否有值
   * 
   * @param str
   * @return
   */
  public static boolean hasContent(String str) {
    return (str != null && !"".equals(str));
  }
}
