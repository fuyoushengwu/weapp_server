package cn.aijiamuyingfang.server.feign;

import java.util.concurrent.Future;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import cn.aijiamuyingfang.vo.response.ResponseBean;

@FeignClient(name = "shoporder-service")
public interface ShopOrderClient {
  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodId
   * @return
   */
  @PutMapping(value = "/shoporder/preorder/good/{good_id}")
  Future<ResponseBean<Void>> updatePreOrder(@PathVariable("good_id") String goodId);
}
