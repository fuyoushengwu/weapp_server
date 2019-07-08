package cn.aijiamuyingfang.vo.shoporder;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 更新订单状态的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:08:32
 */
@Data
public class UpdateShopOrderStatusRequest {
  /**
   * 订单状态
   */
  private ShopOrderStatus status;

  /**
   * 订单寄送公司
   */
  private String thirdsendCompany;

  /**
   * 订单寄送单号
   */
  private String thirdsendno;

  /**
   * 操作员
   */
  private String operator;
}
