package cn.aijiamuyingfang.server.coupon.controller;

import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.CouponException;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.coupon.service.CouponService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [描述]:
 * <p>
 * 优惠券模块控制层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 01:32:33
 */
@RestController
public class CouponController {
  @Autowired
  private CouponService couponService;

  /**
   * 分页获取用户兑换券
   * 
   * @param headerUserId
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/coupon/uservoucher")
  public GetUserVoucherListResponse getUserVoucherList(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestParam("currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's vouchers");
    }
    return couponService.getUserVoucherList(userid, currentpage, pagesize);
  }

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param headerUserId
   * @param userid
   * @param goodids
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/coupon/shoporder")
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestParam(name = "goodids", required = false) List<String> goodids) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's shoporder useable vouchers");
    }
    return couponService.getUserShopOrderVoucherList(userid, goodids);
  }

  /**
   * 分页获取商品兑换项
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/goodvoucher")
  public GetGoodVoucherListResponse getGoodVoucherList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize) {
    return couponService.getGoodVoucherList(currentpage, pagesize);
  }

  /**
   * 获取商品兑换项
   * 
   * @param voucherid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/goodvoucher/{voucherid}")
  public GoodVoucher getGoodVoucher(@PathVariable("voucherid") String voucherid) {
    return couponService.getGoodVoucher(voucherid);
  }

  /**
   * 创建商品兑换券
   * 
   * @param request
   * @return
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping(value = "/coupon/goodvoucher")
  public GoodVoucher createGoodVoucher(@RequestBody GoodVoucher request) {
    if (null == request) {
      throw new CouponException("400", "goodvoucher request  body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new CouponException("400", "good voucher name is empyt");
    }
    if (request.getScore() == 0) {
      throw new CouponException("400", "good voucher score is 0");
    }
    if (CollectionUtils.isEmpty(request.getVoucheritemIdList())) {
      throw new CouponException("400", "good voucher items is empyt");
    }
    return couponService.createGoodVoucher(request);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherid
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @DeleteMapping(value = "/coupon/goodvoucher/{voucherid}")
  public void deprecateGoodVoucher(@PathVariable("voucherid") String voucherid) {
    couponService.deprecateGoodVoucher(voucherid);
  }

  /**
   * 分页获取可选的兑换方式
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/voucheritem")
  public GetVoucherItemListResponse getVoucherItemList(@RequestParam("currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize) {
    return couponService.getVoucherItemList(currentpage, pagesize);
  }

  /**
   * 获取兑换方式
   * 
   * @param itemid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/voucheritem/{itemid}")
  public VoucherItem getVoucherItem(@PathVariable("itemid") String itemid) {
    return couponService.getVoucherItem(itemid);
  }

  /**
   * 创建兑换选择项
   * 
   * @param request
   * @return
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping(value = "/coupon/voucheritem")
  public VoucherItem createVoucherItem(@RequestBody VoucherItem request) {
    if (null == request) {
      throw new CouponException("400", "voucheritem request body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new CouponException("400", "voucher item name is empty");
    }
    if (StringUtils.isEmpty(request.getGoodid())) {
      throw new CouponException("400", "voucher item good is null");
    }
    if (request.getScore() == 0) {
      throw new CouponException("400", "voucher item score is 0");
    }

    return couponService.createVoucherItem(request);
  }

  /**
   * 废弃兑换项
   * 
   * @param itemid
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @DeleteMapping(value = "/coupon/voucheritem/{itemid}")
  public void deprecateVoucherItem(@PathVariable("itemid") String itemid) {
    couponService.deprecateVoucherItem(itemid);
  }

}
