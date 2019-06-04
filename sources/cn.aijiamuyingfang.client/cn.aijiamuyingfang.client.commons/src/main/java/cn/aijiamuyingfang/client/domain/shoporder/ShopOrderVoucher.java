package cn.aijiamuyingfang.client.domain.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
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
   * 订单使用的用户兑换券Id
   */
  private String uservoucherId;

  /**
   * 订单使用的兑换方式
   */
  private String voucherItemId;

  /**
   * 该兑换项关联的商品Id
   */
  private String goodId;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(uservoucherId);
    dest.writeString(voucherItemId);
    dest.writeString(goodId);
  }

  public ShopOrderVoucher() {
  }

  private ShopOrderVoucher(Parcel in) {
    id = in.readString();
    uservoucherId = in.readString();
    voucherItemId = in.readString();
    goodId = in.readString();
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
