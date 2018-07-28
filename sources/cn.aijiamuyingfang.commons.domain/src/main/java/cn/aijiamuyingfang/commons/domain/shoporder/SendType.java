package cn.aijiamuyingfang.commons.domain.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.domain.BaseEnum;
import com.google.gson.annotations.SerializedName;

/**
 * [描述]:
 * <p>
 * 订单配送类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:51:30
 */
public enum SendType implements BaseEnum, Parcelable {
  /**
   * 自取
   */
  @SerializedName("0")
  PICKUP(0),

  /**
   * 送货
   */
  @SerializedName("1")
  OWNSEND(1),

  /**
   * 快递
   */
  @SerializedName("2")
  THIRDSEND(2),

  /**
   * 未知类型
   */
  @SerializedName("-1")
  UNKNOW(-1);

  private int value;

  private SendType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(value);
  }

  public static final Parcelable.Creator<SendType> CREATOR = new Parcelable.Creator<SendType>() {
    @Override
    public SendType createFromParcel(Parcel in) {
      int value = in.readInt();
      for (SendType status : SendType.values()) {
        if (status.value == value) {
          return status;
        }
      }
      return SendType.UNKNOW;
    }

    @Override
    public SendType[] newArray(int size) {
      return new SendType[size];
    }
  };

}