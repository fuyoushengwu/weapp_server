package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;

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

  public ShopOrderStatus getStatus() {
    return status;
  }

  public void setStatus(ShopOrderStatus status) {
    this.status = status;
  }

  public String getThirdsendCompany() {
    return thirdsendCompany;
  }

  public void setThirdsendCompany(String thirdsendCompany) {
    this.thirdsendCompany = thirdsendCompany;
  }

  public String getThirdsendno() {
    return thirdsendno;
  }

  public void setThirdsendno(String thirdsendno) {
    this.thirdsendno = thirdsendno;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

}
