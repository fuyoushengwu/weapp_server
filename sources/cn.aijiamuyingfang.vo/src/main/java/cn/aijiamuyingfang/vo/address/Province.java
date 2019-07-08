package cn.aijiamuyingfang.vo.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * [描述]:
 * <p>
 * 省
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:51:40
 */
public class Province extends District {

  public Province() {
  }

  public Province(String name, String code) {
    super(name, code);
  }

  private Province(Parcel in) {
    this.name = in.readString();
    this.code = in.readString();
  }

  public static final Parcelable.Creator<Province> CREATOR = new Parcelable.Creator<Province>() {
    public Province createFromParcel(Parcel in) {
      return new Province(in);
    }

    public Province[] newArray(int size) {
      return new Province[size];
    }
  };
}
