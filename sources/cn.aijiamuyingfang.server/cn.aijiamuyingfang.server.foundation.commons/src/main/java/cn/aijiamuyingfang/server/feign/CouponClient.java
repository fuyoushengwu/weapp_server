package cn.aijiamuyingfang.server.feign;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.coupon.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.server.feign.domain.coupon.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.feign.domain.coupon.GetVoucherItemListResponse;
import cn.aijiamuyingfang.server.feign.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.feign.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.server.feign.domain.coupon.VoucherItem;

@FeignClient(name = "coupon-service")
public interface CouponClient {
  /**
   * 分页获取用户兑换券
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GetMapping(value = "/user/{userid}/coupon/uservoucher")
  ResponseBean<GetUserVoucherListResponse> getUserVoucherList(@PathVariable("userid") String userid,
      @RequestParam("currentpage") int currentpage, @RequestParam("pagesize") int pagesize);

  /**
   * 更新用户兑换券
   * 
   * @param userid
   * @param userVoucherList
   */
  @PutMapping(value = "/user/{userid}/coupon/uservoucher")
  ResponseBean<Void> updateUserVoucherList(@PathVariable("userid") String userid,
      @RequestBody List<UserVoucher> userVoucherList);

  /**
   * 获得用户的兑换券
   * 
   * @param userid
   * @param voucherid
   * @return
   */
  @GetMapping(value = "/user/{userid}/coupon/uservoucher/{voucherid}")
  ResponseBean<UserVoucher> getUserVoucher(@PathVariable("userid") String userid,
      @PathVariable("voucherid") String voucherid);

  /**
   * 获得用户用户GoodVoucher的兑换券
   * 
   * @param userid
   * @param voucherid
   * @return
   */
  @GetMapping(value = "/user/{userid}/coupon/uservoucher/goodvoucher/{voucherid}")
  ResponseBean<UserVoucher> getUserVoucherForGoodVoucher(@PathVariable("userid") String userid,
      @PathVariable("voucherid") String voucherid);

  /**
   * 分页获取商品兑换项
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GetMapping(value = "/coupon/goodvoucher")
  ResponseBean<GetGoodVoucherListResponse> getGoodVoucherList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize);

  /**
   * 获取商品兑换项
   * 
   * @param voucherid
   * @return
   */
  @GetMapping(value = "/coupon/goodvoucher/{voucherid}")
  ResponseBean<GoodVoucher> getGoodVoucher(@PathVariable("voucherid") String voucherid);

  /**
   * 废弃商品兑换券
   * 
   * @param voucherid
   */
  @DeleteMapping(value = "/coupon/goodvoucher/{voucherid}")
  Future<ResponseBean<Void>> deprecateGoodVoucher(@PathVariable("voucherid") String voucherid);

  /**
   * 分页获取可选的兑换方式
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GetMapping(value = "/coupon/voucheritem")
  ResponseBean<GetVoucherItemListResponse> getVoucherItemList(@RequestParam("currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize);

  /**
   * 获取兑换方式
   * 
   * @param voucheritemId
   * @return
   */
  @GetMapping(value = "/coupon/voucheritem/{voucheritemId}")
  ResponseBean<VoucherItem> getVoucherItem(@PathVariable("voucheritemId") String voucheritemId);

}
