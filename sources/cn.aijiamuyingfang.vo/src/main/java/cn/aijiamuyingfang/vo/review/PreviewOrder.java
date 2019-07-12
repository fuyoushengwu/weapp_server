package cn.aijiamuyingfang.vo.review;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.vo.user.RecieveAddress;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 预览的订单,在系统中一个用户只能有一个预览订单
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:54:00
 */
@Data
public class PreviewOrder {
  private String id;

  /**
   * 订单所属用户
   */
  private String username;

  /**
   * 收货地址
   */
  private RecieveAddress recieveAddress;

  /**
   * 预览的商品项
   */
  private List<PreviewOrderItem> orderItemList = new ArrayList<>();

  /**
   * 添加预览项
   * 
   * @param item
   *          预览项
   */
  public void addOrderItem(PreviewOrderItem item) {
    if (null == item) {
      return;
    }
    synchronized (this) {
      if (null == this.orderItemList) {
        this.orderItemList = new ArrayList<>();
      }
    }
    this.orderItemList.add(item);
  }
}
