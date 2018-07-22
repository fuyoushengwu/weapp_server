package cn.aijiamuyingfang.commons.domain.goods;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * [描述]:
 * <p>
 * 商品详情
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:13:06
 */
@Entity
public class GoodDetail {
  /**
   * 商品详情Id
   */
  @Id
  private String id;

  /**
   * 保质期
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "start", column = @Column(name = "lifetime_start")),
      @AttributeOverride(name = "end", column = @Column(name = "lifetime_end")) })
  private ShelfLife lifetime;

  /**
   * 商品详细图片
   */
  @ElementCollection
  private List<String> detailImgList = new ArrayList<>();

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

  public List<String> getDetailImgList() {
    return detailImgList;
  }

  public void setDetailImgList(List<String> detailImgList) {
    this.detailImgList = detailImgList;
  }

}
