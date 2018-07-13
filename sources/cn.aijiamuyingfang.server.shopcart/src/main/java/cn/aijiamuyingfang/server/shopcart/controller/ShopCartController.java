package cn.aijiamuyingfang.server.shopcart.controller;

import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.shopcart.AddShopCartItemRequest;
import cn.aijiamuyingfang.server.domain.shopcart.GetShopCartItemListResponse;
import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.server.shopcart.service.ShopCartService;
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
 * 购物车Controller层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 18:52:14
 */
@RestController
public class ShopCartController {
  @Autowired
  private ShopCartService shopcartService;

  /**
   * 往用户购物车添加商品
   * 
   * @param headerUserId
   * @param userid
   * @param requestBean
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PostMapping(value = "/user/{userid}/shopcart")
  public ShopCartItem addShopCartItem(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestBody AddShopCartItemRequest requestBean) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission add good to other user's shopcart");
    }
    return shopcartService.addShopCartItem(userid, requestBean.getGoodid(), requestBean.getGoodNum());
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/shopcart")
  public GetShopCartItemListResponse getShopCartItemList(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestParam(value = "currentpage") int currentpage,
      @RequestParam(value = "pagesize") int pagesize) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's shopcart list");
    }
    return shopcartService.getShopCartItemList(userid, currentpage, pagesize);
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param headerUserId
   * @param userid
   * @param ischecked
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/shopcart/allcheck/{ischecked}")
  public void checkAllShopCartItem(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable("ischecked") boolean ischecked) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission check other user's shopcart");
    }
    shopcartService.checkAllShopCartItem(userid, ischecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param headerUserId
   * @param userid
   * @param shopcartid
   * @param ischecked
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/shopcart/{shopcartid}/check/{ischecked}")
  public void checkShopCartItem(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable("shopcartid") String shopcartid, @PathVariable("ischecked") boolean ischecked) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission check other user's shopcart");
    }
    shopcartService.checkShopCartItem(userid, shopcartid, ischecked);
  }

  /**
   * 删除购物项
   * 
   * @param shopcartid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @DeleteMapping(value = "/user/{userid}/shopcart/{shopcartid}")
  public void deleteShopCartItem(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable("shopcartid") String shopcartid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission delete other user's shopcart");
    }
    shopcartService.deleteShopCartItem(userid, shopcartid);
  }

  /**
   * 修改用户购物车中商品数量
   * 
   * @param headerUserId
   * @param userid
   * @param shopcartid
   * @param count
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/shopcart/{shopcartid}/count/{count}")
  public ShopCartItem updateShopCartItemCount(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("shopcartid") String shopcartid,
      @PathVariable("count") int count) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission chage other user's shopcart");
    }
    return shopcartService.updateShopCartItemCount(userid, shopcartid, count);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodid
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @DeleteMapping(value = "/shopcart/good/{goodid}")
  public void deleteGood(@PathVariable("goodid") String goodid) {
    shopcartService.deleteGood(goodid);
  }
}
