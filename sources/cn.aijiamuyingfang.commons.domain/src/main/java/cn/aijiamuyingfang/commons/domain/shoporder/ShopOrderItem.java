package cn.aijiamuyingfang.commons.domain.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

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
@Entity
public class ShopOrderItem implements Parcelable {

  /**
   * 订单商品的ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 商品
   */
  @ManyToOne
  private Good good;

  /**
   * 数量
   */
  private int count;

  /**
   * 该项价格: good.price*count
   */
  private double price;

  /**
   * 通过PreviewOrderItem生成ShopOrderItem
   * 
   * @param previeworderItem
   * @return
   */
  public static ShopOrderItem fromPreviewOrderItem(PreviewOrderItem previeworderItem) {
    if (null == previeworderItem) {
      return null;
    }
    ShopOrderItem shoporderItem = new ShopOrderItem();
    shoporderItem.setGood(previeworderItem.getGood());
    shoporderItem.setCount(previeworderItem.getCount());
    shoporderItem.setPrice(previeworderItem.getCount() * previeworderItem.getGood().getPrice());
    return shoporderItem;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Good getGood() {
    return good;
  }

  public void setGood(Good good) {
    this.good = good;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

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
