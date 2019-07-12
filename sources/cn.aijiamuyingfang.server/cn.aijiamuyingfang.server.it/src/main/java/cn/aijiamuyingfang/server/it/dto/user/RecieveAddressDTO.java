package cn.aijiamuyingfang.server.it.dto.user;

import javax.persistence.Entity;

import cn.aijiamuyingfang.server.it.dto.address.AddressDTO;
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
}
