package cn.aijiamuyingfang.server.goods.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
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
  @ManyToMany
  private List<ImageSource> detailImgList = new ArrayList<>();

  public void update(GoodDetail updateGoodDetail) {
    if (null == updateGoodDetail) {
      return;
    }
    if (updateGoodDetail.lifetime != null) {
      this.lifetime = updateGoodDetail.lifetime;
    }
    if (!CollectionUtils.isEmpty(updateGoodDetail.detailImgList)) {
      this.detailImgList = updateGoodDetail.detailImgList;
    }
  }
}
