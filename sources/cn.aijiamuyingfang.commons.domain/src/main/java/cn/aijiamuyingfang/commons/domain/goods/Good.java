package cn.aijiamuyingfang.commons.domain.goods;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

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
@Entity
public class Good implements Parcelable {
  /**
   * 商品Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 商品封面
   */
  private String coverImg;

  /**
   * 该商品是否废弃(该字段用于删除商品:当需要删除商品时,设置该字段为true)
   */
  private boolean deprecated;

  /**
   * 创建商品提供的商品保质期
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "start", column = @Column(name = "lifetime_start")),
      @AttributeOverride(name = "end", column = @Column(name = "lifetime_end")) })
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
  private double marketprice;

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
  private String voucherId;

  /**
   * 根据提供的Good更新本商品的信息
   * 
   * @param updateGood
   */
  public void update(Good updateGood) {
    if (null == updateGood) {
      return;
    }
    updateNumber(updateGood);
    updateString(updateGood);
  }

  /**
   * 更新数字信息(该方法是为了拆减update方法的复杂度抽取的)
   */
  private void updateNumber(Good updateGood) {
    if (updateGood.count != 0) {
      this.count = updateGood.count;
    }
    if (updateGood.salecount != 0) {
      this.salecount = updateGood.salecount;
    }
    if (updateGood.price != 0) {
      this.price = updateGood.price;
    }
    if (updateGood.marketprice != 0) {
      this.marketprice = updateGood.marketprice;
    }
    if (updateGood.score != 0) {
      this.score = updateGood.score;
    }
  }

  /**
   * 更新字符串信息(该方法是为了拆减update方法的复杂度抽取的)
   */
  private void updateString(Good updateGood) {
    if (StringUtils.hasContent(updateGood.name)) {
      this.name = updateGood.name;
    }
    if (StringUtils.hasContent(updateGood.coverImg)) {
      this.coverImg = updateGood.coverImg;
    }
    if (StringUtils.hasContent(updateGood.pack)) {
      this.pack = updateGood.pack;
    }
    if (StringUtils.hasContent(updateGood.level)) {
      this.level = updateGood.level;
    }
    if (StringUtils.hasContent(updateGood.barcode)) {
      this.barcode = updateGood.barcode;
    }
    if (StringUtils.hasContent(updateGood.voucherId)) {
      this.voucherId = updateGood.voucherId;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCoverImg() {
    return coverImg;
  }

  public void setCoverImg(String coverImg) {
    this.coverImg = coverImg;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

  public ShelfLife getLifetime() {
    return lifetime;
  }

  public void setLifetime(ShelfLife lifetime) {
    this.lifetime = lifetime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getSalecount() {
    return salecount;
  }

  public void setSalecount(int salecount) {
    this.salecount = salecount;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getMarketprice() {
    return marketprice;
  }

  public void setMarketprice(double marketprice) {
    this.marketprice = marketprice;
  }

  public String getPack() {
    return pack;
  }

  public void setPack(String pack) {
    this.pack = pack;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getVoucherId() {
    return voucherId;
  }

  public void setVoucherId(String voucherId) {
    this.voucherId = voucherId;
  }

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(coverImg);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeParcelable(lifetime, flags);
    dest.writeString(name);
    dest.writeInt(count);
    dest.writeInt(salecount);
    dest.writeDouble(price);
    dest.writeDouble(marketprice);
    dest.writeString(pack);
    dest.writeString(level);
    dest.writeString(barcode);
    dest.writeInt(score);
    dest.writeString(voucherId);
  }

  public Good() {
  }

  private Good(Parcel in) {
    this.id = in.readString();
    this.coverImg = in.readString();
    this.deprecated = in.readByte() != 0;
    this.lifetime = in.readParcelable(ShelfLife.class.getClassLoader());
    this.name = in.readString();
    this.count = in.readInt();
    this.salecount = in.readInt();
    this.price = in.readDouble();
    this.marketprice = in.readDouble();
    this.pack = in.readString();
    this.level = in.readString();
    this.barcode = in.readString();
    this.score = in.readInt();
    this.voucherId = in.readString();
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

}
