package cn.aijiamuyingfang.server.goods.controller;

import java.util.ArrayList;
import java.util.List;

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
import cn.aijiamuyingfang.server.feign.ShopCartClient;
import cn.aijiamuyingfang.server.feign.ShopOrderClient;
import cn.aijiamuyingfang.server.goods.service.ClassifyGoodService;
import cn.aijiamuyingfang.server.goods.service.GoodDetailService;
import cn.aijiamuyingfang.server.goods.service.GoodService;
import cn.aijiamuyingfang.server.goods.service.ImageService;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.goods.GoodDetail;
import cn.aijiamuyingfang.vo.goods.PagableGoodList;
import cn.aijiamuyingfang.vo.goods.SaleGood;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.CollectionUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;

/***
 * [描述]:
 * <p>
 * 商品服务-控制层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:43:55
 */
@RestController
public class GoodController {

  /**
   * request body is null
   */
  private static final String REQUEST_BODY_NULL = "request body is null";

  @Autowired
  private ClassifyGoodService classifygoodService;

  @Autowired
  private GoodService goodService;

  @Autowired
  private GoodDetailService gooddetailService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ShopCartClient shopCartClient;

  @Autowired
  private ShopOrderClient shoporderClient;

  /**
   * 分页查询条目下的商品
   *
   * @param classifyId
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classify_id}/good")
  public PagableGoodList getClassifyGoodList(@PathVariable(value = "classify_id") String classifyId,
      @RequestParam(value = "packFilter", required = false) List<String> packFilter,
      @RequestParam(value = "levelFilter", required = false) List<String> levelFilter,
      @RequestParam(value = "orderType", required = false) String orderType,
      @RequestParam(value = "orderValue", required = false) String orderValue,
      @RequestParam(value = "current_page") int currentPage, @RequestParam(value = "page_size") int pageSize) {
    return classifygoodService.getClassifyGoodList(classifyId, packFilter, levelFilter, orderType, orderValue,
        currentPage, pageSize);
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
      throw new IllegalArgumentException(REQUEST_BODY_NULL);
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
   * @param goodId
   *          商品id
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/good/{good_id}")
  public Good getGood(@PathVariable(value = "good_id") String goodId) {
    Good good = goodService.getGood(goodId);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    return good;
  }

  /**
   * 获取商品信息
   * 
   * @param goodIdList
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/good")
  public List<Good> getGoodList(@RequestParam("good_id") List<String> goodIdList) {
    return goodService.getGoodList(goodIdList);
  }

  /**
   * 废弃商品
   * 
   * @param goodId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/good/{good_id}")
  public void deprecateGood(@PathVariable(value = "good_id") String goodId) {
    goodService.deprecateGood(goodId);
    classifygoodService.removeClassifyGood(goodId);
    shopCartClient.deleteGood(goodId);
  }

  /**
   * 获取商品详细信息
   *
   * @param goodId
   *          商品id
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/good/{good_id}/detail")
  public GoodDetail getGoodDetail(@PathVariable(value = "good_id") String goodId) {
    GoodDetail goodDetail = gooddetailService.getGoodDetail(goodId);
    if (null == goodDetail) {
      throw new GoodsException(ResponseCode.GOODDETAIL_NOT_EXIST, goodId);
    }
    return goodDetail;
  }

  /**
   * 更新Good信息
   * 
   * @param goodId
   *          商品id
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/good/{good_id}")
  public Good updateGood(@PathVariable(value = "good_id") String goodId, @RequestBody Good request) {
    if (null == request) {
      throw new IllegalArgumentException(REQUEST_BODY_NULL);
    }
    Good good = goodService.updateGood(goodId, request);
    if (good != null) {
      shoporderClient.updatePreOrder(goodId);
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
      throw new IllegalArgumentException(REQUEST_BODY_NULL);
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
   * @param goodId
   * @param saleGood
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/good/{good_id}/sale")
  public void saleGood(@PathVariable(value = "good_id") String goodId, @RequestBody SaleGood saleGood) {
    if (null == saleGood) {
      throw new IllegalArgumentException(REQUEST_BODY_NULL);
    }
    goodService.saleGood(goodId, saleGood);
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
      throw new IllegalArgumentException(REQUEST_BODY_NULL);
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
