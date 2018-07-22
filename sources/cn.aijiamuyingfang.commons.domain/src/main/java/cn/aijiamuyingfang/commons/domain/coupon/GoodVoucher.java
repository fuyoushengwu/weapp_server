package cn.aijiamuyingfang.commons.domain.coupon;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

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
public class GoodVoucher extends GoodVoucherRequest {
  /**
   * 兑换券-Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  private boolean deprecated;

  /**
   * 使用提供的GoodVoucher更新本商品兑换券信息
   * 
   * @param updateVoucher
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

}
