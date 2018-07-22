package cn.aijiamuyingfang.commons.utils;

import java.util.Collection;

/**
 * Miscellaneous collection utility methods. Mainly for internal use within the framework.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Arjen Poutsma
 * @since 1.1.3
 */
public abstract class CollectionUtils {
  /**
   * Return {@code true} if the supplied Collection is {@code null} or empty. Otherwise, return
   * {@code false}.
   * 
   * @param collection
   *          the Collection to check
   * @return whether the given Collection is empty
   */
  public static boolean isEmpty(Collection<?> collection) {
    return (collection == null || collection.isEmpty());
  }
}