package cn.aijiamuyingfang.commons.domain.shoporder;

import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

/**
 * [描述]:
 * <p>
 * 订单
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:01:18
 */
@Entity
public class ShopOrder {

  /**
   * 订单ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 订单所属用户ID
   */
  private String userid;

  /**
   * 订单号
   */
  private String orderNo;

  /**
   * 订单状态:0:预订;1:未开始;2:进行中;3:已完成;4:订单超时
   */
  private ShopOrderStatus status;

  /**
   * 订单是否来自一个预约单
   */
  private boolean fromPreOrder;

  /**
   * 订单的配送方式: 0:到店自取(pickup); 1:送货上门(ownsend); 2:快递(thirdsend);
   */
  private SendType sendtype;

  /**
   * 订单创建时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime = new Date();

  /**
   * 订单创建时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date finishTime;

  /**
   * 预订取货时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date pickupTime;

  /**
   * 取货/收货地址;根据sendtype决定Address的意义
   */
  @ManyToOne
  private RecieveAddress recieveAddress;

  /**
   * 订单商品列表
   */
  @OneToMany(cascade = CascadeType.ALL)
  private List<ShopOrderItem> orderItemList = new ArrayList<>();

  /**
   * 快递公司
   */
  private String thirdsendCompany;

  /**
   * 当时第三方送货时:表示快递单号<br/>
   * 当时送货上门时:表示送货员
   */
  private String thirdsendNo;

  /**
   * 操作员
   */
  @ElementCollection
  private List<String> operator = new ArrayList<>();

  /**
   * 订单信息最后修改时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastModify = new Date();

  /**
   * 卖家留言
   */
  private String businessMessage;

  /**
   * 提交订单的表单ID
   */
  private String formid;

  /**
   * 订单中商品总额
   */
  private double totalGoodsPrice;

  /**
   * 订单中运费
   */
  private double sendPrice;

  /**
   * 订单使用的通用积分
   */
  private int score;

  /**
   * 订单使用的兑换券
   */
  @ManyToMany(cascade = CascadeType.ALL)
  private List<ShopOrderVoucher> orderVoucher = new ArrayList<>();

  /**
   * 订单总额
   */
  private double totalPrice;

  public ShopOrder() {
    // 因为SimpleDateFormat.format()方法不是线程安全的,所以为了避免多线程的问题不能将SimpleDateFormat作为类变量来调用‘format’方法
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
    this.orderNo = dateFormat.format(new Date());
  }

  /**
   * 距离最后修改日期(单位:天)
   * 
   * @return
   */
  public int getLastModifyTime() {
    Date now = new Date();
    return (int) ((now.getTime() - lastModify.getTime()) / 1000 / 60 / 60 / 24);
  }

  /**
   * 添加订单项
   * 
   * @param orderItem
   */
  public void addOrderItem(ShopOrderItem orderItem) {
    synchronized (this) {
      if (null == this.orderItemList) {
        this.orderItemList = new ArrayList<>();
      }
    }
    if (null == orderItem) {
      return;
    }
    this.orderItemList.add(orderItem);
  }

  /**
   * 添加操作员
   * 
   * @param operator
   */
  public void addOperator(String operator) {
    synchronized (this) {
      if (null == this.operator) {
        this.operator = new ArrayList<>();
      }
    }

    if (StringUtils.hasContent(operator)) {
      this.operator.add(operator);
    }
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

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public ShopOrderStatus getStatus() {
    return status;
  }

  public void setStatus(ShopOrderStatus status) {
    this.status = status;
  }

  public boolean isFromPreOrder() {
    return fromPreOrder;
  }

  public void setFromPreOrder(boolean fromPreOrder) {
    this.fromPreOrder = fromPreOrder;
  }

  public SendType getSendtype() {
    return sendtype;
  }

  public void setSendtype(SendType sendtype) {
    this.sendtype = sendtype;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
  }

  public Date getPickupTime() {
    return pickupTime;
  }

  public void setPickupTime(Date pickupTime) {
    this.pickupTime = pickupTime;
  }

  public RecieveAddress getRecieveAddress() {
    return recieveAddress;
  }

  public void setRecieveAddress(RecieveAddress recieveAddress) {
    this.recieveAddress = recieveAddress;
  }

  public List<ShopOrderItem> getOrderItemList() {
    return orderItemList;
  }

  public void setOrderItemList(List<ShopOrderItem> orderItemList) {
    this.orderItemList = orderItemList;
  }

  public String getThirdsendCompany() {
    return thirdsendCompany;
  }

  public void setThirdsendCompany(String thirdsendCompany) {
    this.thirdsendCompany = thirdsendCompany;
  }

  public String getThirdsendNo() {
    return thirdsendNo;
  }

  public void setThirdsendNo(String thirdsendNo) {
    this.thirdsendNo = thirdsendNo;
  }

  public List<String> getOperator() {
    return operator;
  }

  public void setOperator(List<String> operator) {
    this.operator = operator;
  }

  public Date getLastModify() {
    return lastModify;
  }

  public void setLastModify(Date lastModify) {
    this.lastModify = lastModify;
  }

  public String getBusinessMessage() {
    return businessMessage;
  }

  public void setBusinessMessage(String businessMessage) {
    this.businessMessage = businessMessage;
  }

  public String getFormid() {
    return formid;
  }

  public void setFormid(String formId) {
    this.formid = formId;
  }

  public double getTotalGoodsPrice() {
    return totalGoodsPrice;
  }

  public void setTotalGoodsPrice(double totalGoodsPrice) {
    this.totalGoodsPrice = totalGoodsPrice;
  }

  public double getSendPrice() {
    return sendPrice;
  }

  public void setSendPrice(double sendPrice) {
    this.sendPrice = sendPrice;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public List<ShopOrderVoucher> getOrderVoucher() {
    return orderVoucher;
  }

  public void setOrderVoucher(List<ShopOrderVoucher> orderVoucher) {
    this.orderVoucher = orderVoucher;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

}
