package cn.aijiamuyingfang.server.user.dto;

import javax.persistence.Entity;

import cn.aijiamuyingfang.server.dto.address.AddressDTO;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.utils.StringUtils;
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
@Entity(name = "recieve_address")
@Getter
@Setter
@NoArgsConstructor
public class RecieveAddressDTO extends AddressDTO {
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
  public void update(RecieveAddress updateRecieveAddress) {
    if (null == updateRecieveAddress) {
      return;
    }
    super.update(updateRecieveAddress);
    if (StringUtils.hasContent(updateRecieveAddress.getUsername())) {
      this.setUsername(updateRecieveAddress.getUsername());
    }
    if (StringUtils.hasContent(updateRecieveAddress.getPhone())) {
      this.setPhone(updateRecieveAddress.getPhone());
    }
    if (StringUtils.hasContent(updateRecieveAddress.getReciever())) {
      this.setReciever(updateRecieveAddress.getReciever());
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
