package cn.aijiamuyingfang.client.domain.previeworder;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 预约的商品信息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-03 04:40:35
 */
@Data
public class PreOrderGood {

  /**
   * 预约商品的Id
   */
  private String goodid;

  /**
   * 预约的数量
   */
  private int count;
}
