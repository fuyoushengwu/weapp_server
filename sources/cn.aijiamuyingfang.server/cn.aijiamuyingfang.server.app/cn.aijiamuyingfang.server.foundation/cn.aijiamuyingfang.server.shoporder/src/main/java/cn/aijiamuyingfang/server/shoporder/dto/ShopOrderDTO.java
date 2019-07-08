package cn.aijiamuyingfang.server.shoporder.dto;

import java.io.IOException;
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
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.SendType;
import cn.aijiamuyingfang.server.domain.ShopOrderStatus;
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
@Entity
@Data
public class ShopOrderDTO {

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
  private String username;

  /**
   * 订单号
   */
  private String orderNo;

  /**
   * 订单状态:0:预订;1:未开始;2:进行中;3:已完成;4:订单超时
   */
  @JsonDeserialize(using = StatusDeserializer.class)
  private ShopOrderStatus status;

  private static class StatusDeserializer extends JsonDeserializer<ShopOrderStatus> {

    @Override
    public ShopOrderStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return ShopOrderStatus.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return ShopOrderStatus.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return ShopOrderStatus.UNKNOW;
    }

  }

  /**
   * 订单是否来自一个预约单
   */
  private boolean fromPreOrder;

  /**
   * 订单的配送方式: 0:到店自取(pickup); 1:送货上门(ownsend); 2:快递(thirdsend);
   */
  @JsonDeserialize(using = SendTypeDeserializer.class)
  private SendType sendType;

  private static class SendTypeDeserializer extends JsonDeserializer<SendType> {

    @Override
    public SendType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return SendType.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return SendType.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return SendType.UNKNOW;
    }

  }

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
  @OneToMany(cascade = CascadeType.ALL)
  private List<ShopOrderItemDTO> orderItemList = new ArrayList<>();

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
  @ManyToMany(cascade = CascadeType.ALL)
  private List<ShopOrderVoucherDTO> orderVoucher = new ArrayList<>();

  /**
   * 订单总额
   */
  private double totalPrice;

  /**
   * 
   * @return 距离最后修改日期(单位:天)
   */
  public int getLastModifyTime() {
    Date now = new Date();
    return (int) ((now.getTime() - lastModify.getTime()) / 1000 / 60 / 60 / 24);
  }

  /**
   * 添加订单项
   * 
   * @param orderItem
   *          购物项
   */
  public void addOrderItem(ShopOrderItemDTO orderItem) {
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

  public void setStatus(ShopOrderStatus status) {
    this.status = status;
    this.lastModify = new Date();
  }

  public ShopOrderDTO() {
    // 因为SimpleDateFormat.format()方法不是线程安全的,所以为了避免多线程的问题不能将SimpleDateFormat作为类变量来调用‘format’方法
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
    this.orderNo = dateFormat.format(new Date());
  }
}
