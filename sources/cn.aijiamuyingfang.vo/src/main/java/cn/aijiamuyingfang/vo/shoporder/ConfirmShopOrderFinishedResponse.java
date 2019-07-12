package cn.aijiamuyingfang.vo.shoporder;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 确认订单结束的返回类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 03:49:37
 */
@Data
public class ConfirmShopOrderFinishedResponse {
  /**
   * 通用积分
   */
  private int genericScore;

  /**
   * 兑换券专用积分
   */
  private int voucherScore;

  public void addGenericScore(int score) {
    this.genericScore += score;
  }

  public void addVoucherScore(int score) {
    this.voucherScore += score;
  }
}
