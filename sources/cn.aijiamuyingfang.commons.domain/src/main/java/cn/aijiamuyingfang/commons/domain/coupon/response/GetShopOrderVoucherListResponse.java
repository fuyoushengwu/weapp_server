package cn.aijiamuyingfang.commons.domain.coupon.response;

import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderVoucher;
import java.util.ArrayList;
import java.util.List;

/**
 * [描述]:
 * <p>
 * 购买商品时可用的兑换券
 * </p>
 *
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-13 17:41:34
 */
public class GetShopOrderVoucherListResponse {

  private List<ShopOrderVoucher> voucherList = new ArrayList<>();

  public List<ShopOrderVoucher> getVoucherList() {
    return voucherList;
  }

  /**
   * 添加 购买商品时可用的兑换券
   * 
   * @param voucher
   */
  public void addUsefulHolderCart(ShopOrderVoucher voucher) {
    this.voucherList.add(voucher);
  }

}
