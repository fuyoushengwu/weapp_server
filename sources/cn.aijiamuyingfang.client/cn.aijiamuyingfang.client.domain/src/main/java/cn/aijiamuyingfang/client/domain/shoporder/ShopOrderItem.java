package cn.aijiamuyingfang.client.domain.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
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
  private String goodId;

  /**
   * 商品名
   */
  private String goodName;

  /**
   * 商品包装(听,盒,袋)
   */
  private String goodPack;

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
    dest.writeString(goodId);
    dest.writeString(goodName);
    dest.writeString(goodPack);
    dest.writeInt(count);
    dest.writeDouble(price);
  }

  public ShopOrderItem() {
  }

  private ShopOrderItem(Parcel in) {
    id = in.readString();
    goodId = in.readString();
    goodName = in.readString();
    goodPack = in.readString();
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
