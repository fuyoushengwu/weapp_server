package cn.aijiamuyingfang.vo.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.coupon.UserVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.goods.Good;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 订单使用的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:41:55
 */
@Data
public class ShopOrderVoucher implements Parcelable {
  /**
   * ID
   */
  private String id;

  /**
   * 订单使用的用户兑换券
   */
  private UserVoucher userVoucher;

  /**
   * 订单使用的兑换方式
   */
  private VoucherItem voucherItem;

  /**
   * 该兑换项关联的商品
   */
  private Good good;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeParcelable(userVoucher, flags);
    dest.writeParcelable(voucherItem, flags);
    dest.writeParcelable(good, flags);
  }

  public ShopOrderVoucher() {
  }

  private ShopOrderVoucher(Parcel in) {
    id = in.readString();
    userVoucher = in.readParcelable(UserVoucher.class.getClassLoader());
    voucherItem = in.readParcelable(VoucherItem.class.getClassLoader());
    good = in.readParcelable(Good.class.getClassLoader());
  }

  public static final Parcelable.Creator<ShopOrderVoucher> CREATOR = new Parcelable.Creator<ShopOrderVoucher>() {
    @Override
    public ShopOrderVoucher createFromParcel(Parcel in) {
      return new ShopOrderVoucher(in);
    }

    @Override
    public ShopOrderVoucher[] newArray(int size) {
      return new ShopOrderVoucher[size];
    }
  };
}
