package cn.aijiamuyingfang.client.domain.shoporder.request;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * <code>POST '/user/{userid}/shopcart'</code>请求的参数
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 21:36:36
 */
@Data
public class CreateShopCartRequest {
  /**
   * 商品ID
   */
  private String goodid;

  /**
   * 商品数量
   */
  private int goodNum;
}
