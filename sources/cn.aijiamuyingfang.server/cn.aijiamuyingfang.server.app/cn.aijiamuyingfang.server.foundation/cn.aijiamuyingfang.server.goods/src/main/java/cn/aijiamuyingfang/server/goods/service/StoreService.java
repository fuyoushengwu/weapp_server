package cn.aijiamuyingfang.server.goods.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.City;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.db.StoreAddressRepository;
import cn.aijiamuyingfang.server.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.goods.domain.response.PagableStoreList;
import cn.aijiamuyingfang.server.goods.dto.Store;
import cn.aijiamuyingfang.server.goods.dto.StoreAddress;

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
   * @param currentPage
   *          当前页 (currentPage必须&ge;1,否则重置为1)
   * @param pageSize
   *          每页大小 (pageSize必须&gt;0,否则重置为1)
   * @return
   */
  public PagableStoreList getInUseStoreList(int currentPage, int pageSize) {
    // currentPage必须>=1,否则重置为1
    if (currentPage < 1) {
      currentPage = 1;
    }
    // pageSize必须>0,否则重置为1
    if (pageSize <= 0) {
      pageSize = 1;
    }

    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<Store> storePage = storeRepository.findInUseStores(pageRequest);
    PagableStoreList response = new PagableStoreList();
    response.setCurrentPage(storePage.getNumber() + 1);
    response.setDataList(storePage.getContent());
    response.setTotalpage(storePage.getTotalPages());
    return response;
  }

  /**
   * 新增门店信息
   * 
   * @param store
   */
  public Store createORUpdateStore(Store store) {
    if (null == store) {
      store = new Store();
    }
    if (StringUtils.hasContent(store.getId())) {
      Store oriStore = storeRepository.findOne(store.getId());
      if (oriStore != null) {
        oriStore.update(store);
        return storeRepository.saveAndFlush(oriStore);
      }
    }
    return storeRepository.saveAndFlush(store);
  }

  /**
   * 获取某个门店信息
   * 
   * @param storeId
   *          门店ID
   * @return
   */
  public Store getStore(String storeId) {
    return storeRepository.findOne(storeId);
  }

  /**
   * 获取某个门店地址信息
   * 
   * @param storeaddressId
   * @return
   */
  public StoreAddress getStoreAddress(String storeaddressId) {
    return storeaddressRepository.findOne(storeaddressId);
  }

  /**
   * 更新门店信息
   * 
   * @param storeId
   * @param updateStore
   */
  public Store updateStore(String storeId, Store updateStore) {
    Store store = storeRepository.findOne(storeId);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeId);
    }
    if (null == updateStore) {
      return store;
    }
    store.update(updateStore);
    return storeRepository.saveAndFlush(store);
  }

  /**
   * 废弃门店
   * 
   * @param storeId
   */
  public void deprecateStore(String storeId) {
    Store store = storeRepository.findOne(storeId);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeId);
    }
    store.setDeprecated(true);
    storeRepository.saveAndFlush(store);

    StoreAddress storeaddress = store.getStoreAddress();
    if (storeaddress != null) {
      storeaddress.setDeprecated(true);
      storeaddressRepository.saveAndFlush(storeaddress);
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
      StoreAddress storeaddress = store.getStoreAddress();
      if (null == storeaddress) {
        continue;
      }
      City city = storeaddress.getCity();
      if (city != null && StringUtils.hasContent(city.getName())) {
        storeCity.add(city.getName());
      }
    }
    return storeCity;
  }

}
