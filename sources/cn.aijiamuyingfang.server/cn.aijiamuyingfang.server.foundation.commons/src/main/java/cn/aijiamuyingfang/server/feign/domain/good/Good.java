package cn.aijiamuyingfang.server.feign.domain.good;

import lombok.Data;

@Data
public class Good {
  private String id;

  /**
   * 商品名
   */
  private String name;

  /**
   * 商品销量
   */
  private int salecount;

  /**
   * 商品数量
   */
  private int count;

  /**
   * 商品价格
   */
  private double price;

  /**
   * 商品包装(听,盒,袋)
   */
  private String pack;

  /**
   * 购买商品可以获得的积分
   */
  private int score;

  /**
   * 购买商品可以获得的兑换券
   */
  private String voucherId;

}
