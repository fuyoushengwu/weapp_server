package cn.aijiamuyingfang.server.shoporder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.ShopCartException;
import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.feign.domain.good.Good;
import cn.aijiamuyingfang.server.feign.domain.user.User;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.domain.ShopCart;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopCartListResponse;

/**
 * [描述]:
 * <p>
 * 购物车Service层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 18:52:35
 */
@Service
public class ShopCartService {
  @Autowired
  private ShopCartRepository shopCartRepository;

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private UserClient userClient;

  /**
   * 往购物车添加商品
   * 
   * @param userId
   *          用户id
   * @param goodId
   *          商品id
   * @param goodNum
   * @return
   */
  public ShopCart addShopCart(String userId, String goodId, int goodNum) {
    User user = userClient.getUserInternal(userId, null).getData();
    if (null == user) {
      throw new ShopCartException(ResponseCode.USER_NOT_EXIST, userId);
    }
    if (goodNum <= 0) {
      return null;
    }

    Good good = goodClient.getGood(goodId).getData();
    if (null == good) {
      throw new ShopCartException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    ShopCart shopCart = shopCartRepository.findByUserIdAndGoodId(userId, goodId);
    if (null == shopCart) {
      shopCart = new ShopCart();
      shopCart.setUserId(userId);
      shopCart.setGoodId(good.getId());
      shopCart.setCount(0);
    }
    shopCart.addCount(goodNum);
    shopCartRepository.saveAndFlush(shopCart);
    return shopCart;
  }

  /**
   * 分页获取购物车中的项目
   * 
   * @param userId
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetShopCartListResponse getShopCartList(String userId, int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
    Page<ShopCart> shopCartPage = shopCartRepository.findByUserId(userId, pageRequest);
    GetShopCartListResponse response = new GetShopCartListResponse();
    response.setCurrentPage(shopCartPage.getNumber() + 1);
    response.setDataList(shopCartPage.getContent());
    response.setTotalpage(shopCartPage.getTotalPages());
    return response;
  }

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userId
   *          用户id
   * @param isChecked
   */
  public void checkAllShopCart(String userId, boolean isChecked) {
    shopCartRepository.checkAllShopCart(userId, isChecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userId
   *          用户id
   * @param shopCartId
   * @param isChecked
   */
  public void checkShopCart(String userId, String shopCartId, boolean isChecked) {
    ShopCart shopCart = shopCartRepository.findOne(shopCartId);
    if (null == shopCart) {
      return;
    }
    if (!userId.equals(shopCart.getUserId())) {
      throw new AccessDeniedException("no permission check other user's ShopCart");
    }
    shopCart.setChecked(isChecked);
    shopCartRepository.saveAndFlush(shopCart);
  }

  /**
   * 删除购物项
   * 
   * @param userId
   *          用户id
   * @param shopCartId
   */
  public void deleteShopCart(String userId, String shopCartId) {
    ShopCart shopCart = shopCartRepository.findOne(shopCartId);
    if (null == shopCart) {
      return;
    }
    if (!userId.equals(shopCart.getUserId())) {
      throw new AccessDeniedException("no permission delete other user's ShopCart");
    }
    shopCartRepository.delete(shopCartId);
  }

  /**
   * 修改购物项商品数量
   * 
   * @param userId
   *          用户id
   * @param shopCartId
   * @param count
   * @return
   */
  public ShopCart updateShopCartCount(String userId, String shopCartId, int count) {
    ShopCart shopCart = shopCartRepository.findOne(shopCartId);
    if (null == shopCart) {
      throw new IllegalArgumentException("ShopCart item not exist");
    }
    if (count <= 0) {
      return shopCart;
    }

    if (!userId.equals(shopCart.getUserId())) {
      throw new AccessDeniedException("no permission chage other user's ShopCart");
    }
    shopCart.setCount(count);
    shopCartRepository.saveAndFlush(shopCart);
    return shopCart;
  }

  /**
   * 删除购物车中的商品
   * 
   * @param goodId
   *          商品id
   */
  public void deleteGood(String goodId) {
    shopCartRepository.deleteGood(goodId);
  }
}
