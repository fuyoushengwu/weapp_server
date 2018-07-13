package cn.aijiamuyingfang.server.domain.shopcart;

import cn.aijiamuyingfang.server.domain.goods.Good;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

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
public class ShopCartItem {
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
  private String userid;

  /**
   * 关联商品
   */
  @ManyToOne
  private Good good;

  /**
   * 该项是否被选中
   */
  private boolean ischecked = true;

  /**
   * 商品数量
   */
  private int count;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public Good getGood() {
    return good;
  }

  public void setGood(Good good) {
    this.good = good;
  }

  public boolean isIschecked() {
    return ischecked;
  }

  public void setIschecked(boolean ischecked) {
    this.ischecked = ischecked;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
