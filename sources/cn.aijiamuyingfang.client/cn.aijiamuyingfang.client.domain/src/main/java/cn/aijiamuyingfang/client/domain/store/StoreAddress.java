package cn.aijiamuyingfang.client.domain.store;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.client.domain.address.City;
import cn.aijiamuyingfang.client.domain.address.Coordinate;
import cn.aijiamuyingfang.client.domain.address.County;
import cn.aijiamuyingfang.client.domain.address.Province;
import cn.aijiamuyingfang.client.domain.address.Town;
import lombok.Data;

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
@Data
public class StoreAddress implements Parcelable {
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
   * 店铺地址-联系电话
   */
  private String phone;

  /**
   * 店铺地址-联系人
   */
  private String contactor;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeParcelable(province, flags);
    dest.writeParcelable(city, flags);
    dest.writeParcelable(county, flags);
    dest.writeParcelable(town, flags);
    dest.writeString(detail);
    dest.writeParcelable(coordinate, flags);
    dest.writeString(phone);
    dest.writeString(contactor);
  }

  public StoreAddress() {
  }

  private StoreAddress(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    province = in.readParcelable(Province.class.getClassLoader());
    city = in.readParcelable(City.class.getClassLoader());
    county = in.readParcelable(County.class.getClassLoader());
    town = in.readParcelable(Town.class.getClassLoader());
    detail = in.readString();
    coordinate = in.readParcelable(Coordinate.class.getClassLoader());
    phone = in.readString();
    contactor = in.readString();
  }

  public static final Parcelable.Creator<StoreAddress> CREATOR = new Parcelable.Creator<StoreAddress>() {
    @Override
    public StoreAddress createFromParcel(Parcel in) {
      return new StoreAddress(in);
    }

    @Override
    public StoreAddress[] newArray(int size) {
      return new StoreAddress[size];
    }
  };
}
