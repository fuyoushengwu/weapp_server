package cn.aijiamuyingfang.vo.coupon;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户获取的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:01
 */
@Data
public class UserVoucher implements Parcelable {
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 用户ID
   */
  private String username;

  /**
   * 兑换券
   */
  private GoodVoucher goodVoucher;

  /**
   * 用户拥有的该兑换券分值
   */
  private int score;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeString(username);
    dest.writeParcelable(goodVoucher, flags);
    dest.writeInt(score);
  }

  public UserVoucher() {
  }

  private UserVoucher(Parcel in) {
    this.id = in.readString();
    this.deprecated = in.readByte() != 0;
    this.username = in.readString();
    this.goodVoucher = in.readParcelable(GoodVoucher.class.getClassLoader());
    this.score = in.readInt();
  }

  public static final Parcelable.Creator<UserVoucher> CREATOR = new Parcelable.Creator<UserVoucher>() {
    @Override
    public UserVoucher createFromParcel(Parcel in) {
      return new UserVoucher(in);
    }

    @Override
    public UserVoucher[] newArray(int size) {
      return new UserVoucher[size];
    }
  };
}
