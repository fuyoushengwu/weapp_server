package cn.aijiamuyingfang.server.feign.domain.user;

import lombok.Data;

@Data
public class RecieveAddress {
  /**
   * 地址-Id
   */
  private String id;

  /**
   * 地址-详细地址
   */
  protected String detail;

  /**
   * 收货地址-默认收货地址
   */
  private boolean def;
}
