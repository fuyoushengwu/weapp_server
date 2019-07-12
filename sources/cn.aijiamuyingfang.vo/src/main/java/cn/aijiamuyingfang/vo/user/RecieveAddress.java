package cn.aijiamuyingfang.vo.user;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.address.Address;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class RecieveAddress extends Address {
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
    super.writeToParcel(dest, flags);
    dest.writeString(username);
    dest.writeString(phone);
    dest.writeString(reciever);
    dest.writeByte((byte) (def ? 1 : 0));
  }

  public RecieveAddress() {
    super();
  }

  private RecieveAddress(Parcel in) {
    super(in);
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

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
