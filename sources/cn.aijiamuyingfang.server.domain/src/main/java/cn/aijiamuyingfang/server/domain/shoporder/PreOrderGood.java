package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.domain.goods.Good;

/**
 * [描述]:
 * <p>
 * 预约的商品信息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:40:35
 */
public class PreOrderGood {
  /**
   * 预约的商品
   */
  private Good good;

  /**
   * 预约的数量
   */
  private int count;

  public Good getGood() {
    return good;
  }

  public void setGood(Good good) {
    this.good = good;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
