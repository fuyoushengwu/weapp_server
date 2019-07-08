package cn.aijiamuyingfang.server.it.dto.goods;

import javax.persistence.Entity;

import cn.aijiamuyingfang.server.it.dto.address.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 店铺地址
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 20:37:56
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class StoreAddressDTO extends AddressDTO {
  /**
   * 店铺地址-联系电话
   */
  private String phone;

  /**
   * 店铺地址-联系人
   */
  private String contactor;
}
