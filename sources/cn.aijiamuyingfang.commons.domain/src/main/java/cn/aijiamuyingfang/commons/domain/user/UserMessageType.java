package cn.aijiamuyingfang.commons.domain.user;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.domain.BaseEnum;
import com.google.gson.annotations.SerializedName;

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
   * 通知
   */
  @SerializedName("0")
  NOTICE(0),

  /**
   * 链接
   */
  @SerializedName("1")
  LINK(1),
  /**
   * 位置类型
   */
  @SerializedName("-1")
  UNKNOW(-1);
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
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UserMessageType> CREATOR = new Creator<UserMessageType>() {
    @Override
    public UserMessageType createFromParcel(Parcel in) {
      int value = in.readInt();
      return UserMessageType.fromValue(value);
    }

    @Override
    public UserMessageType[] newArray(int size) {
      return new UserMessageType[size];
    }
  };

}