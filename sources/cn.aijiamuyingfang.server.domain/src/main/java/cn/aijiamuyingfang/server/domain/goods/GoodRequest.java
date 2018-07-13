package cn.aijiamuyingfang.server.domain.goods;

import cn.aijiamuyingfang.server.domain.goods.GoodDetail.ShelfLife;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;

/**
 * [描述]:
 * <p>
 * <code>POST '/good'</code>请求的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 20:18:30
 */
@MappedSuperclass
public class GoodRequest {
  /**
   * 商品Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  protected String id;

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
  protected String name;

  /**
   * 商品数量
   */
  protected int count;

  /**
   * 商品销量
   */
  protected int salecount;

  /**
   * 商品价格
   */
  protected double price;

  /**
   * 超市零售价
   */
  protected double marketprice;

  /**
   * 商品包装(听,盒,袋)
   */
  protected String pack;

  /**
   * 商品等级(例如:奶粉:一段,二段,三段,四段,儿童,中老年)
   */
  protected String level;

  /**
   * 商品条形码
   */
  protected String barcode;

  /**
   * 购买商品可以获得的积分
   */
  protected int score;

  /**
   * 购买商品可以获得的兑换券
   */
  protected String voucherId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

}
