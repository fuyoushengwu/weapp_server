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
import cn.aijiamuyingfang.server.coupon.domain.response.PagableGoodVoucherList;
import cn.aijiamuyingfang.server.coupon.domain.response.PagableUserVoucherList;
import cn.aijiamuyingfang.server.coupon.domain.response.PagableVoucherItemList;
import cn.aijiamuyingfang.server.coupon.dto.GoodVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.UserVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.VoucherItemDTO;
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
   * @param username
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{username}/coupon/uservoucher")
  public PagableUserVoucherList getUserVoucherList(@PathVariable("username") String username,
      @RequestParam("current_page") int currentPage, @RequestParam("page_size") int pageSize) {
    return couponService.getUserVoucherList(username, currentPage, pageSize);
  }

  /**
   * 更新用户兑换券
   * 
   * @param username
   * @param userVoucherList
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PutMapping(value = "/user/{username}/coupon/uservoucher")
  public void updateUserVoucherList(@PathVariable("username") String username,
      @RequestBody List<UserVoucherDTO> userVoucherList) {
    couponService.updateUserVoucher(userVoucherList);
  }

  /**
   * 获得用户的兑换券
   * 
   * @param username
   * @param voucherId
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{username}/coupon/uservoucher/{voucher_id}")
  public UserVoucherDTO getUserVoucher(@PathVariable("username") String username,
      @PathVariable("voucher_id") String voucherId) {
    return couponService.getUserVoucher(voucherId);
  }

  /**
   * 获得用户用户GoodVoucher的兑换券
   * 
   * @param username
   * @param voucherId
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{username}/coupon/uservoucher/goodvoucher/{voucher_id}")
  public UserVoucherDTO getUserVoucherForGoodVoucher(@PathVariable("username") String username,
      @PathVariable("voucher_id") String voucherId) {
    return couponService.getUserVoucherForGoodVoucher(username, voucherId);
  }

  /**
   * 更新用户兑换券
   * 
   * @param username
   * @param voucherId
   * @param uservoucher
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PutMapping(value = "/user/{username}/coupon/uservoucher/{voucher_id}")
  public void updateUserVoucher(@PathVariable("username") String username, @PathVariable("voucher_id") String voucherId,
      @RequestBody UserVoucherDTO uservoucher) {
    couponService.updateUserVoucher(uservoucher);
  }

  /**
   * 分页获取商品兑换项
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/goodvoucher")
  public PagableGoodVoucherList getGoodVoucherList(@RequestParam(value = "current_page") int currentPage,
      @RequestParam("page_size") int pageSize) {
    return couponService.getGoodVoucherList(currentPage, pageSize);
  }

  /**
   * 创建商品兑换券
   * 
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/coupon/goodvoucher")
  public GoodVoucherDTO createGoodVoucher(@RequestBody GoodVoucherDTO request) {
    if (null == request) {
      throw new IllegalArgumentException("goodvoucher request  body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new IllegalArgumentException("good voucher name is empyt");
    }
    if (request.getScore() == 0) {
      throw new IllegalArgumentException("good voucher score is 0");
    }
    if (CollectionUtils.isEmpty(request.getVoucherItemIdList())) {
      throw new IllegalArgumentException("good voucher items is empyt");
    }
    return couponService.createORUpdateGoodVoucher(request);
  }

  /**
   * 获取商品兑换项
   * 
   * @param voucherId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/goodvoucher/{voucher_id}")
  public GoodVoucherDTO getGoodVoucher(@PathVariable("voucher_id") String voucherId) {
    return couponService.getGoodVoucher(voucherId);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/coupon/goodvoucher/{voucher_id}")
  public void deprecateGoodVoucher(@PathVariable("voucher_id") String voucherId) {
    couponService.deprecateGoodVoucher(voucherId);
  }

  /**
   * 分页获取可选的兑换方式
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/voucher_item")
  public PagableVoucherItemList getVoucherItemList(@RequestParam("current_page") int currentPage,
      @RequestParam("page_size") int pageSize) {
    return couponService.getVoucherItemList(currentPage, pageSize);
  }

  /**
   * 创建兑换选择项
   * 
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/coupon/voucher_item")
  public VoucherItemDTO createVoucherItem(@RequestBody VoucherItemDTO request) {
    if (null == request) {
      throw new IllegalArgumentException("VoucherItem request body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new IllegalArgumentException("VoucherItem name is empty");
    }
    if (StringUtils.isEmpty(request.getGoodId())) {
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
   * @param voucherItemId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/coupon/voucher_item/{voucher_item_id}")
  public VoucherItemDTO getVoucherItem(@PathVariable("voucher_item_id") String voucherItemId) {
    return couponService.getVoucherItem(voucherItemId);
  }

  /**
   * 废弃兑换项
   * 
   * @param voucherItemId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/coupon/voucher_item/{voucher_item_id}")
  public void deprecateVoucherItem(@PathVariable("voucher_item_id") String voucherItemId) {
    couponService.deprecateVoucherItem(voucherItemId);
  }

}
