package cn.aijiamuyingfang.vo.shoporder;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.store.StoreAddress;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ShopOrder implements Parcelable {

  /**
   * 订单ID
   */
  private String id;

  /**
   * 订单所属用户ID
   */
  private String username;

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
  private SendType sendType;

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
   * 取货门店地址
   */
  private StoreAddress storeAddress;

  /**
   * 收货地址
   */
  private RecieveAddress recieveAddress;

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
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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

  /**
   * 
   * @return 距离最后修改日期(单位:天)
   */
  public double getLastModifyTime() {
    Date now = new Date();
    return (now.getTime() - lastModify.getTime()) / 1000 / 60 / 60 / 24;
  }

  /**
   * 添加操作员
   * 
   * @param operator
   *          操作员
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

  /**
   * 添加订单项
   * 
   * @param orderItem
   *          购物项
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

  public ShopOrder() {
    // 因为SimpleDateFormat.format()方法不是线程安全的,所以为了避免多线程的问题不能将SimpleDateFormat作为类变量来调用‘format’方法
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
    this.orderNo = dateFormat.format(new Date());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(username);
    dest.writeString(orderNo);
    dest.writeParcelable(status, flags);
    dest.writeByte((byte) (fromPreOrder ? 1 : 0));
    dest.writeParcelable(sendType, flags);
    dest.writeLong(createTime != null ? createTime.getTime() : -1);
    dest.writeLong(finishTime != null ? finishTime.getTime() : -1);
    dest.writeLong(pickupTime != null ? pickupTime.getTime() : -1);
    dest.writeParcelable(storeAddress, flags);
    dest.writeParcelable(recieveAddress, flags);
    dest.writeParcelableArray(orderItemList.toArray(new ShopOrderItem[orderItemList.size()]), flags);
    dest.writeString(thirdsendCompany);
    dest.writeString(thirdsendNo);
    dest.writeStringList(operator);
    dest.writeLong(lastModify != null ? lastModify.getTime() : -1);
    dest.writeString(buyerMessage);
    dest.writeString(formid);
    dest.writeDouble(totalGoodsPrice);
    dest.writeDouble(sendPrice);
    dest.writeInt(score);
    dest.writeParcelableArray(orderVoucher.toArray(new ShopOrderVoucher[orderVoucher.size()]), flags);
    dest.writeDouble(totalPrice);
  }

  private ShopOrder(Parcel in) {
    id = in.readString();
    username = in.readString();
    orderNo = in.readString();
    status = in.readParcelable(ShopOrderStatus.class.getClassLoader());
    fromPreOrder = in.readByte() != 0;
    sendType = in.readParcelable(SendType.class.getClassLoader());
    long createTimeValue = in.readLong();
    if (createTimeValue != -1) {
      createTime = new Date(createTimeValue);
    }
    long finishTimeValue = in.readLong();
    if (finishTimeValue != -1) {
      finishTime = new Date(finishTimeValue);
    }
    long pickupTimeValue = in.readLong();
    if (pickupTimeValue != -1) {
      pickupTime = new Date(pickupTimeValue);
    }
    storeAddress = in.readParcelable(StoreAddress.class.getClassLoader());
    recieveAddress = in.readParcelable(RecieveAddress.class.getClassLoader());
    orderItemList = new ArrayList<>();
    for (Parcelable p : in.readParcelableArray(ShopOrderItem.class.getClassLoader())) {
      orderItemList.add((ShopOrderItem) p);
    }
    thirdsendCompany = in.readString();
    thirdsendNo = in.readString();
    in.readStringList(operator);
    long lastModifyValue = in.readLong();
    if (lastModifyValue != -1) {
      lastModify = new Date(lastModifyValue);
    }
    buyerMessage = in.readString();
    formid = in.readString();
    totalGoodsPrice = in.readDouble();
    sendPrice = in.readDouble();
    score = in.readInt();
    orderVoucher = new ArrayList<>();
    for (Parcelable p : in.readParcelableArray(ShopOrderVoucher.class.getClassLoader())) {
      orderVoucher.add((ShopOrderVoucher) p);
    }
    totalPrice = in.readDouble();
  }

  public static final Parcelable.Creator<ShopOrder> CREATOR = new Parcelable.Creator<ShopOrder>() {
    @Override
    public ShopOrder createFromParcel(Parcel in) {
      return new ShopOrder(in);
    }

    @Override
    public ShopOrder[] newArray(int size) {
      return new ShopOrder[size];
    }
  };
}
