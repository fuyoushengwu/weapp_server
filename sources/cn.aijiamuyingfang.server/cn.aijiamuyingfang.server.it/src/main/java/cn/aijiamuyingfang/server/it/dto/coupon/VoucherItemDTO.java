package cn.aijiamuyingfang.server.it.dto.coupon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 兑换券可以选择的兑换方式
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:54
 */
@Entity(name = "voucher_item")
@Data
public class VoucherItemDTO {
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 兑换项名称
   */
  private String name;

  /**
   * 兑换项描述
   */
  private String description;

  /**
   * 兑换券关联的商品ID
   */
  private String goodId;

  /**
   * 多少兑换值可以兑换商品
   */
  private int score;
}
