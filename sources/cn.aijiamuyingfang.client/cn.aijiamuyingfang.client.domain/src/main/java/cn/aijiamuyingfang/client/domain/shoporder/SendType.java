package cn.aijiamuyingfang.client.domain.shoporder;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.client.commons.domain.BaseEnum;

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
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW(0),
  /**
   * 自取
   */
  @SerializedName("1")
  PICKUP(1),

  /**
   * 送货
   */
  @SerializedName("2")
  OWNSEND(2),

  /**
   * 快递
   */
  @SerializedName("3")
  THIRDSEND(3);

  private int value;

  private SendType(int value) {
    this.value = value;
  }

  public static SendType fromValue(int value) {
    for (SendType type : SendType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOW;
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
      return SendType.fromValue(in.readInt());
    }

    @Override
    public SendType[] newArray(int size) {
      return new SendType[size];
    }
  };
}