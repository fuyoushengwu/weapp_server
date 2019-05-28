package cn.aijiamuyingfang.server.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.store.Store;
import cn.aijiamuyingfang.server.feign.domain.store.StoreAddress;

/**
 * [描述]:
 * <p>
 * 门店服务的客户端
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-23 20:13:10
 */
@FeignClient(name = "goods-service")
public interface StoreClient {
  @GetMapping(value = "/store/{storeid}")
  ResponseBean<Store> getStore(@PathVariable("storeid") String storeid);

  @GetMapping(value = "/storeaddress/{addressid}")
  ResponseBean<StoreAddress> getStoreAddressByAddressId(@PathVariable("addressid") String addressid);
}
