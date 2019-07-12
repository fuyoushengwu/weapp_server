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

import cn.aijiamuyingfang.server.shoporder.service.ShopCartService;
import cn.aijiamuyingfang.vo.shopcart.CreateShopCartRequest;
import cn.aijiamuyingfang.vo.shopcart.PagableShopCartList;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;

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
  private ShopCartService shopCartService;

  /**
   * 往用户购物车添加商品
   * 
   * @param username
   * @param requestBean
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{username}/shop_cart")
  public ShopCart addShopCart(@PathVariable("username") String username,
      @RequestBody CreateShopCartRequest requestBean) {
    return shopCartService.addShopCart(username, requestBean.getGoodId(), requestBean.getGoodNum());
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param username
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{username}/shop_cart")
  public PagableShopCartList getShopCartList(@PathVariable("username") String username,
      @RequestParam(value = "current_page") int currentPage, @RequestParam(value = "page_size") int pageSize) {
    return shopCartService.getShopCartList(username, currentPage, pageSize);
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param username
   * @param checked
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}/shop_cart/allcheck/{checked}")
  public void checkAllShopCart(@PathVariable("username") String username, @PathVariable("checked") boolean checked) {
    shopCartService.checkAllShopCart(username, checked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param username
   * @param shopCartId
   * @param checked
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}/shop_cart/{shop_cart_id}/check/{checked}")
  public void checkShopCart(@PathVariable("username") String username, @PathVariable("shop_cart_id") String shopCartId,
      @PathVariable("checked") boolean checked) {
    shopCartService.checkShopCart(username, shopCartId, checked);
  }

  /**
   * 删除购物项
   * 
   * @param username
   * @param shopCartId
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{username}/shop_cart/{shop_cart_id}")
  public void deleteShopCart(@PathVariable("username") String username,
      @PathVariable("shop_cart_id") String shopCartId) {
    shopCartService.deleteShopCart(username, shopCartId);
  }

  /**
   * 修改用户购物车中商品数量
   * 
   * @param username
   * @param shopCartId
   * @param count
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}/shop_cart/{shop_cart_id}/count/{count}")
  public ShopCart updateShopCartCount(@PathVariable("username") String username,
      @PathVariable("shop_cart_id") String shopCartId, @PathVariable("count") int count) {
    return shopCartService.updateShopCartCount(username, shopCartId, count);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodId
   *          商品id
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/shop_cart/good/{good_id}")
  public void deleteGood(@PathVariable("good_id") String goodId) {
    shopCartService.deleteGood(goodId);
  }
}
