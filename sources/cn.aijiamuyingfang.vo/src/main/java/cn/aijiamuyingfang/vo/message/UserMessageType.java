package cn.aijiamuyingfang.vo.message;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.BaseEnum;

/**
 * [描述]:
 * <p>
 * 用户消息类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 17:10:30
 */
public enum UserMessageType implements BaseEnum, Parcelable {

  /**
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW(0),
  /**
   * 通知
   */
  @SerializedName("1")
  NOTICE(1),

  /**
   * 链接
   */
  @SerializedName("2")
  LINK(2);

  private int value;

  private UserMessageType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static UserMessageType fromValue(int value) {
    for (UserMessageType type : UserMessageType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOW;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(value);
  }

  public static final Creator<UserMessageType> CREATOR = new Creator<UserMessageType>() {
    @Override
    public UserMessageType createFromParcel(Parcel in) {
      return UserMessageType.fromValue(in.readInt());
    }

    @Override
    public UserMessageType[] newArray(int size) {
      return new UserMessageType[size];
    }
  };
}