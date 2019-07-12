package cn.aijiamuyingfang.vo.shopcart;

import cn.aijiamuyingfang.vo.goods.Good;
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
  private String username;

  /**
   * 关联商品
   */
  private Good good;

  /**
   * 该项是否被选中
   */
  private boolean checked = true;

  /**
   * 商品数量
   */
  private int count;

  /**
   * 增加购物车中商品数量
   * 
   * @param c
   *          商品增加的数量
   */
  public void addCount(int c) {
    this.count += c;
  }
}
