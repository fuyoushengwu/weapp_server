package cn.aijiamuyingfang.client.domain.shopcart;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 购物车中的选购项
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:36:05
 */
@Data
public class ShopCart {
  /**
   * 购物车-货单ID
   */
  private String id;

  /**
   * 购物车关联用户Id
   */
  private String userid;

  /**
   * 关联商品Id
   */
  private String goodId;

  /**
   * 该项是否被选中
   */
  private boolean ischecked = true;

  /**
   * 商品数量
   */
  private int count;
}
