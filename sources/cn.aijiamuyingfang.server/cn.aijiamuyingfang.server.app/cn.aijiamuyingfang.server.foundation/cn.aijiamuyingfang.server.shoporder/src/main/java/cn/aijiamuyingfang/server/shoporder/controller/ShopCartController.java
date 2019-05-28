package cn.aijiamuyingfang.server.shoporder.controller;

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

import cn.aijiamuyingfang.server.shoporder.domain.ShopCart;
import cn.aijiamuyingfang.server.shoporder.domain.request.CreateShopCartRequest;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopCartListResponse;
import cn.aijiamuyingfang.server.shoporder.service.ShopCartService;

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
   * @param userid
   * @param requestBean
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{userid}/shopcart")
  public ShopCart addShopCart(@PathVariable("userid") String userid,
      @RequestBody CreateShopCartRequest requestBean) {
    return shopcartService.addShopCart(userid, requestBean.getGoodid(), requestBean.getGoodNum());
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userid
   *          用户id
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/shopcart")
  public GetShopCartListResponse getShopCartList(@PathVariable("userid") String userid,
      @RequestParam(value = "currentpage") int currentpage, @RequestParam(value = "pagesize") int pagesize) {
    return shopcartService.getShopCartList(userid, currentpage, pagesize);
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param userid
   * @param ischecked
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/shopcart/allcheck/{ischecked}")
  public void checkAllShopCart(@PathVariable("userid") String userid,
      @PathVariable("ischecked") boolean ischecked) {
    shopcartService.checkAllShopCart(userid, ischecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userid
   * @param shopcartid
   * @param ischecked
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/shopcart/{shopcartid}/check/{ischecked}")
  public void checkShopCart(@PathVariable("userid") String userid, @PathVariable("shopcartid") String shopcartid,
      @PathVariable("ischecked") boolean ischecked) {
    shopcartService.checkShopCart(userid, shopcartid, ischecked);
  }

  /**
   * 删除购物项
   * 
   * @param shopcartid
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{userid}/shopcart/{shopcartid}")
  public void deleteShopCart(@PathVariable("userid") String userid, @PathVariable("shopcartid") String shopcartid) {
    shopcartService.deleteShopCart(userid, shopcartid);
  }

  /**
   * 修改用户购物车中商品数量
   * 
   * @param userid
   * @param shopcartid
   * @param count
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/shopcart/{shopcartid}/count/{count}")
  public ShopCart updateShopCartCount(@PathVariable("userid") String userid,
      @PathVariable("shopcartid") String shopcartid, @PathVariable("count") int count) {
    return shopcartService.updateShopCartCount(userid, shopcartid, count);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodid
   *          商品id
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/shopcart/good/{goodid}")
  public void deleteGood(@PathVariable("goodid") String goodid) {
    shopcartService.deleteGood(goodid);
  }
}
