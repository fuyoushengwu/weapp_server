package cn.aijiamuyingfang.server.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import cn.aijiamuyingfang.server.feign.ShopCartClient;
import cn.aijiamuyingfang.server.feign.ShopOrderClient;
import cn.aijiamuyingfang.server.goods.domain.Good;
import cn.aijiamuyingfang.server.goods.domain.GoodDetail;
import cn.aijiamuyingfang.server.goods.domain.ImageSource;
import cn.aijiamuyingfang.server.goods.domain.request.SaleGood;
import cn.aijiamuyingfang.server.goods.domain.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.server.goods.service.ClassifyGoodService;
import cn.aijiamuyingfang.server.goods.service.GoodDetailService;
import cn.aijiamuyingfang.server.goods.service.GoodService;
import cn.aijiamuyingfang.server.goods.service.ImageService;

/***
 * [描述]:*
 * <p>
 * *商品服务-控制层*
 * </p>
 * **
 * 
 * @version 1.0.0*@author ShiWei*@email shiweideyouxiang @sina.cn
 * @date 2018-06-26 23:43:55
 */
@RestController
public class GoodController {

  @Autowired
  private ClassifyGoodService classifygoodService;

  @Autowired
  private GoodService goodService;

  @Autowired
  private GoodDetailService gooddetailService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ShopCartClient shopcartClient;

  @Autowired
  private ShopOrderClient shoporderClient;

  /**
   * 分页查询条目下的商品
   *
   * @param classifyid
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classifyid}/good")
  public GetClassifyGoodListResponse getClassifyGoodList(@PathVariable(value = "classifyid") String classifyid,
      @RequestParam(value = "packFilter", required = false) List<String> packFilter,
      @RequestParam(value = "levelFilter", required = false) List<String> levelFilter,
      @RequestParam(value = "orderType", required = false) String orderType,
      @RequestParam(value = "orderValue", required = false) String orderValue,
      @RequestParam(value = "currentpage") int currentpage, @RequestParam(value = "pagesize") int pagesize) {
    return classifygoodService.getClassifyGoodList(classifyid, packFilter, levelFilter, orderType, orderValue,
        currentpage, pagesize);
  }

  /**
   * 添加商品
   *
   * @param coverImage
   * @param detailImages
   * @param good
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/good")
  public Good createGood(@RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
      @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages, Good good,
      HttpServletRequest request) {
    if (null == good) {
      throw new IllegalArgumentException("good request body is null");
    }
    if (StringUtils.isEmpty(good.getName())) {
      throw new IllegalArgumentException("good name is empty");
    }
    if (StringUtils.isEmpty(good.getBarcode())) {
      throw new IllegalArgumentException("good barcode is empty");
    }
    if (good.getPrice() == 0) {
      throw new IllegalArgumentException("good price is 0");
    }
    good = goodService.createORUpdateGood(good);

    ImageSource coverImageSource = imageService.saveImage(coverImage);
    if (coverImageSource != null) {
      good.setCoverImg(coverImageSource);
    }

    List<ImageSource> detailImageSourceList = new ArrayList<>();
    if (CollectionUtils.hasContent(detailImages)) {
      for (MultipartFile detailImagePart : detailImages) {
        ImageSource detailImageSource = imageService.saveImage(detailImagePart);
        detailImageSourceList.add(detailImageSource);
      }
    }

    GoodDetail goodDetail = new GoodDetail();
    goodDetail.setId(good.getId());
    goodDetail.setLifetime(good.getLifetime());
    goodDetail.setDetailImgList(detailImageSourceList);
    gooddetailService.createORUpdateGoodDetail(goodDetail);
    return goodService.createORUpdateGood(good);
  }

  /**
   * 获取商品信息
   *
   * @param goodid
   *          商品id
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/good/{goodid}")
  public Good getGood(@PathVariable(value = "goodid") String goodid) {
    Good good = goodService.getGood(goodid);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodid);
    }
    return good;
  }

  /**
   * 废弃商品
   * 
   * @param goodid
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/good/{goodid}")
  public void deprecateGood(@PathVariable(value = "goodid") String goodid) {
    goodService.deprecateGood(goodid);
    classifygoodService.removeClassifyGood(goodid);
    shopcartClient.deleteGood(goodid);
  }

  /**
   * 获取商品详细信息
   *
   * @param goodid
   *          商品id
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/good/{goodid}/detail")
  public GoodDetail getGoodDetail(@PathVariable(value = "goodid") String goodid) {
    GoodDetail goodDetail = gooddetailService.getGoodDetail(goodid);
    if (null == goodDetail) {
      throw new GoodsException(ResponseCode.GOODDETAIL_NOT_EXIST, goodid);
    }
    return goodDetail;
  }

  /**
   * 更新Good信息
   * 
   * @param goodid
   *          商品id
   * @param request
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/good/{goodid}")
  public Good updateGood(@PathVariable(value = "goodid") String goodid, @RequestBody Good request)
      throws InterruptedException, ExecutionException {
    if (null == request) {
      throw new IllegalArgumentException("request body is null");
    }
    Good good = goodService.updateGood(goodid, request);
    if (good != null) {
      shoporderClient.updatePreOrder(goodid);
    }
    return good;
  }

  /**
   * 更新Good信息
   * 
   * @param request
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/good")
  public void updateGood(@RequestBody List<Good> request) {
    if (null == request) {
      throw new IllegalArgumentException("request body is null");
    }
    for (Good good : request) {
      if (null == good || StringUtils.isEmpty(good.getId())) {
        continue;
      }
      goodService.updateGood(good.getId(), good);
      shoporderClient.updatePreOrder(good.getId());
    }
  }

  /**
   * 售卖商品
   * 
   * @param goodid
   * @param saleGood
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/good/{goodid}/sale")
  public void saleGood(@PathVariable(value = "goodid") String goodid, @RequestBody SaleGood saleGood) {
    if (null == saleGood) {
      throw new IllegalArgumentException("request body is null");
    }
    goodService.saleGood(goodid, saleGood);
  }

  /**
   * 售卖商品
   * 
   * @param saleGoodList
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/good/sale")
  public void saleGoodList(@RequestBody List<SaleGood> saleGoodList) {
    if (null == saleGoodList) {
      throw new IllegalArgumentException("request body is null");
    }
    goodService.saleGoodList(saleGoodList);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param goodvoucherId
   */
  @PreAuthorize(value = "hasAuthority('permission:manager:*')")
  @PutMapping(value = "/good/goodvoucher/{good_voucher_id}")
  public void deprecateGoodVoucher(@PathVariable(value = "good_voucher_id") String goodvoucherId) {
    goodService.deprecateGoodVoucher(goodvoucherId);
  }
}
