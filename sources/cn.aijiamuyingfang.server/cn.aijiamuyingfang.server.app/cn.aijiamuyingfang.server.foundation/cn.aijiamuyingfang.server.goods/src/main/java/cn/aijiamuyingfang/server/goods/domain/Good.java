package cn.aijiamuyingfang.server.goods.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Good {
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
  @ManyToOne
  private ImageSource coverImg;

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
  private String voucherId;

  /**
   * 根据提供的Good更新本商品的信息
   * 
   * @param updateGood
   *          要更新的商品信息
   */
  public void update(Good updateGood) {
    if (null == updateGood) {
      return;
    }
    if (updateGood.coverImg != null) {
      this.coverImg.update(updateGood.coverImg);
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
    if (updateGood.marketPrice != 0) {
      this.marketPrice = updateGood.marketPrice;
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
