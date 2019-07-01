package cn.aijiamuyingfang.server.it.data.qinsilk;

import java.util.List;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 秦丝商品信息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-30 20:05:00
 */
@Data
public class Good {
  private Long id;

  private String name;

  /**
   * 规格
   */
  private String specs;

  /**
   * 条码
   */
  private String goodsSn;

  /**
   * 销售价
   */
  private double tradePrice;

  /**
   * 主图
   */
  private String imgUrl;

  /**
   * 图片
   */
  private List<GoodImage> goodImages;
}
