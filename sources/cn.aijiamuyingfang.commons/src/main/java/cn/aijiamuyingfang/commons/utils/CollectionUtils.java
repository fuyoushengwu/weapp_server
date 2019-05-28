package cn.aijiamuyingfang.commons.utils;

import java.util.Collection;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * Collection工具集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-08 20:18:58
 */
@UtilityClass
public class CollectionUtils {

  /**
   * Collection是否为空
   * 
   * @param collection
   * @return
   */
  public static boolean isEmpty(Collection<?> collection) {
    return (null == collection || collection.isEmpty());
  }

  /**
   * Collection是否包含元素
   * 
   * @param collection
   * @return
   */
  public static boolean hasContent(Collection<?> collection) {
    return !isEmpty(collection);
  }
}
