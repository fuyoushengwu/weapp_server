package cn.aijiamuyingfang.client.domain.previeworder;

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
  private String shopcartId;

  /**
   * 商品Id
   */
  private String goodId;
}
