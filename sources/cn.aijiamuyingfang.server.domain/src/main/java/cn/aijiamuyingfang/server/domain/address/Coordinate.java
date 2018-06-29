package cn.aijiamuyingfang.server.domain.address;

/**
 * [描述]:
 * <p>
 * 位置坐标
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:50:27
 */
public class Coordinate {
	/**
	 * 位置-纬度
	 */
	private double latitude;

	/**
	 * 位置-经度
	 */
	private double longitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
