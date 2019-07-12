package cn.aijiamuyingfang.server.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.goods.SaleGood;
import cn.aijiamuyingfang.vo.response.ResponseBean;

/**
 * [描述]:
 * <p>
 * 商品服务客户端
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-23 00:17:55
 */
@FeignClient(name = "goods-service")
public interface GoodClient {
  @GetMapping(value = "/good/{good_id}")
  ResponseBean<Good> getGood(@PathVariable(value = "good_id") String goodId);

  @PutMapping(value = "/good/{good_id}/sale")
  ResponseBean<Void> saleGood(@PathVariable(value = "good_id") String goodId, @RequestBody SaleGood saleGood);

  @PutMapping(value = "/good/sale")
  ResponseBean<Void> saleGoodList(@RequestBody List<SaleGood> saleGoodList);

  /**
   * 废弃商品兑换券
   * 
   * @param goodvoucherId
   */
  @PutMapping(value = "/good/goodvoucher/{good_voucher_id}")
  ResponseBean<Void> deprecateGoodVoucher(@PathVariable(value = "good_voucher_id") String goodvoucherId);
}
