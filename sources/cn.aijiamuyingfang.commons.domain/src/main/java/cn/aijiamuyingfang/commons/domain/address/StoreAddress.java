package cn.aijiamuyingfang.commons.domain.address;

import android.os.Parcel;
import android.os.Parcelable;
import javax.persistence.Entity;

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
@Entity
public class StoreAddress extends Address {
  /**
   * 店铺地址-联系电话
   */
  private String phone;

  /**
   * 店铺地址-联系人
   */
  private String contactor;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getContactor() {
    return contactor;
  }

  public void setContactor(String contactor) {
    this.contactor = contactor;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(phone);
    dest.writeString(contactor);
  }

  public StoreAddress() {
    super();
  }

  private StoreAddress(Parcel in) {
    super(in);
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
