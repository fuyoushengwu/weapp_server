package cn.aijiamuyingfang.commons.domain.coupon;

import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * <code>POST '/coupon/voucheritem'</code>请求的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 19:56:24
 */
@MappedSuperclass
public class VoucherItemRequest {

  /**
   * 兑换项名称
   */
  protected String name;

  /**
   * 兑换项描述
   */
  protected String description;

  /**
   * 兑换券关联的商品ID
   */
  protected String goodid;

  /**
   * 多少兑换值可以兑换商品
   */
  protected int score;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGoodid() {
    return goodid;
  }

  public void setGoodid(String goodid) {
    this.goodid = goodid;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

}
