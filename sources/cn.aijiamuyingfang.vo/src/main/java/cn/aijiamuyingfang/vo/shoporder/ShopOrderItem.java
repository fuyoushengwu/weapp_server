package cn.aijiamuyingfang.vo.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.goods.Good;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 订单中的商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:01:45
 */
@Data
public class ShopOrderItem implements Parcelable {

  /**
   * 订单商品的ID
   */
  private String id;

  /**
   * 商品
   */
  private Good good;

  /**
   * 数量
   */
  private int count;

  /**
   * 该项价格: good.price*count
   */
  private double price;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeParcelable(good, flags);
    dest.writeInt(count);
    dest.writeDouble(price);
  }

  public ShopOrderItem() {
  }

  private ShopOrderItem(Parcel in) {
    id = in.readString();
    good = in.readParcelable(Good.class.getClassLoader());
    count = in.readInt();
    price = in.readDouble();
  }

  public static final Parcelable.Creator<ShopOrderItem> CREATOR = new Parcelable.Creator<ShopOrderItem>() {
    @Override
    public ShopOrderItem createFromParcel(Parcel in) {
      return new ShopOrderItem(in);
    }

    @Override
    public ShopOrderItem[] newArray(int size) {
      return new ShopOrderItem[size];
    }
  };
}
