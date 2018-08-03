package cn.aijiamuyingfang.commons.domain.user;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.domain.BaseEnum;
import com.google.gson.annotations.SerializedName;

/**
 * [描述]:
 * <p>
 * 用户性别
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 16:38:23
 */
public enum Gender implements BaseEnum, Parcelable {
  /**
   * 男性
   */
  @SerializedName("1")
  MALE(1),

  /**
   * 女性
   */
  @SerializedName("2")
  FEMALE(2),

  /**
   * 未知
   */
  @SerializedName("-1")
  UNKNOW(-1);
  private int value;

  private Gender(int value) {
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

  public static final Parcelable.Creator<Gender> CREATOR = new Parcelable.Creator<Gender>() {
    @Override
    public Gender createFromParcel(Parcel in) {
      int value = in.readInt();
      for (Gender status : Gender.values()) {
        if (status.value == value) {
          return status;
        }
      }
      return Gender.UNKNOW;
    }

    @Override
    public Gender[] newArray(int size) {
      return new Gender[size];
    }
  };
}