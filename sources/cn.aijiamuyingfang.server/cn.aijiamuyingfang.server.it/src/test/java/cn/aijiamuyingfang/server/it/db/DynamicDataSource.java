package cn.aijiamuyingfang.server.it.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import cn.aijiamuyingfang.server.it.context.DynamicDataSourceContextHolder;

/**
 * [描述]:
 * <p>
 * TODO
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-14 01:57:36
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    return DynamicDataSourceContextHolder.getDataSourceType();
  }

}
