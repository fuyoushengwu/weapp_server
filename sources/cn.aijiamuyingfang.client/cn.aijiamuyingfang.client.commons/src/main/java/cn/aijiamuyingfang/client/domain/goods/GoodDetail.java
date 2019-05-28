package cn.aijiamuyingfang.client.domain.goods;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.client.domain.ImageSource;
import lombok.Data;

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
@Data
public class GoodDetail {
  /**
   * 商品详情Id
   */
  private String id;

  /**
   * 保质期
   */
  private ShelfLife lifetime;

  /**
   * 商品详细图片
   */
  private List<ImageSource> detailImgList = new ArrayList<>();
}
