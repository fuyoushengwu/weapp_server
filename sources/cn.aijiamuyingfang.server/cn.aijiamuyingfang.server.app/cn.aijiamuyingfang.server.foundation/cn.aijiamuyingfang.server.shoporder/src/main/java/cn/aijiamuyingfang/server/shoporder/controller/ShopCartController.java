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
  private ShopCartService shopCartService;

  /**
   * 往用户购物车添加商品
   * 
   * @param userId
   * @param requestBean
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{user_id}/shop_cart")
  public ShopCart addShopCart(@PathVariable("user_id") String userId, @RequestBody CreateShopCartRequest requestBean) {
    return shopCartService.addShopCart(userId, requestBean.getGoodId(), requestBean.getGoodNum());
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userId
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/shop_cart")
  public GetShopCartListResponse getShopCartList(@PathVariable("user_id") String userId,
      @RequestParam(value = "current_page") int currentPage, @RequestParam(value = "page_size") int pageSize) {
    return shopCartService.getShopCartList(userId, currentPage, pageSize);
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param userId
   * @param isChecked
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/shop_cart/allcheck/{is_checked}")
  public void checkAllShopCart(@PathVariable("user_id") String userId, @PathVariable("is_checked") boolean isChecked) {
    shopCartService.checkAllShopCart(userId, isChecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userId
   * @param shopCartId
   * @param isChecked
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/shop_cart/{shop_cart_id}/check/{is_checked}")
  public void checkShopCart(@PathVariable("user_id") String userId, @PathVariable("shop_cart_id") String shopCartId,
      @PathVariable("is_checked") boolean isChecked) {
    shopCartService.checkShopCart(userId, shopCartId, isChecked);
  }

  /**
   * 删除购物项
   * 
   * @param shopCartId
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{user_id}/shop_cart/{shop_cart_id}")
  public void deleteShopCart(@PathVariable("user_id") String userId, @PathVariable("shop_cart_id") String shopCartId) {
    shopCartService.deleteShopCart(userId, shopCartId);
  }

  /**
   * 修改用户购物车中商品数量
   * 
   * @param userId
   * @param shopCartId
   * @param count
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/shop_cart/{shop_cart_id}/count/{count}")
  public ShopCart updateShopCartCount(@PathVariable("user_id") String userId,
      @PathVariable("shop_cart_id") String shopCartId, @PathVariable("count") int count) {
    return shopCartService.updateShopCartCount(userId, shopCartId, count);
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
