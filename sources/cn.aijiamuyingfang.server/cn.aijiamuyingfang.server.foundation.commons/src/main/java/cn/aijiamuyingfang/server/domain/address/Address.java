package cn.aijiamuyingfang.server.domain.address;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 地址相关请求的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 23:00:45
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Address {
  /**
   * 地址-Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  protected String id;

  /**
   * 地址是否废弃
   */
  protected boolean deprecated;

  /**
   * 地址-省
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "province_name")),
      @AttributeOverride(name = "code", column = @Column(name = "province_code")) })
  protected Province province;

  /**
   * 地址-市
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "city_name")),
      @AttributeOverride(name = "code", column = @Column(name = "city_code")) })
  protected City city;

  /**
   * 地址-县/区
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "county_name")),
      @AttributeOverride(name = "code", column = @Column(name = "county_code")) })
  protected County county;

  /**
   * 地址-镇/社区
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "town_name")),
      @AttributeOverride(name = "code", column = @Column(name = "town_code")) })
  protected Town town;

  /**
   * 地址-详细地址
   */
  protected String detail;

  /**
   * 地址-坐标
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "latitude", column = @Column(name = "coordinate_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "coordinate_longitude")) })
  protected Coordinate coordinate;

  /**
   * 使用updateAddress更新地址信息
   * 
   * @param updateAddress
   *          要更新的定制信息
   */
  public void update(Address updateAddress) {
    if (null == updateAddress) {
      return;
    }
    if (updateAddress.province != null) {
      this.province = updateAddress.province;
    }
    if (updateAddress.city != null) {
      this.city = updateAddress.city;
    }
    if (updateAddress.county != null) {
      this.county = updateAddress.county;
    }
    if (updateAddress.town != null) {
      this.town = updateAddress.town;
    }
    if (StringUtils.hasContent(updateAddress.detail)) {
      this.detail = updateAddress.detail;
    }
    if (updateAddress.coordinate != null) {
      this.coordinate = updateAddress.coordinate;
    }
  }
}
