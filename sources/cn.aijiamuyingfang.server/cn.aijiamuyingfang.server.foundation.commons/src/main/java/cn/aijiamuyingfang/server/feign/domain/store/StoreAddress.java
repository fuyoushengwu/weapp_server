package cn.aijiamuyingfang.server.feign.domain.store;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 店铺地址
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 20:37:56
 */
@Data
public class StoreAddress {
  /**
   * 地址-Id
   */
  private String id;

  /**
   * 地址-详细地址
   */
  protected String detail;
}
