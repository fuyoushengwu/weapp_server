package cn.aijiamuyingfang.client.domain.shoporder.response;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderVoucher;
import lombok.Data;

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
@Data
public class GetShopOrderVoucherListResponse {
  private List<ShopOrderVoucher> voucherList = new ArrayList<>();
}
