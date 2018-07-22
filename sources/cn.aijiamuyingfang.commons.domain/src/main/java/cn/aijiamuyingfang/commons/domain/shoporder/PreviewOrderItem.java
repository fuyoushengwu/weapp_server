package cn.aijiamuyingfang.commons.domain.shoporder;

import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

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
public class PreviewOrderItem extends PreviewOrderItemRequest {
  /**
   * 预览项的ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 通过ShopCartItem生成PreviewOrderItem
   * 
   * @param shopcartItem
   * @return
   */
  public static PreviewOrderItem fromShopCartItem(ShopCartItem shopcartItem) {
    if (null == shopcartItem) {
      return null;
    }
    PreviewOrderItem previeworderItem = new PreviewOrderItem();
    previeworderItem.setCount(shopcartItem.getCount());
    previeworderItem.setGood(shopcartItem.getGood());
    previeworderItem.setShopcartItemId(shopcartItem.getId());
    return previeworderItem;
  }

  /**
   * 使用提供的updateOrderItem更新预览项
   * 
   * @param updateOrderItem
   */
  public void update(PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return;
    }
    if (updateOrderItem.count != 0) {
      this.count = updateOrderItem.count;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PreviewOrderItem other = (PreviewOrderItem) obj;
    if (null == id) {
      return null == other.id;
    }
    return id.equals(other.id);

  }

}
