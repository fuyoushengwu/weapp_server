package cn.aijiamuyingfang.server.feign;

import java.util.concurrent.Future;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;

/**
 * [描述]:
 * <p>
 * ShopCartController客户端
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-17 23:40:43
 */
@FeignClient(name = "shoporder-service")
public interface ShopCartClient {
  /**
   * 删除商品
   * 
   * @param goodid
   * @return
   */
  @DeleteMapping(value = "/shopcart/good/{goodid}")
  Future<ResponseBean<Void>> deleteGood(@PathVariable("goodid") String goodid);
}
