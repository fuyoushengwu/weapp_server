package cn.aijiamuyingfang.client.domain.shoporder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

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
@Data
public class ShopOrder {

  /**
   * 订单ID
   */
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
  private Date createTime = new Date();

  /**
   * 订单创建时间
   */
  private Date finishTime;

  /**
   * 预订取货时间
   */
  private Date pickupTime;

  /**
   * 取货门店地址Id
   */
  private String pickupStoreAddressId;

  /**
   * 收货地址
   */
  private String recieveAddressId;

  /**
   * 订单商品列表
   */
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
  private List<String> operator = new ArrayList<>();

  /**
   * 订单信息最后修改时间
   */
  private Date lastModify = new Date();

  /**
   * 卖家留言
   */
  private String buyerMessage;

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
}
