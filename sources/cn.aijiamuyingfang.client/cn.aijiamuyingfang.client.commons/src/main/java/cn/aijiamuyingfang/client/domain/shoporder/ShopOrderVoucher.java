package cn.aijiamuyingfang.client.domain.shoporder;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 订单使用的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:41:55
 */
@Data
public class ShopOrderVoucher {
  /**
   * ID
   */
  private String id;

  /**
   * 订单使用的用户兑换券Id
   */
  private String uservoucherId;

  /**
   * 订单使用的兑换方式
   */
  private String voucheritemId;

  /**
   * 该兑换项关联的商品Id
   */
  private String goodId;
}
