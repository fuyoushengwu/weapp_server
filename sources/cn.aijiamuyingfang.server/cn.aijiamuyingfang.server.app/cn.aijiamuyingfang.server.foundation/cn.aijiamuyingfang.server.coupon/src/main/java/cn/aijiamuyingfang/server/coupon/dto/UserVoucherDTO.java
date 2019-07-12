package cn.aijiamuyingfang.server.coupon.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.vo.coupon.UserVoucher;
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
@Entity(name = "user_voucher")
@Data
public class UserVoucherDTO {
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
  private GoodVoucherDTO goodVoucher;

  /**
   * 用户拥有的该兑换券分值
   */
  private int score;

  public void update(UserVoucher userVoucher) {
    if (userVoucher != null) {
      this.setScore(userVoucher.getScore());
    }
  }
}
