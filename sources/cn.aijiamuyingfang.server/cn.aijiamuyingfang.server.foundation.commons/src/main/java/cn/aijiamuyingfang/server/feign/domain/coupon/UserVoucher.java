package cn.aijiamuyingfang.server.feign.domain.coupon;

import lombok.Data;

@Data
public class UserVoucher {
  private String id;

  /**
   * 用户ID
   */
  private String username;

  /**
   * 用户拥有的该兑换券分值
   */
  private int score;

  /**
   * 兑换券
   */
  private GoodVoucher goodVoucher;

  /**
   * 增加兑换券积分
   * 
   * @param param
   *          要增加的兑换积分
   */
  public void increaseScore(int param) {
    this.score += param;
  }

  /**
   * 减少兑换券积分
   * 
   * @param param
   *          要减少的兑换积分
   */
  public void decreaseScore(int param) {
    this.score -= param;
  }

}
