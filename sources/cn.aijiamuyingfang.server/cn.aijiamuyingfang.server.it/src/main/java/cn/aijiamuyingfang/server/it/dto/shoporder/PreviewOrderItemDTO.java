package cn.aijiamuyingfang.server.it.dto.shoporder;

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
public class PreviewOrderItemDTO {
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
  private String shopCartId;

  /**
   * 商品Id
   */
  private String goodId;

  /**
   * 通过ShopCart生成PreviewOrderItem
   * 
   * @param shopCart
   *          购物车项
   * @return 预览项
   */
  public static PreviewOrderItemDTO fromShopCart(ShopCartDTO shopCart) {
    if (null == shopCart) {
      return null;
    }
    PreviewOrderItemDTO previeworderItem = new PreviewOrderItemDTO();
    previeworderItem.setCount(shopCart.getCount());
    previeworderItem.setGoodId(shopCart.getGoodId());
    previeworderItem.setShopCartId(shopCart.getId());
    return previeworderItem;
  }

  /**
   * 使用提供的updateOrderItem更新预览项
   * 
   * @param updateOrderItem
   *          预览项
   */
  public void update(PreviewOrderItemDTO updateOrderItem) {
    if (null == updateOrderItem) {
      return;
    }
    if (updateOrderItem.count != 0) {
      this.count = updateOrderItem.count;
    }
  }
}