package cn.aijiamuyingfang.vo.preorder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.goods.Good;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 预约的商品信息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:40:35
 */
@Data
public class PreOrderGood implements Parcelable {

  /**
   * 预约商品的
   */
  private Good good;

  /**
   * 预约的数量
   */
  private int count;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(good, flags);
    dest.writeInt(count);
  }

  public PreOrderGood() {
  }

  private PreOrderGood(Parcel in) {
    good = in.readParcelable(Good.class.getClassLoader());
    count = in.readInt();
  }

  public static final Parcelable.Creator<PreOrderGood> CREATOR = new Parcelable.Creator<PreOrderGood>() {
    @Override
    public PreOrderGood createFromParcel(Parcel in) {
      return new PreOrderGood(in);
    }

    @Override
    public PreOrderGood[] newArray(int size) {
      return new PreOrderGood[size];
    }
  };
}
