package cn.aijiamuyingfang.server.shoporder.controller;

import java.io.IOException;
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

/****
 * [描述]:**
 * <p>
 * **订单服务Controller层**
 * </p>
 * *****
 * 
 * @version 1.0.0*@author ShiWei*@email shiweideyouxiang @sina.cn
 * @date 2018-07-02 23:01:50
 */
@RestController
public class ShopOrderController {
  @Autowired
  private ShopOrderService shoporderSerivce;

  /**
   * 分页获取用户的订单信息
   *
   * @param userid
   *          用户id
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/shoporder")
  public GetShopOrderListResponse getUserShopOrderList(@PathVariable(name = "userid") String userid,
      @RequestParam(value = "status", required = false) List<ShopOrderStatus> status,
      @RequestParam(value = "sendtype", required = false) List<SendType> sendtype,
      @RequestParam(value = "currentpage") int currentpage, @RequestParam(value = "pagesize") int pagesize) {
    return shoporderSerivce.getUserShopOrderList(userid, status, sendtype, currentpage, pagesize);
  }

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param userid
   * @param goodids
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/coupon/shoporder")
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(@PathVariable("userid") String userid,
      @RequestParam(name = "goodids", required = false) List<String> goodids) {
    return shoporderSerivce.getUserShopOrderVoucherList(userid, goodids);
  }

  /**
   * 分页获取所有的订单信息
   * 
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder")
  public GetShopOrderListResponse getShopOrderList(
      @RequestParam(value = "status", required = false) List<ShopOrderStatus> status,
      @RequestParam(value = "sendtype", required = false) List<SendType> sendtype,
      @RequestParam(value = "currentpage") int currentpage, @RequestParam(value = "pagesize") int pagesize) {
    return shoporderSerivce.getShopOrderList(status, sendtype, currentpage, pagesize);
  }

  /**
   * 更新订单状态(在送货时调用)
   *
   * @param shoporderid
   * @param requestBean
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @PutMapping(value = "/shoporder/{shoporderid}/status")
  public void updateShopOrderStatus(@PathVariable("shoporderid") String shoporderid,
      @RequestBody UpdateShopOrderStatusRequest requestBean) {
    shoporderSerivce.updateShopOrderStatus(shoporderid, requestBean);
  }

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   *
   * @param shoporderid
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @DeleteMapping(value = "/shoporder/{shoporderid}")
  public void delete100DaysFinishedShopOrder(@PathVariable("shoporderid") String shoporderid) {
    shoporderSerivce.delete100DaysFinishedShopOrder(shoporderid);
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   *
   * @param shoporderid
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{userid}/shoporder/{shoporderid}")
  public void deleteUserShopOrder(@PathVariable("userid") String userid,
      @PathVariable("shoporderid") String shoporderid) {
    shoporderSerivce.deleteUserShopOrder(userid, shoporderid);
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   *
   * @param shoporderid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/shoporder/{shoporderid}/finisheorder")
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(@PathVariable("userid") String userid,
      @PathVariable("shoporderid") String shoporderid) {
    return shoporderSerivce.confirmUserShopOrderFinished(userid, shoporderid);
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   *
   * @param shoporderid
   * @param addressid
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/shoporder/{shoporderid}/recieveaddress/{addressid}")
  public void updateUserShopOrderRecieveAddress(@PathVariable("userid") String userid,
      @PathVariable("shoporderid") String shoporderid, @PathVariable("addressid") String addressid) {
    shoporderSerivce.updateUserShopOrderRecieveAddress(userid, shoporderid, addressid);
  }

  /**
   * 分页获取已完成预约单
   *
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder/preorder/finished")
  public GetFinishedPreOrderListResponse getFinishedPreOrderList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam(value = "pagesize") int pagesize) {
    return shoporderSerivce.getFinishedPreOrderList(currentpage, pagesize);
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   *
   * @param shoporderid
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}/shoporder/{shoporderid}")
  public ShopOrder getUserShopOrder(@PathVariable("userid") String userid,
      @PathVariable(name = "shoporderid") String shoporderid) {
    return shoporderSerivce.getUserShopOrder(userid, shoporderid);
  }

  /**
   * 创建用户订单
   *
   * @param userid
   *          用户id
   * @param requestBean
   * @throws IOException
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{userid}/shoporder")
  public ShopOrder createUserShopOrder(@PathVariable("userid") String userid,
      @RequestBody CreateShopOrderRequest requestBean) throws IOException {
    return shoporderSerivce.createUserShopOrder(userid, requestBean);
  }

  /**
   * 得到用户订单根据状态分类的数目
   *
   * @param userid
   *          用户id
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/shoporder/statuscount")
  public Map<String, Integer> getUserShopOrderStatusCount(@PathVariable("userid") String userid) {
    return shoporderSerivce.getUserShopOrderStatusCount(userid);
  }

  /**
   * 分页获取预定的商品信息
   *
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/shoporder/preordergoods")
  public GetPreOrderGoodListResponse getPreOrderGoodList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam(value = "pagesize") int pagesize) {
    return shoporderSerivce.getPreOrderGoodList(currentpage, pagesize);
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodid
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/shoporder/preorder/good/{goodid}")
  public void updatePreOrder(@PathVariable("goodid") String goodid) {
    shoporderSerivce.updatePreOrders(goodid);
  }

}
