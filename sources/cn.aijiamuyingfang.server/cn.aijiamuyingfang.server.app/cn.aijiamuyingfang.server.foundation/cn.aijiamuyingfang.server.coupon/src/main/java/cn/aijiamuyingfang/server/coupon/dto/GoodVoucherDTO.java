package cn.aijiamuyingfang.server.coupon.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.utils.CollectionUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;

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
@Entity(name = "good_voucher")
@Data
public class GoodVoucherDTO {
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
  private List<String> voucherItemIdList = new ArrayList<>();

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
    if (StringUtils.hasContent(updateVoucher.getName())) {
      this.setName(updateVoucher.getName());
    }
    if (StringUtils.hasContent(updateVoucher.getDescription())) {
      this.setDescription(updateVoucher.getDescription());
    }
    if (updateVoucher.getScore() != 0) {
      this.setScore(updateVoucher.getScore());
    }

    if (CollectionUtils.hasContent(updateVoucher.getVoucherItemList())) {
      this.voucherItemIdList = new ArrayList<>();
      for (VoucherItem voucherItem : updateVoucher.getVoucherItemList()) {
        if (voucherItem != null) {
          this.voucherItemIdList.add(voucherItem.getId());
        }
      }
    }
  }
}
