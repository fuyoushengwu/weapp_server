package cn.aijiamuyingfang.client.domain.previeworder;

import java.util.ArrayList;
import java.util.List;

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
  private String userId;

  /**
   * 收货地址
   */
  private String recieveAddressId;

  /**
   * 预览的商品项
   */
  private List<PreviewOrderItem> orderItemList = new ArrayList<>();
}
