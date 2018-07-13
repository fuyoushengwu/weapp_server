package cn.aijiamuyingfang.server.domain.address;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

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
   * 地址-Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
