package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.commons.domain.SendType;
import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;
import java.util.Date;

/**
 * [描述]:
 * <p>
 * <code>POST '/user/{userid}/shoporder'</code>方法的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:29:16
 */
public class CreateUserShoprderRequest {
  /**
   * 订单状态::0:预订;1:未开始;2:进行中;3:已完成;4:订单超时
   */
  private ShopOrderStatus status;

  /**
   * 订单的配送方式: 0:到店自取(pickup); 1:送货上门(ownsend); 2:快递(thirdsend);
   */
  private SendType sendtype;

  /**
   * 取货/收货地址的ID
   */
  private String addressid;

  /**
   * 自提单的门店ID
   */
  private String storeid;

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

  public ShopOrderStatus getStatus() {
    return status;
  }

  public void setStatus(ShopOrderStatus status) {
    this.status = status;
  }

  public SendType getSendtype() {
    return sendtype;
  }

  public void setSendtype(SendType sendtype) {
    this.sendtype = sendtype;
  }

  public String getAddressid() {
    return addressid;
  }

  public void setAddressid(String addressid) {
    this.addressid = addressid;
  }

  public String getStoreid() {
    return storeid;
  }

  public void setStoreid(String storeid) {
    this.storeid = storeid;
  }

  public Date getPickupTime() {
    return pickupTime;
  }

  public void setPickupTime(Date pickupTime) {
    this.pickupTime = pickupTime;
  }

  public String getFormid() {
    return formid;
  }

  public void setFormid(String formid) {
    this.formid = formid;
  }

  public String getBuyerMessage() {
    return buyerMessage;
  }

  public void setBuyerMessage(String buyerMessage) {
    this.buyerMessage = buyerMessage;
  }

  public int getJfNum() {
    return jfNum;
  }

  public void setJfNum(int jfNum) {
    this.jfNum = jfNum;
  }

}
