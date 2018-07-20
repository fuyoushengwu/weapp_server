package cn.aijiamuyingfang.server.shoporder.controller;

import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.domain.SendType;
import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.shoporder.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.domain.shoporder.CreateUserShoprderRequest;
import cn.aijiamuyingfang.server.domain.shoporder.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetUserShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.server.domain.util.ConverterService;
import cn.aijiamuyingfang.server.shoporder.service.ShopOrderService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
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

  @Autowired
  private ConverterService converterService;

  /**
   * 分页获取用户的订单信息
   * 
   * @param userid
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/shoporder")
  public GetUserShopOrderListResponse getUserShopOrderList(@RequestHeader("userid") String headerUserId,
      @PathVariable(name = "userid") String userid,
      @RequestParam(value = "status", required = false) List<ShopOrderStatus> status,
      @RequestParam(value = "sendtype", required = false) List<SendType> sendtype,
      @RequestParam(value = "currentpage") int currentpage, @RequestParam(value = "pagesize") int pagesize) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's shoporder");
    }
    return shoporderSerivce.getUserShopOrderList(userid, status, sendtype, currentpage, pagesize);
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
  @PreAuthorize(value = "hasAnyAuthority('admin','sender')")
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
   * @param token
   * @param shoporderid
   * @param requestBean
   * @throws IOException
   */
  @PreAuthorize(value = "hasAnyAuthority('admin','sender')")
  @PutMapping(value = "/shoporder/{shoporderid}/status")
  public void updateShopOrderStatus(@RequestHeader(AuthConstants.HEADER_STRING) String token,
      @PathVariable("shoporderid") String shoporderid, @RequestBody UpdateShopOrderStatusRequest requestBean)
      throws IOException {
    shoporderSerivce.updateShopOrderStatus(token, shoporderid, requestBean);
  }

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param shoporderid
   */
  @PreAuthorize(value = "hasAnyAuthority('admin','sender')")
  @DeleteMapping(value = "/shoporder/{shoporderid}")
  public void delete100DaysFinishedShopOrder(@PathVariable("shoporderid") String shoporderid) {
    shoporderSerivce.delete100DaysFinishedShopOrder(shoporderid);
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param shoporderid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @DeleteMapping(value = "/user/{userid}/shoporder/{shoporderid}")
  public void deleteUserShopOrder(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable("shoporderid") String shoporderid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission delete other user's shoporder");
    }
    shoporderSerivce.deleteUserShopOrder(userid, shoporderid);
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param shoporderid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/shoporder/{shoporderid}/finisheorder")
  public ConfirmUserShopOrderFinishedResponse confirmUserShopOrderFinished(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("shoporderid") String shoporderid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission confirm other user's shoporder");
    }
    return shoporderSerivce.confirmUserShopOrderFinished(userid, shoporderid);
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param shoporderid
   * @param addressid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/shoporder/{shoporderid}/recieveaddress/{addressid}")
  public void updateUserShopOrderRecieveAddress(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("shoporderid") String shoporderid,
      @PathVariable("addressid") String addressid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission to change other user's shoporder recieve address");
    }
    shoporderSerivce.updateUserShopOrderRecieveAddress(userid, shoporderid, addressid);
  }

  /**
   * 分页获取已完成预约单
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('admin','sender')")
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
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/shoporder/{shoporderid}")
  public ShopOrder getUserShopOrder(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable(name = "shoporderid") String shoporderid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission other user's shoporder");
    }
    return shoporderSerivce.getUserShopOrder(userid, shoporderid);
  }

  /**
   * 创建用户订单
   * 
   * @param userid
   * @param requestBean
   * @throws IOException
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PostMapping(value = "/user/{userid}/shoporder")
  public ShopOrder createUserShopOrder(@RequestHeader(AuthConstants.HEADER_STRING) String token,
      @RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @RequestBody CreateUserShoprderRequest requestBean) throws IOException {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission create other user's shoporder");
    }
    return shoporderSerivce.createUserShopOrder(token, userid, requestBean);
  }

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/shoporder/statuscount")
  public Map<String, Integer> getUserShopOrderStatusCount(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's  shoporder status count");
    }
    return shoporderSerivce.getUserShopOrderStatusCount(userid);
  }

  /**
   * 分页获取预定的商品信息
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('admin','sender')")
  @GetMapping(value = "/shoporder/preordergoods")
  public GetPreOrderGoodListResponse getPreOrderGoodList(@RequestParam(value = "currentpage") int currentpage,
      @RequestParam(value = "pagesize") int pagesize) {
    return shoporderSerivce.getPreOrderGoodList(currentpage, pagesize);
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param token
   * @param request
   * @throws IOException
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PutMapping(value = "/shoporder/preorder")
  public void updatePreOrder(@RequestHeader(AuthConstants.HEADER_STRING) String token, @RequestBody GoodRequest request)
      throws IOException {
    if (null == request) {
      throw new ShopOrderException("400", "update preorder request body is null");
    }
    shoporderSerivce.updatePreOrders(token, converterService.from(request));
  }

}
