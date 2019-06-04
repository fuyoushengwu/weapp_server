package cn.aijiamuyingfang.server.it.coupon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Entity
@Data
@NoArgsConstructor
public class VoucherItem {
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

  /**
   * 使用提供的VoucherItem更新本商品兑换券信息
   * 
   * @param updateVoucherItem
   *          要更新的兑换券信息
   */
  public void update(VoucherItem updateVoucherItem) {
    if (null == updateVoucherItem) {
      return;
    }
    if (StringUtils.hasContent(updateVoucherItem.name)) {
      this.name = updateVoucherItem.name;
    }
    if (StringUtils.hasContent(updateVoucherItem.description)) {
      this.description = updateVoucherItem.description;
    }
    if (StringUtils.hasContent(updateVoucherItem.goodId)) {
      this.goodId = updateVoucherItem.goodId;
    }
    if (updateVoucherItem.score > 0) {
      this.score = updateVoucherItem.score;
    }
  }
}
