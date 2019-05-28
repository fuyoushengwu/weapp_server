package cn.aijiamuyingfang.client.domain.coupon;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.client.rest.utils.StringUtils;
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
@Data
public class GoodVoucher {
  /**
   * 兑换券-Id
   */
  private String id;

  private boolean deprecated;

  /**
   * 兑换券名称
   */
  private String name;

  /**
   * 兑换券可以兑换的项目
   */
  private List<String> voucheritemIdList = new ArrayList<>();

  /**
   * 兑换券描述
   */
  private String description;

  /**
   * 兑换券中可用的兑换值
   */
  private int score;

  public void addVoucheritemId(String voucheritemId) {
    if (StringUtils.hasContent(voucheritemId)) {
      this.voucheritemIdList.add(voucheritemId);
    }
  }
}