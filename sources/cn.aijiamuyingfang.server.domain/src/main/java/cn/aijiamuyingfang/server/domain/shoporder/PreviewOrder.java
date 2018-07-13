package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

/**
 * [描述]:
 * <p>
 * 预览的订单,在系统中一个用户只能有一个预览订单
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:54:00
 */
@Entity
public class PreviewOrder {
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 订单所属用户
   */
  private String userid;

  /**
   * 收货地址
   */
  @ManyToOne
  private RecieveAddress recieveAddress;

  /**
   * 预览的商品项
   */
  @OneToMany(cascade = CascadeType.ALL)
  private List<PreviewOrderItem> orderItemList = new ArrayList<>();

  /**
   * 添加预览项
   * 
   * @param item
   */
  public void addOrderItem(PreviewOrderItem item) {
    if (null == item) {
      return;
    }
    synchronized (this) {
      if (null == this.orderItemList) {
        this.orderItemList = new ArrayList<>();
      }
    }
    this.orderItemList.add(item);
  }

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

  public RecieveAddress getRecieveAddress() {
    return recieveAddress;
  }

  public void setRecieveAddress(RecieveAddress recieveAddress) {
    this.recieveAddress = recieveAddress;
  }

  public List<PreviewOrderItem> getOrderItemList() {
    return orderItemList;
  }

  public void setOrderItemList(List<PreviewOrderItem> orderItemList) {
    this.orderItemList = orderItemList;
  }

}
