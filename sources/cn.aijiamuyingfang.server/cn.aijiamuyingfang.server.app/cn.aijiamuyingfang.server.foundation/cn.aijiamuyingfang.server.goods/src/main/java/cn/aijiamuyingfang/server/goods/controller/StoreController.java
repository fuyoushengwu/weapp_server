package cn.aijiamuyingfang.server.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.domain.ImageSource;
import cn.aijiamuyingfang.server.goods.domain.Store;
import cn.aijiamuyingfang.server.goods.domain.StoreAddress;
import cn.aijiamuyingfang.server.goods.domain.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.server.goods.domain.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.server.goods.service.ImageService;
import cn.aijiamuyingfang.server.goods.service.StoreService;

/***
 * [描述]:*
 * <p>
 * *门店服务-控制层*
 * </p>
 * **
 * 
 * @version 1.0.0*@author ShiWei*@email shiweideyouxiang @sina.cn
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
   * @param currentpage
   *          当前页 默认值:1 (currentpage必须&ge;1,否则重置为1)
   * @param pagesize
   *          每页大小 默认值:10(pagesize必&gt;0,否则重置为1)
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store")
  public GetInUseStoreListResponse getInUseStoreList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam(value = "pagesize") int pagesize) {
    return storeService.getInUseStoreList(currentpage, pagesize);
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
    if (CollectionUtils.hasContent(detailImageParts)) {
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
   * @param storeid
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/{storeid}")
  public Store getStore(@PathVariable("storeid") String storeid) {
    Store store = storeService.getStore(storeid);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
    }
    return store;
  }

  /**
   * 获取门店地址信息
   * 
   * @param storeid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/store/{storeid}/address")
  public StoreAddress getStoreAddressByStoreId(@PathVariable("storeid") String storeid) {
    return getStore(storeid).getStoreAddress();
  }

  /**
   * 获取门店地址信息
   * 
   * @param addressid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/storeaddress/{addressid}")
  public StoreAddress getStoreAddressByAddressId(@PathVariable("addressid") String addressid) {
    return storeService.getStoreAddress(addressid);
  }

  /**
   * 更新门店信息
   *
   * @param storeid
   * @param storeRequest
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/store/{storeid}")
  public Store updateStore(@PathVariable("storeid") String storeid, @RequestBody Store storeRequest) {
    if (null == storeRequest) {
      throw new IllegalArgumentException("update store request body is null");
    }
    return storeService.updateStore(storeid, storeRequest);
  }

  /**
   * 废弃门店
   *
   * @param storeid
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/store/{storeid}")
  public void deprecateStore(@PathVariable("storeid") String storeid) {
    Store store = storeService.getStore(storeid);
    if (store != null) {
      storeService.deprecateStore(storeid);
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
  public GetDefaultStoreIdResponse getDefaultStoreId() {
    GetInUseStoreListResponse response = storeService.getInUseStoreList(1, 1);
    List<Store> storeList = response.getDataList();
    GetDefaultStoreIdResponse getDefaultStoreIdResponse = new GetDefaultStoreIdResponse();
    if (storeList != null && storeList.size() == 1) {
      getDefaultStoreIdResponse.setDefaultId(storeList.get(0).getId());
    }
    return getDefaultStoreIdResponse;
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
