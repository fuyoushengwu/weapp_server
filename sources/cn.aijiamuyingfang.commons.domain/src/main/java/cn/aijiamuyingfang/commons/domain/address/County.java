package cn.aijiamuyingfang.commons.domain.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * [描述]:
 * <p>
 * 县/区
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:53:29
 */
public class County extends District {
  public County() {
  }

  public County(String name, String code) {
    super(name, code);
  }

  private County(Parcel in) {
    this.name = in.readString();
    this.code = in.readString();
  }

  public static final Parcelable.Creator<County> CREATOR = new Parcelable.Creator<County>() {
    @Override
    public County createFromParcel(Parcel in) {
      return new County(in);
    }

    @Override
    public County[] newArray(int size) {
      return new County[size];
    }
  };
}
