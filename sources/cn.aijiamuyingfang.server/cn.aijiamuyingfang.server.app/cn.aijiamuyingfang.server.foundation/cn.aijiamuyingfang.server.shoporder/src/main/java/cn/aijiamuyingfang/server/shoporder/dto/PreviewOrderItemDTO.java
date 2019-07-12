package cn.aijiamuyingfang.server.shoporder.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.vo.review.PreviewOrderItem;
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
@Entity(name = "preview_order_item")
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
   * 使用提供的updateOrderItem更新预览项
   * 
   * @param updateOrderItem
   *          预览项
   */
  public void update(PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return;
    }
    if (updateOrderItem.getCount() != 0) {
      this.setCount(updateOrderItem.getCount());
    }
  }
}
