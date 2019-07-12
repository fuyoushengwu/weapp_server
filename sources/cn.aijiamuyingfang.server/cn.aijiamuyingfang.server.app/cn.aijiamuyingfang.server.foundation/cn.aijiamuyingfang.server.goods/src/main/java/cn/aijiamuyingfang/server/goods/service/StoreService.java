package cn.aijiamuyingfang.server.goods.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.dto.address.CityDTO;
import cn.aijiamuyingfang.server.goods.db.StoreAddressRepository;
import cn.aijiamuyingfang.server.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.goods.dto.StoreAddressDTO;
import cn.aijiamuyingfang.server.goods.dto.StoreDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.store.PagableStoreList;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.store.StoreAddress;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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
    Page<StoreDTO> storeDTOPage = storeRepository.findInUseStores(pageRequest);
    PagableStoreList response = new PagableStoreList();
    response.setCurrentPage(storeDTOPage.getNumber() + 1);
    response.setDataList(ConvertUtils.convertStoreDTOList(storeDTOPage.getContent()));
    response.setTotalpage(storeDTOPage.getTotalPages());
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
      StoreDTO oriStoreDTO = storeRepository.findOne(store.getId());
      if (oriStoreDTO != null) {
        oriStoreDTO.update(store);
        return ConvertUtils.convertStoreDTO(storeRepository.saveAndFlush(oriStoreDTO));
      }
    }
    return ConvertUtils.convertStoreDTO(storeRepository.saveAndFlush(ConvertUtils.convertStore(store)));
  }

  /**
   * 获取某个门店信息
   * 
   * @param storeId
   *          门店ID
   * @return
   */
  public Store getStore(String storeId) {
    return ConvertUtils.convertStoreDTO(storeRepository.findOne(storeId));
  }

  /**
   * 获取某个门店地址信息
   * 
   * @param storeaddressId
   * @return
   */
  public StoreAddress getStoreAddress(String storeaddressId) {
    return ConvertUtils.convertStoreAddressDTO(storeaddressRepository.findOne(storeaddressId));
  }

  /**
   * 更新门店信息
   * 
   * @param storeId
   * @param updateStore
   */
  public Store updateStore(String storeId, Store updateStore) {
    StoreDTO storeDTO = storeRepository.findOne(storeId);
    if (null == storeDTO) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeId);
    }
    if (null == updateStore) {
      return ConvertUtils.convertStoreDTO(storeDTO);
    }
    storeDTO.update(updateStore);
    return ConvertUtils.convertStoreDTO(storeRepository.saveAndFlush(storeDTO));
  }

  /**
   * 废弃门店
   * 
   * @param storeId
   */
  public void deprecateStore(String storeId) {
    StoreDTO storeDTO = storeRepository.findOne(storeId);
    if (null == storeDTO) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeId);
    }
    storeDTO.setDeprecated(true);
    storeRepository.saveAndFlush(storeDTO);

    StoreAddressDTO storeAddressDTO = storeDTO.getStoreAddress();
    if (storeAddressDTO != null) {
      storeAddressDTO.setDeprecated(true);
      storeaddressRepository.saveAndFlush(storeAddressDTO);
    }

  }

  /**
   * 获取在哪些城市有门店分布
   * 
   * @return
   */
  public Set<String> getStoresCity() {
    List<StoreDTO> storeDTOList = storeRepository.findInUseStores();
    Set<String> storeCity = new HashSet<>();
    for (StoreDTO storeDTO : storeDTOList) {
      StoreAddressDTO storeAddressDTO = storeDTO.getStoreAddress();
      if (null == storeAddressDTO) {
        continue;
      }
      CityDTO cityDTO = storeAddressDTO.getCity();
      if (cityDTO != null && StringUtils.hasContent(cityDTO.getName())) {
        storeCity.add(cityDTO.getName());
      }
    }
    return storeCity;
  }

}
