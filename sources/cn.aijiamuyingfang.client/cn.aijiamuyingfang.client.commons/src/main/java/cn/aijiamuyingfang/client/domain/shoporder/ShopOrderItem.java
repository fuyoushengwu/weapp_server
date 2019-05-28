package cn.aijiamuyingfang.client.domain.shoporder;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 订单中的商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:01:45
 */
@Data
public class ShopOrderItem {

  /**
   * 订单商品的ID
   */
  private String id;

  /**
   * 商品
   */
  private String goodId;

  /**
   * 商品名
   */
  private String goodName;

  /**
   * 商品包装(听,盒,袋)
   */
  private String goodPack;

  /**
   * 数量
   */
  private int count;

  /**
   * 该项价格: good.price*count
   */
  private double price;
}
