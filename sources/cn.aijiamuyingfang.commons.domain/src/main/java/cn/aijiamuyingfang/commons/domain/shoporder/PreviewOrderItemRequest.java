package cn.aijiamuyingfang.commons.domain.shoporder;

import cn.aijiamuyingfang.commons.domain.goods.Good;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 预览项相关请求的RequestBean
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 23:41:35
 */
@MappedSuperclass
public class PreviewOrderItemRequest {
  /**
   * 商品数量
   */
  protected int count;

  /**
   * 关联的购物车项Id
   */
  protected String shopcartItemId;

  /**
   * 商品
   */
  @ManyToOne
  private Good good;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getShopcartItemId() {
    return shopcartItemId;
  }

  public void setShopcartItemId(String shopcartItemId) {
    this.shopcartItemId = shopcartItemId;
  }

  public Good getGood() {
    return good;
  }

  public void setGood(Good good) {
    this.good = good;
  }
}
