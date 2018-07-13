package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.domain.address.StoreAddress;
import cn.aijiamuyingfang.server.domain.address.db.StoreAddressRepository;
import cn.aijiamuyingfang.server.domain.exception.GoodsException;
import cn.aijiamuyingfang.server.domain.goods.GetInUseStoreListResponse;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 门店服务Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:50:27
 */
@Service
public class StoreService {
  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private StoreAddressRepository storeaddressRepository;

  /**
   * 分页获取在使用中的Store
   * 
   * @param currentpage
   *          当前页 (currentpage必须>=1,否则重置为1)
   * @param pagesize
   *          每页大小 (pagesize必须>0,否则重置为1)
   * @return
   */
  public GetInUseStoreListResponse getInUseStoreList(int currentpage, int pagesize) {
    // currentpage必须>=1,否则重置为1
    if (currentpage < 1) {
      currentpage = 1;
    }
    // pagesize必须>0,否则重置为1
    if (pagesize <= 0) {
      pagesize = 1;
    }

    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<Store> storePage = storeRepository.findInUseStores(pageRequest);
    GetInUseStoreListResponse response = new GetInUseStoreListResponse();
    response.setCurrentpage(storePage.getNumber() + 1);
    response.setDataList(storePage.getContent());
    response.setTotalpage(storePage.getTotalPages());
    return response;
  }

  /**
   * 新增门店信息
   * 
   * @param store
   */
  public void createStore(Store store) {
    if (null == store) {
      return;
    }
    storeRepository.save(store);
  }

  /**
   * 获取某个门店信息
   * 
   * @param storeid
   *          门店ID
   * @return
   */
  public Store getStore(String storeid) {
    return storeRepository.findOne(storeid);
  }

  /**
   * 更新门店信息
   * 
   * @param storeid
   * @param store
   */
  public Store updateStore(String storeid, Store updateStore) {
    Store store = storeRepository.findOne(storeid);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
    }
    if (null == updateStore) {
      return store;
    }
    store.update(updateStore);
    storeRepository.save(store);
    return store;
  }

  /**
   * 废弃门店
   * 
   * @param storeid
   */
  public void deprecateStore(String storeid) {
    Store store = storeRepository.findOne(storeid);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
    }
    store.setDeprecated(true);
    storeRepository.save(store);

    StoreAddress storeaddress = store.getStoreAddress();
    if (storeaddress != null) {
      storeaddress.setDeprecated(true);
      storeaddressRepository.save(storeaddress);
    }

  }

  /**
   * 获取在哪些城市有门店分布
   * 
   * @return
   */
  public Set<String> getStoresCity() {
    List<Store> stores = storeRepository.findInUseStores();
    Set<String> storeCity = new HashSet<>();
    for (Store store : stores) {
      storeCity.add(store.getStoreAddress().getCity().getName());
    }
    return storeCity;
  }

}
