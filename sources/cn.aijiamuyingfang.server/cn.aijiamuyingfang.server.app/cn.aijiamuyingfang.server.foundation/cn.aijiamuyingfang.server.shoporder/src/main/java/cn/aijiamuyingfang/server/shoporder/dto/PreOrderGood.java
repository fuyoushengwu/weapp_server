package cn.aijiamuyingfang.server.shoporder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class PreOrderGood {

  /**
   * 预约商品的Id
   */
  private String goodId;

  /**
   * 预约商品的Name
   */
  private String goodName;

  /**
   * 预约的数量
   */
  private int count;
}
