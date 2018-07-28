package cn.aijiamuyingfang.commons.domain.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * [描述]:
 * <p>
 * 市
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:52:32
 */
public class City extends District {

  public City() {
  }

  public City(String name, String code) {
    super(name, code);
  }

  private City(Parcel in) {
    this.name = in.readString();
    this.code = in.readString();
  }

  public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
    @Override
    public City createFromParcel(Parcel in) {
      return new City(in);
    }

    @Override
    public City[] newArray(int size) {
      return new City[size];
    }
  };
}
