package cn.aijiamuyingfang.server.domain.coupon;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * <code>POST '/coupon/goodvoucher'</code>方法的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 19:39:51
 */
@MappedSuperclass
public class GoodVoucherRequest {
  /**
   * 兑换券名称
   */
  protected String name;

  /**
   * 兑换券可以兑换的项目
   */
  @ElementCollection
  protected List<String> voucheritemIdList = new ArrayList<>();

  /**
   * 兑换券描述
   */
  protected String description;

  /**
   * 兑换券中可用的兑换值
   */
  protected int score;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getVoucheritemIdList() {
    return voucheritemIdList;
  }

  public void setVoucheritemIdList(List<String> voucheritemIdList) {
    this.voucheritemIdList = voucheritemIdList;
  }

  public void addVoucheritemId(String voucheritemId) {
    if (StringUtils.hasContent(voucheritemId)) {
      this.voucheritemIdList.add(voucheritemId);
    }

  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

}
