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
public class RecieveAddress extends RecieveAddressRequest {

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
}
