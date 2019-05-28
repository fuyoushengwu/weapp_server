package cn.aijiamuyingfang.client.domain.coupon;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户获取的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:01
 */
@Data
public class UserVoucher {
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 用户ID
   */
  private String userid;

  /**
   * 兑换券
   */
  private GoodVoucher goodVoucher;

  /**
   * 用户拥有的该兑换券分值
   */
  private int score;
}
