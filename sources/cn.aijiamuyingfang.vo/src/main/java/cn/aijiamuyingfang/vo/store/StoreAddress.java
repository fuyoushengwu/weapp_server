package cn.aijiamuyingfang.vo.store;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.address.Address;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class StoreAddress extends Address {

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
    super.writeToParcel(dest, flags);
    dest.writeString(phone);
    dest.writeString(contactor);
  }

  private StoreAddress(Parcel in) {
    super(in);
    phone = in.readString();
    contactor = in.readString();
  }

  public StoreAddress() {
    super();
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

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
