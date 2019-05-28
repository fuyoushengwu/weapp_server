package cn.aijiamuyingfang.server.coupon.controller;

import java.util.List;

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

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.coupon.domain.GoodVoucher;
import cn.aijiamuyingfang.server.coupon.domain.UserVoucher;
import cn.aijiamuyingfang.server.coupon.domain.VoucherItem;
import cn.aijiamuyingfang.server.coupon.domain.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.server.coupon.domain.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.coupon.domain.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.server.coupon.service.CouponService;

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
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/coupon/uservoucher")
  public GetUserVoucherListResponse getUserVoucherList(@PathVariable("userid") String userid,
      @RequestParam("currentpage") int currentpage, @RequestParam("pagesize") int pagesize) {
    return couponService.getUserVoucherList(userid, currentpage, pagesize);
  }

  /**
   * 更新用户兑换券
   * 
   * @param userid
   * @param userVoucherList
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PutMapping(value = "/user/{userid}/coupon/uservoucher")
  public void updateUserVoucherList(@PathVariable("userid") String userid,
      @RequestBody List<UserVoucher> userVoucherList) {
    couponService.updateUserVoucher(userVoucherList);
  }

  /**
   * 获得用户的兑换券
   * 
   * @param userid
   * @param voucherid
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/coupon/uservoucher/{voucherid}")
  public UserVoucher getUserVoucher(@PathVariable("userid") String userid,
      @PathVariable("voucherid") String voucherid) {
    return couponService.getUserVoucher(voucherid);
  }

  /**
   * 获得用户用户GoodVoucher的兑换券
   * 
   * @param userid
   * @param voucherid
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/coupon/uservoucher/goodvoucher/{voucherid}")
  public UserVoucher getUserVoucherForGoodVoucher(@PathVariable("userid") String userid,
      @PathVariable("voucherid") String voucherid) {
    return couponService.getUserVoucherForGoodVoucher(userid, voucherid);
  }

  /**
   * 更新用户兑换券
   * 
   * @param userid
   * @param voucherid
   * @param uservoucher
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PutMapping(value = "/user/{userid}/coupon/uservoucher/{voucherid}")
  public void updateUserVoucher(@PathVariable("userid") String userid, @PathVariable("voucherid") String voucherid,
      @RequestBody UserVoucher uservoucher) {
    couponService.updateUserVoucher(uservoucher);
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
   * 创建商品兑换券
   * 
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/coupon/goodvoucher")
  public GoodVoucher createGoodVoucher(@RequestBody GoodVoucher request) {
    if (null == request) {
      throw new IllegalArgumentException("goodvoucher request  body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new IllegalArgumentException("good voucher name is empyt");
    }
    if (request.getScore() == 0) {
      throw new IllegalArgumentException("good voucher score is 0");
    }
    if (CollectionUtils.isEmpty(request.getVoucheritemIdList())) {
      throw new IllegalArgumentException("good voucher items is empyt");
    }
    return couponService.createORUpdateGoodVoucher(request);
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
   * 废弃商品兑换券
   * 
   * @param voucherid
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
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
   * 创建兑换选择项
   * 
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/coupon/voucheritem")
  public VoucherItem createVoucherItem(@RequestBody VoucherItem request) {
    if (null == request) {
      throw new IllegalArgumentException("voucheritem request body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new IllegalArgumentException("voucher item name is empty");
    }
    if (StringUtils.isEmpty(request.getGoodid())) {
      throw new IllegalArgumentException("voucher item good is null");
    }
    if (request.getScore() == 0) {
      throw new IllegalArgumentException("voucher item score is 0");
    }

    return couponService.createORUpdateVoucherItem(request);
  }

  /**
   * 获取兑换方式
   * 
   * @param voucheritemId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/voucheritem/{voucheritemId}")
  public VoucherItem getVoucherItem(@PathVariable("voucheritemId") String voucheritemId) {
    return couponService.getVoucherItem(voucheritemId);
  }

  /**
   * 废弃兑换项
   * 
   * @param voucheritemId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/coupon/voucheritem/{voucheritemId}")
  public void deprecateVoucherItem(@PathVariable("voucheritemId") String voucheritemId) {
    couponService.deprecateVoucherItem(voucheritemId);
  }

}
