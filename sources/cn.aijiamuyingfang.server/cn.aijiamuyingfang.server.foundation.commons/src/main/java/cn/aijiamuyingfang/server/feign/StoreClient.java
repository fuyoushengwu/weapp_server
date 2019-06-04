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
  /**
   * 获取门店
   * 
   * @param storeId
   * @return
   */
  @GetMapping(value = "/store/{store_id}")
  ResponseBean<Store> getStore(@PathVariable("store_id") String storeId);

  /**
   * 获取门店地址
   * 
   * @param addressId
   * @return
   */
  @GetMapping(value = "/storeaddress/{address_id}")
  ResponseBean<StoreAddress> getStoreAddressByAddressId(@PathVariable("address_id") String addressId);
}
