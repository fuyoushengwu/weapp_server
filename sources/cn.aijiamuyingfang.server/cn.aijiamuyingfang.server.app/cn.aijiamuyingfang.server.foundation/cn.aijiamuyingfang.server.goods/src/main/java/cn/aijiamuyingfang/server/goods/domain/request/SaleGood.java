package cn.aijiamuyingfang.server.goods.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 贩卖商品描述
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-25 05:54:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleGood {
  private String goodId;

  private int salecount;
}
