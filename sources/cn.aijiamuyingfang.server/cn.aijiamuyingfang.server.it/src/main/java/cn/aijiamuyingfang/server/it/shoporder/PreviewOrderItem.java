package cn.aijiamuyingfang.server.it.shoporder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 订单预览中的商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:55:10
 */
@Entity
@Data
public class PreviewOrderItem {
  /**
   * 预览项的ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 商品数量
   */
  private int count;

  /**
   * 关联的购物车项Id
   */
  private String shopcartId;

  /**
   * 商品Id
   */
  private String goodId;

  /**
   * 通过ShopCart生成PreviewOrderItem
   * 
   * @param shopcart
   *          购物车项
   * @return 预览项
   */
  public static PreviewOrderItem fromShopCart(ShopCart shopcart) {
    if (null == shopcart) {
      return null;
    }
    PreviewOrderItem previeworderItem = new PreviewOrderItem();
    previeworderItem.setCount(shopcart.getCount());
    previeworderItem.setGoodId(shopcart.getGoodId());
    previeworderItem.setShopcartId(shopcart.getId());
    return previeworderItem;
  }

  /**
   * 使用提供的updateOrderItem更新预览项
   * 
   * @param updateOrderItem
   *          预览项
   */
  public void update(PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return;
    }
    if (updateOrderItem.count != 0) {
      this.count = updateOrderItem.count;
    }
  }
}
