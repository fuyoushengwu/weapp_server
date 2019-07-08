package cn.aijiamuyingfang.server.it.dto.shoporder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

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
@Entity
@Data
public class ShopCartDTO {
  /**
   * 购物车-货单ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 购物车关联用户Id
   */
  private String username;

  /**
   * 关联商品Id
   */
  private String goodId;

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
