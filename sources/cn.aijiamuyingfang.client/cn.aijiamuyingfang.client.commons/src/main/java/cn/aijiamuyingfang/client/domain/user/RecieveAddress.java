package cn.aijiamuyingfang.client.domain.user;

import cn.aijiamuyingfang.client.domain.address.City;
import cn.aijiamuyingfang.client.domain.address.Coordinate;
import cn.aijiamuyingfang.client.domain.address.County;
import cn.aijiamuyingfang.client.domain.address.Province;
import cn.aijiamuyingfang.client.domain.address.Town;
import lombok.Data;

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
@Data
public class RecieveAddress {
  /**
   * 地址-Id
   */
  private String id;

  /**
   * 地址是否废弃
   */
  private boolean deprecated;

  /**
   * 地址-省
   */
  private Province province;

  /**
   * 地址-市
   */
  private City city;

  /**
   * 地址-县/区
   */
  private County county;

  /**
   * 地址-镇/社区
   */
  private Town town;

  /**
   * 地址-详细地址
   */
  private String detail;

  /**
   * 地址-坐标
   */
  private Coordinate coordinate;

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

}
