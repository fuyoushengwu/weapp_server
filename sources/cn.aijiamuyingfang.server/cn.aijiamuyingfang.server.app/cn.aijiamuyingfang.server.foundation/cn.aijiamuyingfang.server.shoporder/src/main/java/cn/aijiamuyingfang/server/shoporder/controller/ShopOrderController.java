package cn.aijiamuyingfang.server.shoporder.controller;

import java.util.List;
import java.util.Map;

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

import cn.aijiamuyingfang.server.domain.SendType;
import cn.aijiamuyingfang.server.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.shoporder.domain.ShopOrder;
import cn.aijiamuyingfang.server.shoporder.domain.request.CreateShopOrderRequest;
import cn.aijiamuyingfang.server.shoporder.domain.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.server.shoporder.domain.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.server.shoporder.service.ShopOrderService;

/***
 * [描述]:
 * <p>
 * 订单服务Controller层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 23:01:50
 */
@RestController
public class ShopOrderController {
  @Autowired
  private ShopOrderService shoporderSerivce;

  /**
   * 分页获取用户的订单信息
   * 
   * @param userId
   * @param status
   * @param sendType
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userId.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{user_id}/shoporder")
  public GetShopOrderListResponse getUserShopOrderList(@PathVariable(name = "user_id") String userId,
      @RequestParam(value = "status", required = false) List<ShopOrderStatus> status,
      @RequestParam(value = "send_type", required = false) List<SendType> sendType,
      @RequestParam(value = "current_page") int currentPage, @RequestParam(value = "page_size") int pageSize) {
    return shoporderSerivce.getUserShopOrderList(userId, status, sendType, currentPage, pageSize);
  }

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param userId
   * @param goodIdList
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userId.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{user_id}/coupon/shoporder")
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(@PathVariable("user_id") String userId,
      @RequestParam(name = "good_id", required = false) List<String> goodIdList) {
    return shoporderSerivce.getUserShopOrderVoucherList(userId, goodIdList);
  }

  /**
   * 分页获取所有的订单信息
   * 
   * @param status
   * @param sendType
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder")
  public GetShopOrderListResponse getShopOrderList(
      @RequestParam(value = "status", required = false) List<ShopOrderStatus> status,
      @RequestParam(value = "send_type", required = false) List<SendType> sendType,
      @RequestParam(value = "current_page") int currentPage, @RequestParam(value = "page_size") int pageSize) {
    return shoporderSerivce.getShopOrderList(status, sendType, currentPage, pageSize);
  }

  /**
   * 更新订单状态(在送货时调用)
   *
   * @param shopOrderId
   * @param requestBean
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @PutMapping(value = "/shoporder/{shop_order_id}/status")
  public void updateShopOrderStatus(@PathVariable("shop_order_id") String shopOrderId,
      @RequestBody UpdateShopOrderStatusRequest requestBean) {
    shoporderSerivce.updateShopOrderStatus(shopOrderId, requestBean);
  }

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   *
   * @param shopOrderId
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @DeleteMapping(value = "/shoporder/{shop_order_id}")
  public void delete100DaysFinishedShopOrder(@PathVariable("shop_order_id") String shopOrderId) {
    shoporderSerivce.delete100DaysFinishedShopOrder(shopOrderId);
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{user_id}/shoporder/{shop_order_id}")
  public void deleteUserShopOrder(@PathVariable("user_id") String userId,
      @PathVariable("shop_order_id") String shopOrderId) {
    shoporderSerivce.deleteUserShopOrder(userId, shopOrderId);
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/shoporder/{shop_order_id}/finisheorder")
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(@PathVariable("user_id") String userId,
      @PathVariable("shop_order_id") String shopOrderId) {
    return shoporderSerivce.confirmUserShopOrderFinished(userId, shopOrderId);
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @param addressId
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/shoporder/{shop_order_id}/recieveaddress/{address_id}")
  public void updateUserShopOrderRecieveAddress(@PathVariable("user_id") String userId,
      @PathVariable("shop_order_id") String shopOrderId, @PathVariable("address_id") String addressId) {
    shoporderSerivce.updateUserShopOrderRecieveAddress(userId, shopOrderId, addressId);
  }

  /**
   * 分页获取已完成预约单
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder/preorder/finished")
  public GetFinishedPreOrderListResponse getFinishedPreOrderList(@RequestParam(value = "current_page") int currentPage,
      @RequestParam(value = "page_size") int pageSize) {
    return shoporderSerivce.getFinishedPreOrderList(currentPage, pageSize);
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userId.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{user_id}/shoporder/{shop_order_id}")
  public ShopOrder getUserShopOrder(@PathVariable("user_id") String userId,
      @PathVariable(name = "shop_order_id") String shopOrderId) {
    return shoporderSerivce.getUserShopOrder(userId, shopOrderId);
  }

  /**
   * 创建用户订单
   * 
   * @param userId
   * @param requestBean
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{user_id}/shoporder")
  public ShopOrder createUserShopOrder(@PathVariable("user_id") String userId,
      @RequestBody CreateShopOrderRequest requestBean) {
    return shoporderSerivce.createUserShopOrder(userId, requestBean);
  }

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * @param userId
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/shoporder/statuscount")
  public Map<String, Integer> getUserShopOrderStatusCount(@PathVariable("user_id") String userId) {
    return shoporderSerivce.getUserShopOrderStatusCount(userId);
  }

  /**
   * 分页获取预定的商品信息
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder/preordergoods")
  public GetPreOrderGoodListResponse getPreOrderGoodList(@RequestParam(value = "current_page") int currentPage,
      @RequestParam(value = "page_size") int pageSize) {
    return shoporderSerivce.getPreOrderGoodList(currentPage, pageSize);
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/shoporder/preorder/good/{good_id}")
  public void updatePreOrder(@PathVariable("good_id") String goodId) {
    shoporderSerivce.updatePreOrders(goodId);
  }

}
