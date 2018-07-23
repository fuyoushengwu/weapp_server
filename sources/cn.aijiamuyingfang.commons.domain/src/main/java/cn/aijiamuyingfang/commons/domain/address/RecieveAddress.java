package cn.aijiamuyingfang.commons.domain.address;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import javax.persistence.Entity;

/**
 * [描述]:
 * <p>
 * 收货地址
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 20:38:53
 */
@Entity
public class RecieveAddress extends Address {
  /**
   * 收货地址-关联用户
   */
  private String userid;

  /**
   * 收货地址-联系电话
   */
  private String phone;

  /**
   * 收货地址-收件人
   */
  private String reciever;

  /**
   * 收货地址-默认收货地址
   */
  private boolean def;

  /**
   * 使用updateRecieveAddress更新收件地址信息
   * 
   * @param updateRecieveAddress
   */
  public void update(RecieveAddress updateRecieveAddress) {
    if (null == updateRecieveAddress) {
      return;
    }
    super.update(updateRecieveAddress);
    if (StringUtils.hasContent(updateRecieveAddress.userid)) {
      this.userid = updateRecieveAddress.userid;
    }
    if (StringUtils.hasContent(updateRecieveAddress.phone)) {
      this.phone = updateRecieveAddress.phone;
    }
    if (StringUtils.hasContent(updateRecieveAddress.reciever)) {
      this.reciever = updateRecieveAddress.reciever;
    }
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getReciever() {
    return reciever;
  }

  public void setReciever(String reciever) {
    this.reciever = reciever;
  }

  public boolean isDef() {
    return def;
  }

  public void setDef(boolean def) {
    this.def = def;
  }

}
