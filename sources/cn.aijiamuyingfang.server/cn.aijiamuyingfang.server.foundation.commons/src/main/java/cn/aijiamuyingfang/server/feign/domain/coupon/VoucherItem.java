package cn.aijiamuyingfang.server.feign.domain.coupon;

import lombok.Data;

@Data
public class VoucherItem {
  private String id;

  /**
   * 兑换券关联的商品ID
   */
  private String goodid;

  /**
   * 多少兑换值可以兑换商品
   */
  private int score;
}
