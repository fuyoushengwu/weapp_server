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
import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.goods.GoodDetail;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

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
@Entity(name = "good_detail")
@Data
public class GoodDetailDTO {
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
  private ShelfLifeDTO lifetime;

  /**
   * 商品详细图片
   */
  @ManyToMany
  private List<ImageSourceDTO> detailImgList = new ArrayList<>();

  public void update(GoodDetail updateGoodDetail) {
    if (null == updateGoodDetail) {
      return;
    }
    if (updateGoodDetail.getLifetime() != null) {
      this.setLifetime(ConvertUtils.convertShelfLife(updateGoodDetail.getLifetime()));
    }
    if (CollectionUtils.isNotEmpty(updateGoodDetail.getDetailImgList())) {
      this.setDetailImgList(ConvertUtils.convertImageSourceList(updateGoodDetail.getDetailImgList()));
    }
  }
}
