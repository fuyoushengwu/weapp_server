package cn.aijiamuyingfang.server.coupon.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserVoucher {
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 用户ID
   */
  private String username;

  /**
   * 兑换券
   */
  @ManyToOne
  private GoodVoucher goodVoucher;

  /**
   * 用户拥有的该兑换券分值
   */
  private int score;

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

  public void update(UserVoucher userVoucher) {
    if (userVoucher != null) {
      this.score = userVoucher.getScore();
    }
  }
}
