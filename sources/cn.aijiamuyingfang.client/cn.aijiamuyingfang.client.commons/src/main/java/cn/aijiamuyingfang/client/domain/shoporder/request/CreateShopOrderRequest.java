package cn.aijiamuyingfang.client.domain.shoporder.request;

import java.util.Date;

import cn.aijiamuyingfang.client.domain.shoporder.SendType;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderStatus;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * <code>POST '/user/{username}/shoporder'</code>方法的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:29:16
 */
@Data
public class CreateShopOrderRequest {
  /**
   * 订单状态::0:预订;1:未开始;2:进行中;3:已完成;4:订单超时
   */
  private ShopOrderStatus status;

  /**
   * 订单的配送方式: 0:到店自取(pickup); 1:送货上门(ownsend); 2:快递(thirdsend);
   */
  private SendType sendType;

  /**
   * 取货/收货地址的ID
   */
  private String addressId;

  /**
   * 取货时间
   */
  private Date pickupTime;

  /**
   * 买家留言
   */
  private String buyerMessage;

  /**
   * 提交订单的表单ID
   */
  private String formid;

  /**
   * 订单使用的通用积分
   */
  private int jfNum;
}
