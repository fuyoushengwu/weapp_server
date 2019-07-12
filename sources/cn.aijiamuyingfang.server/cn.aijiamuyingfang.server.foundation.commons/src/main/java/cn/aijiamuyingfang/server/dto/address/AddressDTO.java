package cn.aijiamuyingfang.server.dto.address;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.server.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.address.Address;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;

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
@Data
public abstract class AddressDTO {
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
  protected ProvinceDTO province;

  /**
   * 地址-市
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "city_name")),
      @AttributeOverride(name = "code", column = @Column(name = "city_code")) })
  protected CityDTO city;

  /**
   * 地址-县/区
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "county_name")),
      @AttributeOverride(name = "code", column = @Column(name = "county_code")) })
  protected CountyDTO county;

  /**
   * 地址-镇/社区
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "town_name")),
      @AttributeOverride(name = "code", column = @Column(name = "town_code")) })
  protected TownDTO town;

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
  protected CoordinateDTO coordinate;

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
    if (updateAddress.getProvince() != null) {
      this.setProvince(ConvertUtils.convertProvince(updateAddress.getProvince()));
    }
    if (updateAddress.getCity() != null) {
      this.setCity(ConvertUtils.convertCity(updateAddress.getCity()));
    }
    if (updateAddress.getCounty() != null) {
      this.setCounty(ConvertUtils.convertCounty(updateAddress.getCounty()));
    }
    if (updateAddress.getTown() != null) {
      this.setTown(ConvertUtils.convertTown(updateAddress.getTown()));
    }
    if (StringUtils.hasContent(updateAddress.getDetail())) {
      this.setDetail(updateAddress.getDetail());
    }
    if (updateAddress.getCoordinate() != null) {
      this.setCoordinate(ConvertUtils.convertCoordinate(updateAddress.getCoordinate()));
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    AddressDTO other = (AddressDTO) obj;
    if (null == id) {
      return null == other.id;
    }
    return id.equals(other.id);
  }
}
