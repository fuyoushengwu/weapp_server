package cn.aijiamuyingfang.vo.review;

import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
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
@Data
public class PreviewOrderItem {
  /**
   * 预览项的ID
   */
  private String id;

  /**
   * 商品数量
   */
  private int count;

  /**
   * 关联的购物车项Id
   */
  private ShopCart shopCart;

  /**
   * 商品
   */
  private Good good;

  /**
   * 通过ShopCart生成PreviewOrderItem
   * 
   * @param shopCart
   *          购物车项
   * @return 预览项
   */
  public static PreviewOrderItem fromShopCart(ShopCart shopCart) {
    if (null == shopCart) {
      return null;
    }
    PreviewOrderItem previeworderItem = new PreviewOrderItem();
    previeworderItem.setCount(shopCart.getCount());
    previeworderItem.setGood(shopCart.getGood());
    previeworderItem.setShopCart(shopCart);
    return previeworderItem;
  }
}
