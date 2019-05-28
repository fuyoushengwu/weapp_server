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
  private ShopCartRepository shopcartRepository;

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private UserClient userClient;

  /**
   * 往购物车添加商品
   * 
   * @param userid
   *          用户id
   * @param goodid
   *          商品id
   * @param goodNum
   * @return
   */
  public ShopCart addShopCart(String userid, String goodid, int goodNum) {
    User user = userClient.getUserById(userid).getData();
    if (null == user) {
      throw new ShopCartException(ResponseCode.USER_NOT_EXIST, userid);
    }
    if (goodNum <= 0) {
      return null;
    }

    Good good = goodClient.getGood(goodid).getData();
    if (null == good) {
      throw new ShopCartException(ResponseCode.GOOD_NOT_EXIST, goodid);
    }
    ShopCart shopcart = shopcartRepository.findByUseridAndGoodId(userid, goodid);
    if (null == shopcart) {
      shopcart = new ShopCart();
      shopcart.setUserid(userid);
      shopcart.setGoodId(good.getId());
      shopcart.setCount(0);
    }
    shopcart.addCount(goodNum);
    shopcartRepository.saveAndFlush(shopcart);
    return shopcart;
  }

  /**
   * 分页获取购物车中的项目
   * 
   * @param userid
   *          用户id
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetShopCartListResponse getShopCartList(String userid, int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "id");
    Page<ShopCart> shopcartPage = shopcartRepository.findByUserid(userid, pageRequest);
    GetShopCartListResponse response = new GetShopCartListResponse();
    response.setCurrentpage(shopcartPage.getNumber() + 1);
    response.setDataList(shopcartPage.getContent());
    response.setTotalpage(shopcartPage.getTotalPages());
    return response;
  }

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userid
   *          用户id
   * @param ischecked
   */
  public void checkAllShopCart(String userid, boolean ischecked) {
    shopcartRepository.checkAllShopCart(userid, ischecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userid
   *          用户id
   * @param shopcartid
   * @param ischecked
   */
  public void checkShopCart(String userid, String shopcartid, boolean ischecked) {
    ShopCart shopcart = shopcartRepository.findOne(shopcartid);
    if (null == shopcart) {
      return;
    }
    if (!userid.equals(shopcart.getUserid())) {
      throw new AccessDeniedException("no permission check other user's shopcart");
    }
    shopcart.setIschecked(ischecked);
    shopcartRepository.saveAndFlush(shopcart);
  }

  /**
   * 删除购物项
   * 
   * @param userid
   *          用户id
   * @param shopcartid
   */
  public void deleteShopCart(String userid, String shopcartid) {
    ShopCart shopcart = shopcartRepository.findOne(shopcartid);
    if (null == shopcart) {
      return;
    }
    if (!userid.equals(shopcart.getUserid())) {
      throw new AccessDeniedException("no permission delete other user's shopcart");
    }
    shopcartRepository.delete(shopcartid);
  }

  /**
   * 修改购物项商品数量
   * 
   * @param userid
   *          用户id
   * @param shopcartid
   * @param count
   * @return
   */
  public ShopCart updateShopCartCount(String userid, String shopcartid, int count) {
    ShopCart shopcart = shopcartRepository.findOne(shopcartid);
    if (null == shopcart) {
      throw new IllegalArgumentException("shopcart item not exist");
    }
    if (count <= 0) {
      return shopcart;
    }

    if (!userid.equals(shopcart.getUserid())) {
      throw new AccessDeniedException("no permission chage other user's shopcart");
    }
    shopcart.setCount(count);
    shopcartRepository.saveAndFlush(shopcart);
    return shopcart;
  }

  /**
   * 删除购物车中的商品
   * 
   * @param goodid
   *          商品id
   */
  public void deleteGood(String goodid) {
    shopcartRepository.deleteGood(goodid);
  }
}
