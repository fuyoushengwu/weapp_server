package cn.aijiamuyingfang.server.domain.address;

import javax.persistence.MappedSuperclass;

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
@MappedSuperclass
public class RecieveAddressRequest extends AddressRequest {

  /**
   * 收货地址-关联用户
   */
  protected String userid;

  /**
   * 收货地址-联系电话
   */
  protected String phone;

  /**
   * 收货地址-收件人
   */
  protected String reciever;

  /**
   * 收货地址-默认收货地址
   */
  protected boolean def;

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
