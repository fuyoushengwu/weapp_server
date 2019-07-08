package cn.aijiamuyingfang.vo.user;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.address.City;
import cn.aijiamuyingfang.vo.address.Coordinate;
import cn.aijiamuyingfang.vo.address.County;
import cn.aijiamuyingfang.vo.address.Province;
import cn.aijiamuyingfang.vo.address.Town;
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
public class RecieveAddress implements Parcelable {
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
  private String username;

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
    dest.writeString(username);
    dest.writeString(phone);
    dest.writeString(reciever);
    dest.writeByte((byte) (def ? 1 : 0));
  }

  public RecieveAddress() {
  }

  private RecieveAddress(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    province = in.readParcelable(Province.class.getClassLoader());
    city = in.readParcelable(City.class.getClassLoader());
    county = in.readParcelable(County.class.getClassLoader());
    town = in.readParcelable(Town.class.getClassLoader());
    detail = in.readString();
    coordinate = in.readParcelable(Coordinate.class.getClassLoader());
    username = in.readString();
    phone = in.readString();
    reciever = in.readString();
    def = in.readByte() != 0;
  }

  public static final Parcelable.Creator<RecieveAddress> CREATOR = new Parcelable.Creator<RecieveAddress>() {

    @Override
    public RecieveAddress createFromParcel(Parcel source) {
      return new RecieveAddress(source);
    }

    @Override
    public RecieveAddress[] newArray(int size) {
      return new RecieveAddress[size];
    }

  };
}
