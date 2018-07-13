package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.server.client.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.exception.GoodsException;
import cn.aijiamuyingfang.server.domain.goods.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.goods.GoodDetail;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.util.ConverterService;
import cn.aijiamuyingfang.server.goods.service.ClassifyGoodService;
import cn.aijiamuyingfang.server.goods.service.GoodDetailService;
import cn.aijiamuyingfang.server.goods.service.GoodService;
import cn.aijiamuyingfang.server.goods.service.ImageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
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

  @Autowired
  private ClassifyGoodService classifygoodService;

  @Autowired
  private GoodService goodService;

  @Autowired
  private GoodDetailService gooddetailService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ShopCartControllerClient shopcartControllerClient;

  @Autowired
  private ShopOrderControllerClient shoporderControllerClient;

  @Autowired
  private ConverterService converterService;

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
  @PreAuthorize(value = "isAuthenticated()")
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
   * @param goodRequest
   * @param request
   * @return
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping(value = "/good")
  public Good createGood(@RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
      @RequestParam(value = "detailImages", required = false) List<MultipartFile> detailImages, GoodRequest goodRequest,
      HttpServletRequest request) {
    if (null == goodRequest) {
      throw new GoodsException("400", "good request body is null");
    }
    if (StringUtils.isEmpty(goodRequest.getName())) {
      throw new GoodsException("400", "good name is empty");
    }
    if (StringUtils.isEmpty(goodRequest.getBarcode())) {
      throw new GoodsException("400", "good barcode is empty");
    }
    if (goodRequest.getPrice() == 0) {
      throw new GoodsException("400", "good price  is 0");
    }
    Good good = converterService.from(goodRequest);
    goodService.createGood(good);

    String coverImgUrl = imageService.saveGoodLogo(good.getId(), coverImage);
    if (StringUtils.hasContent(coverImgUrl)) {
      coverImgUrl = String.format("http://%s:%s/%s", request.getServerName(), request.getServerPort(), coverImgUrl);
      good.setCoverImg(coverImgUrl);
    }

    imageService.clearGoodDetailImgs(good.getId());
    List<String> detailImgList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(detailImages)) {
      for (MultipartFile img : detailImages) {
        String detailImgUrl = imageService.saveGoodDetailImg(good.getId(), img);
        if (StringUtils.hasContent(detailImgUrl)) {
          detailImgUrl = String.format("http://%s:%s/%s", request.getServerName(), request.getServerPort(),
              detailImgUrl);
          detailImgList.add(detailImgUrl);
        }
      }
    }

    GoodDetail goodDetail = new GoodDetail();
    goodDetail.setId(good.getId());
    goodDetail.setLifetime(goodRequest.getLifetime());
    goodDetail.setDetailImgList(detailImgList);

    gooddetailService.createGoodDetail(goodDetail);
    return good;
  }

  /**
   * 获取商品信息
   * 
   * @param goodid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
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
   * @param token
   * @param goodid
   * @throws IOException
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @DeleteMapping(value = "/good/{goodid}")
  public void deprecateGood(@RequestHeader(AuthConstants.HEADER_STRING) String token,
      @PathVariable(value = "goodid") String goodid) throws IOException {
    goodService.deprecateGood(goodid);
    classifygoodService.removeClassifyGood(goodid);
    shopcartControllerClient.deleteGood(token, goodid, true);
  }

  /**
   * 获取商品详细信息
   * 
   * @param goodid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
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
   * @param token
   * @param goodid
   * @param request
   * @return
   * @throws IOException
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PutMapping(value = "/good/{goodid}")
  public Good updateGood(@RequestHeader(AuthConstants.HEADER_STRING) String token,
      @PathVariable(value = "goodid") String goodid, @RequestBody GoodRequest request) throws IOException {
    if (null == request) {
      throw new GoodsException("400", "update good request body is null");
    }
    Good good = goodService.updateGood(goodid, converterService.from(request));
    if (good != null) {
      request.setId(good.getId());
      shoporderControllerClient.updatePreOrder(token, request, true);
    }
    return good;
  }
}
