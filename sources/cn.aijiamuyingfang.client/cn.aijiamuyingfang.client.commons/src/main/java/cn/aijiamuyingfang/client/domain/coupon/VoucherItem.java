package cn.aijiamuyingfang.client.domain.coupon;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 兑换券可以选择的兑换方式
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:54
 */
@Data
public class VoucherItem {
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 兑换项名称
   */
  private String name;

  /**
   * 兑换项描述
   */
  private String description;

  /**
   * 兑换券关联的商品ID
   */
  private String goodid;

  /**
   * 多少兑换值可以兑换商品
   */
  private int score;
}
