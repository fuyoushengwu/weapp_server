package cn.aijiamuyingfang.server.shoporder.dto;

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

import cn.aijiamuyingfang.vo.utils.StringUtils;
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
@Entity(name = "shop_order")
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
  private ShopOrderStatusDTO status;

  /**
   * 订单是否来自一个预约单
   */
  private boolean fromPreOrder;

  /**
   * 订单的配送方式: 0:到店自取(pickup); 1:送货上门(ownsend); 2:快递(thirdsend);
   */
  private SendTypeDTO sendType;

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
   * 订单总额
   */
  private double totalPrice;

  /**
   * 订单使用的兑换券
   */
  @ManyToMany(cascade = CascadeType.ALL)
  private List<ShopOrderVoucherDTO> orderVoucher = new ArrayList<>();

  /**
   * 
   * @return 距离最后修改日期(单位:天)
   */
  public int getLastModifyTime() {
    Date now = new Date();
    return (int) ((now.getTime() - lastModify.getTime()) / 1000 / 60 / 60 / 24);
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
}
