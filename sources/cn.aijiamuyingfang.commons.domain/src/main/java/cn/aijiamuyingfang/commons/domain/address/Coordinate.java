package cn.aijiamuyingfang.commons.domain.address;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Coordinate implements Parcelable {
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
  }

  public Coordinate() {
  }

  private Coordinate(Parcel in) {
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
  }

  public static final Parcelable.Creator<Coordinate> CREATOR = new Parcelable.Creator<Coordinate>() {
    @Override
    public Coordinate createFromParcel(Parcel in) {
      return new Coordinate(in);
    }

    @Override
    public Coordinate[] newArray(int size) {
      return new Coordinate[size];
    }
  };
}
