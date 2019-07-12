package cn.aijiamuyingfang.vo.goods;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import lombok.Getter;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:50
 */
@Getter
@Setter
public class Good implements Parcelable {
  /**
   * 商品Id
   */
  private String id;

  /**
   * 商品封面
   */
  private ImageSource coverImg;

  /**
   * 该商品是否废弃(该字段用于删除商品:当需要删除商品时,设置该字段为true)
   */
  private boolean deprecated;

  /**
   * 创建商品提供的商品保质期
   */
  private ShelfLife lifetime;

  /**
   * 商品名
   */
  private String name;

  /**
   * 商品数量
   */
  private int count;

  /**
   * 商品销量
   */
  private int salecount;

  /**
   * 商品价格
   */
  private double price;

  /**
   * 超市零售价
   */
  private double marketPrice;

  /**
   * 商品包装(听,盒,袋)
   */
  private String pack;

  /**
   * 商品等级(例如:奶粉:一段,二段,三段,四段,儿童,中老年)
   */
  private String level;

  /**
   * 商品条形码
   */
  private String barcode;

  /**
   * 购买商品可以获得的积分
   */
  private int score;

  /**
   * 购买商品可以获得的兑换券
   */
  private GoodVoucher goodVoucher;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeParcelable(coverImg, flags);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeParcelable(lifetime, flags);
    dest.writeString(name);
    dest.writeInt(count);
    dest.writeInt(salecount);
    dest.writeDouble(price);
    dest.writeDouble(marketPrice);
    dest.writeString(pack);
    dest.writeString(level);
    dest.writeString(barcode);
    dest.writeInt(score);
    dest.writeParcelable(goodVoucher, flags);
  }

  public Good() {
  }

  private Good(Parcel in) {
    this.id = in.readString();
    this.coverImg = in.readParcelable(ImageSource.class.getClassLoader());
    this.deprecated = in.readByte() != 0;
    this.lifetime = in.readParcelable(ShelfLife.class.getClassLoader());
    this.name = in.readString();
    this.count = in.readInt();
    this.salecount = in.readInt();
    this.price = in.readDouble();
    this.marketPrice = in.readDouble();
    this.pack = in.readString();
    this.level = in.readString();
    this.barcode = in.readString();
    this.score = in.readInt();
    this.goodVoucher = in.readParcelable(GoodVoucher.class.getClassLoader());
  }

  public static final Parcelable.Creator<Good> CREATOR = new Parcelable.Creator<Good>() {
    @Override
    public Good createFromParcel(Parcel in) {
      return new Good(in);
    }

    @Override
    public Good[] newArray(int size) {
      return new Good[size];
    }
  };

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Good other = (Good) obj;
    if (null == id) {
      return null == other.id;
    }
    return id.equals(other.id);
  }

}