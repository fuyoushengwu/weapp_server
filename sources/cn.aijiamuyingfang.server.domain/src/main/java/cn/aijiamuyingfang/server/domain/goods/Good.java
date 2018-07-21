package cn.aijiamuyingfang.server.domain.goods;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import javax.persistence.Entity;

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
public class Good extends GoodRequest {

  /**
   * 商品封面
   */
  private String coverImg;

  /**
   * 该商品是否废弃(该字段用于删除商品:当需要删除商品时,设置该字段为true)
   */
  private boolean deprecated;

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
