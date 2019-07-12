package cn.aijiamuyingfang.vo.address;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

@Data
public class Address implements Parcelable {
  /**
   * 地址-Id
   */
  protected String id;

  /**
   * 地址是否废弃
   */
  protected boolean deprecated;

  /**
   * 地址-省
   */
  protected Province province;

  /**
   * 地址-市
   */
  protected City city;

  /**
   * 地址-县/区
   */
  protected County county;

  /**
   * 地址-镇/社区
   */
  protected Town town;

  /**
   * 地址-详细地址
   */
  protected String detail;

  /**
   * 地址-坐标
   */
  protected Coordinate coordinate;

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
  }

  protected Address(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    province = in.readParcelable(Province.class.getClassLoader());
    city = in.readParcelable(City.class.getClassLoader());
    county = in.readParcelable(County.class.getClassLoader());
    town = in.readParcelable(Town.class.getClassLoader());
    detail = in.readString();
    coordinate = in.readParcelable(Coordinate.class.getClassLoader());
  }

  protected Address() {

  }

  public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {

    @Override
    public Address createFromParcel(Parcel source) {
      return new Address(source);
    }

    @Override
    public Address[] newArray(int size) {
      return new Address[size];
    }

  };

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
    Address other = (Address) obj;
    if (null == id) {
      return null == other.id;
    }
    return id.equals(other.id);
  }
}
