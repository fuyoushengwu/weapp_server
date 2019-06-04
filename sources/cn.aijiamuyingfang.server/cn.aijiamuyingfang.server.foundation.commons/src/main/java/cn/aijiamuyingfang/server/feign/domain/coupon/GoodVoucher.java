package cn.aijiamuyingfang.server.feign.domain.coupon;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;

import lombok.Data;

@Data
public class GoodVoucher {
  private String id;

  /**
   * 兑换券可以兑换的项目
   */
  @ElementCollection
  private List<String> voucherItemIdList = new ArrayList<>();

  /**
   * 兑换券中可用的兑换值
   */
  private int score;
}
