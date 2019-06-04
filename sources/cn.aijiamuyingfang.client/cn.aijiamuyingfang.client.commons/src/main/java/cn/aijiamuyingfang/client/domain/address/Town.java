package cn.aijiamuyingfang.client.domain.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * [描述]:
 * <p>
 * 镇/社区
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 18:00:46
 */
public class Town extends District {

  public Town() {
  }

  public Town(String name, String code) {
    super(name, code);
  }

  private Town(Parcel in) {
    this.name = in.readString();
    this.code = in.readString();
  }

  public static final Parcelable.Creator<Town> CREATOR = new Parcelable.Creator<Town>() {
    @Override
    public Town createFromParcel(Parcel in) {
      return new Town(in);
    }

    @Override
    public Town[] newArray(int size) {
      return new Town[size];
    }
  };
}
