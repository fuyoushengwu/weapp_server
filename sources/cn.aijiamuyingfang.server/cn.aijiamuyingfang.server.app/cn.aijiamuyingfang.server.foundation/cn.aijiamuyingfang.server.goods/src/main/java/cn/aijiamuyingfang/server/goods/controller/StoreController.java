package cn.aijiamuyingfang.server.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cn.aijiamuyingfang.server.goods.service.ImageService;
import cn.aijiamuyingfang.server.goods.service.StoreService;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.store.PagableStoreList;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.store.StoreAddress;
import cn.aijiamuyingfang.vo.utils.StringUtils;

/***
 * [描述]:
 * <p>
 * 门店服务-控制层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:43:33
 */
@RestController
public class StoreController {

  @Autowired
  private StoreService storeService;

  @Autowired
  private ImageService imageService;

  /**
   * 分页获取在使用中的Store
   *
   * @param currentPage
   *          当前页 默认值:1 (currentPage必须&ge;1,否则重置为1)
   * @param pageSize
   *          每页大小 默认值:10(pageSize必&gt;0,否则重置为1)
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store")
  public PagableStoreList getInUseStoreList(@RequestParam(value = "current_page") int currentPage,
      @RequestParam(value = "page_size") int pageSize) {
    return storeService.getInUseStoreList(currentPage, pageSize);
  }

  /**
   * 创建门店
   *
   * @param storeRequest
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/store")
  public Store createStore(@RequestParam(value = "coverImage", required = false) MultipartFile coverImagePart,
      @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImageParts, Store storeRequest,
      HttpServletRequest request) {
    if (null == storeRequest) {
      throw new IllegalArgumentException("store request body is null");
    }
    if (StringUtils.isEmpty(storeRequest.getName())) {
      throw new IllegalArgumentException("store name is empty");
    }
    Store store = storeService.createORUpdateStore(storeRequest);
    ImageSource imageSource = imageService.saveImage(coverImagePart);
    if (imageSource != null) {
      store.setCoverImg(imageSource);
    }

    List<ImageSource> detailImageSourceList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(detailImageParts)) {
      for (MultipartFile detailImagePart : detailImageParts) {
        ImageSource detailImageSource = imageService.saveImage(detailImagePart);
        if (detailImageSource != null) {
          detailImageSourceList.add(detailImageSource);
        }
      }
    }
    store.setDetailImgList(detailImageSourceList);
    return storeService.updateStore(store.getId(), store);

  }

  /**
   * 获取门店信息
   *
   * @param storeId
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/{store_id}")
  public Store getStore(@PathVariable("store_id") String storeId) {
    Store store = storeService.getStore(storeId);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeId);
    }
    return store;
  }

  /**
   * 获取门店地址信息
   * 
   * @param storeId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/{store_id}/address")
  public StoreAddress getStoreAddressByStoreId(@PathVariable("store_id") String storeId) {
    return getStore(storeId).getStoreAddress();
  }

  /**
   * 获取门店地址信息
   * 
   * @param addressId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/storeaddress/{address_id}")
  public StoreAddress getStoreAddressByAddressId(@PathVariable("address_id") String addressId) {
    return storeService.getStoreAddress(addressId);
  }

  /**
   * 更新门店信息
   *
   * @param storeId
   * @param storeRequest
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/store/{store_id}")
  public Store updateStore(@PathVariable("store_id") String storeId, @RequestBody Store storeRequest) {
    if (null == storeRequest) {
      throw new IllegalArgumentException("update store request body is null");
    }
    return storeService.updateStore(storeId, storeRequest);
  }

  /**
   * 废弃门店
   *
   * @param storeId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/store/{store_id}")
  public void deprecateStore(@PathVariable("store_id") String storeId) {
    Store store = storeService.getStore(storeId);
    if (store != null) {
      storeService.deprecateStore(storeId);
      imageService.deleteImage(store.getCoverImg());
      imageService.deleteImage(store.getDetailImgList());
    }
  }

  /**
   * 获取默认门店Id
   *
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/defaultid")
  public String getDefaultStoreId() {
    PagableStoreList response = storeService.getInUseStoreList(1, 1);
    List<Store> storeList = response.getDataList();
    if (storeList != null && storeList.size() == 1) {
      return storeList.get(0).getId();
    }
    return null;
  }

  /**
   * 获取在哪些城市有门店分布
   *
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/city")
  public Set<String> getStoresCity() {
    return storeService.getStoresCity();
  }
}
