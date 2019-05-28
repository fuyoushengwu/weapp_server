package cn.aijiamuyingfang.server.it.context;

import java.util.ArrayList;
import java.util.List;

/**
 * [描述]:
 * <p>
 * 保存动态数据源的Key
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-14 01:18:52
 */
public class DynamicDataSourceContextHolder {
  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

  public static void setDataSourceType(String dataSourceType) {
    contextHolder.set(dataSourceType);
  }

  public static String getDataSourceType() {
    return contextHolder.get();
  }

  public static void clearDataSourceType() {
    contextHolder.remove();
  }

  private static List<String> dataSourceKeys = new ArrayList<>();

  /**
   * 判断指定DataSrouce当前是否存在
   *
   * @param dataSourceKey
   */
  public static boolean containsDataSource(String dataSourceKey) {
    return dataSourceKeys.contains(dataSourceKey);
  }

  /**
   * 添加动态数据源的Key
   * 
   * @param dataSourceId
   */
  public static void addDataSourceKey(String dataSourceKey) {
    dataSourceKeys.add(dataSourceKey);
  }
}
