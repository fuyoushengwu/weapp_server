package cn.aijiamuyingfang.server.domain.address;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 地址
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:54:36
 */
@MappedSuperclass
public abstract class Address {
	/**
	 * 地址-Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 地址-省
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "province_name")),
			@AttributeOverride(name = "code", column = @Column(name = "province_code")) })
	private Province province;

	/**
	 * 地址-市
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "city_name")),
			@AttributeOverride(name = "code", column = @Column(name = "city_code")) })
	private City city;

	/**
	 * 地址-县/区
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "county_name")),
			@AttributeOverride(name = "code", column = @Column(name = "county_code")) })
	private County county;

	/**
	 * 地址-镇/社区
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "town_name")),
			@AttributeOverride(name = "code", column = @Column(name = "town_code")) })
	private Town town;

	/**
	 * 地址-详细地址
	 */
	private String detail;

	/**
	 * 地址-坐标
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "latitude", column = @Column(name = "coordinate_latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "coordinate_longitude")) })
	private Coordinate coordinate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public County getCounty() {
		return county;
	}

	public void setCounty(County county) {
		this.county = county;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

}
