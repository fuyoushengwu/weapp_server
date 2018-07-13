package cn.aijiamuyingfang.server.domain.coupon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

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
  private String userid;

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
   */
  public void increaseScore(int param) {
    this.score += param;
  }

  /**
   * 减少兑换券积分
   * 
   * @param param
   */
  public void decreaseScore(int param) {
    this.score -= param;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public GoodVoucher getGoodVoucher() {
    return goodVoucher;
  }

  public void setGoodVoucher(GoodVoucher goodVoucher) {
    this.goodVoucher = goodVoucher;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

}
