package cn.aijiamuyingfang.server.user.dto;

import javax.persistence.Entity;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
public class RecieveAddressDTO extends Address {
  /**
   * 收货地址-关联用户
   */
  private String username;

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
   *          要更新的收件地址
   */
  public void update(RecieveAddressDTO updateRecieveAddress) {
    if (null == updateRecieveAddress) {
      return;
    }
    super.update(updateRecieveAddress);
    if (StringUtils.hasContent(updateRecieveAddress.username)) {
      this.username = updateRecieveAddress.username;
    }
    if (StringUtils.hasContent(updateRecieveAddress.phone)) {
      this.phone = updateRecieveAddress.phone;
    }
    if (StringUtils.hasContent(updateRecieveAddress.reciever)) {
      this.reciever = updateRecieveAddress.reciever;
    }
  }

}
