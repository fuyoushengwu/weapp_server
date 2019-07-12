package cn.aijiamuyingfang.server.goods.dto;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;

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
@Entity(name = "good")
@Data
public class GoodDTO {
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
  private ImageSourceDTO coverImg;

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
  private ShelfLifeDTO lifetime;

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
    if (updateGood.getCoverImg() != null) {
      this.setCoverImg(ConvertUtils.convertImageSource(updateGood.getCoverImg()));
    }
    updateNumber(updateGood);
    updateString(updateGood);
  }

  /**
   * 更新数字信息(该方法是为了拆减update方法的复杂度抽取的)
   */
  private void updateNumber(Good updateGood) {
    if (updateGood.getCount() != 0) {
      this.setCount(updateGood.getCount());
    }
    if (updateGood.getSalecount() != 0) {
      this.setSalecount(updateGood.getSalecount());
    }
    if (updateGood.getPrice() != 0) {
      this.setPrice(updateGood.getPrice());
    }
    if (updateGood.getMarketPrice() != 0) {
      this.setMarketPrice(updateGood.getMarketPrice());
    }
    if (updateGood.getScore() != 0) {
      this.setScore(updateGood.getScore());
    }
  }

  /**
   * 更新字符串信息(该方法是为了拆减update方法的复杂度抽取的)
   */
  private void updateString(Good updateGood) {
    if (StringUtils.hasContent(updateGood.getName())) {
      this.setName(updateGood.getName());
    }
    if (StringUtils.hasContent(updateGood.getPack())) {
      this.setPack(updateGood.getPack());
    }
    if (StringUtils.hasContent(updateGood.getLevel())) {
      this.setLevel(updateGood.getLevel());
    }
    if (StringUtils.hasContent(updateGood.getBarcode())) {
      this.setBarcode(updateGood.getBarcode());
    }
    if (updateGood.getGoodVoucher() != null) {
      this.setVoucherId(updateGood.getGoodVoucher().getId());
    }
  }
}
