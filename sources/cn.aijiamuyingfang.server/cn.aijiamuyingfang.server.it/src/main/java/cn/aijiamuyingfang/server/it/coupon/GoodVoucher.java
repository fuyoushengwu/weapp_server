package cn.aijiamuyingfang.server.it.coupon;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 购买商品可以获得的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:02:06
 */
@Entity
@Data
@NoArgsConstructor
public class GoodVoucher {
  /**
   * 兑换券-Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  private boolean deprecated;

  /**
   * 兑换券名称
   */
  private String name;

  /**
   * 兑换券可以兑换的项目
   */
  @ElementCollection
  private List<String> voucheritemIdList = new ArrayList<>();

  /**
   * 兑换券描述
   */
  private String description;

  /**
   * 兑换券中可用的兑换值
   */
  private int score;

  /**
   * 使用提供的GoodVoucher更新本商品兑换券信息
   * 
   * @param updateVoucher
   *          要更新的商品兑换信息
   */
  public void update(GoodVoucher updateVoucher) {
    if (null == updateVoucher) {
      return;
    }
    if (StringUtils.hasContent(updateVoucher.name)) {
      this.name = updateVoucher.name;
    }
    if (StringUtils.hasContent(updateVoucher.description)) {
      this.description = updateVoucher.description;
    }
    if (updateVoucher.score != 0) {
      this.score = updateVoucher.score;
    }
    if (!CollectionUtils.isEmpty(updateVoucher.voucheritemIdList)) {
      this.voucheritemIdList = updateVoucher.voucheritemIdList;
    }
  }

  public void addVoucheritemId(String voucheritemId) {
    if (StringUtils.hasContent(voucheritemId)) {
      this.voucheritemIdList.add(voucheritemId);
    }

  }
}
