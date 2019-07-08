package cn.aijiamuyingfang.server.it.dto.shoporder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 订单使用的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:41:55
 */
@Entity
@Data
@NoArgsConstructor
public class ShopOrderVoucherDTO {
  /**
   * ID
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 订单使用的用户兑换券Id
   */
  private String uservoucherId;

  /**
   * 订单使用的兑换方式
   */
  private String voucherItemId;

  /**
   * 该兑换项关联的商品Id
   */
  private String goodId;
}
