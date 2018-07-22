package cn.aijiamuyingfang.server.shopcart.service;

import cn.aijiamuyingfang.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.ShopCartException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.shopcart.GetShopCartItemListResponse;
import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.domain.shopcart.db.ShopCartItemRepository;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
  private ShopCartItemRepository shopcartItemRepository;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * 往购物车添加商品
   * 
   * @param userid
   * @param goodid
   * @param goodNum
   * @return
   */
  public ShopCartItem addShopCartItem(String userid, String goodid, int goodNum) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new ShopCartException(ResponseCode.USER_NOT_EXIST, userid);
    }
    if (goodNum <= 0) {
      return null;
    }

    Good good = goodRepository.findOne(goodid);
    if (null == good) {
      throw new ShopCartException(ResponseCode.GOOD_NOT_EXIST, goodid);
    }
    ShopCartItem shopcartItem = shopcartItemRepository.findByUseridAndGoodId(userid, goodid);
    if (null == shopcartItem) {
      shopcartItem = new ShopCartItem();
      shopcartItem.setUserid(userid);
      shopcartItem.setGood(good);
      shopcartItem.setCount(0);
    }
    shopcartItem.addCount(goodNum);
    shopcartItemRepository.saveAndFlush(shopcartItem);
    return shopcartItem;
  }

  /**
   * 分页获取购物车中的项目
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetShopCartItemListResponse getShopCartItemList(String userid, int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<ShopCartItem> shopcartItemPage = shopcartItemRepository.findByUserid(userid, pageRequest);
    GetShopCartItemListResponse response = new GetShopCartItemListResponse();
    response.setCurrentpage(shopcartItemPage.getNumber() + 1);
    response.setDataList(shopcartItemPage.getContent());
    response.setTotalpage(shopcartItemPage.getTotalPages());
    return response;
  }

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userid
   * @param ischecked
   */
  public void checkAllShopCartItem(String userid, boolean ischecked) {
    shopcartItemRepository.checkAllShopCartItem(userid, ischecked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userid
   * @param shopcartid
   * @param ischecked
   */
  public void checkShopCartItem(String userid, String shopcartid, boolean ischecked) {
    ShopCartItem shopcartItem = shopcartItemRepository.findOne(shopcartid);
    if (null == shopcartItem) {
      return;
    }
    if (!userid.equals(shopcartItem.getUserid())) {
      throw new AuthException("403", "no permission check other user's shopcart");
    }
    shopcartItem.setIschecked(ischecked);
    shopcartItemRepository.saveAndFlush(shopcartItem);
  }

  /**
   * 删除购物项
   * 
   * @param userid
   * @param shopcartid
   */
  public void deleteShopCartItem(String userid, String shopcartid) {
    ShopCartItem shopcartitem = shopcartItemRepository.findOne(shopcartid);
    if (null == shopcartitem) {
      return;
    }
    if (!userid.equals(shopcartitem.getUserid())) {
      throw new AuthException("403", "no permission delete other user's shopcart");
    }
    shopcartItemRepository.delete(shopcartid);
  }

  /**
   * 修改购物项商品数量
   * 
   * @param userid
   * @param shopcartid
   * @param count
   * @return
   */
  public ShopCartItem updateShopCartItemCount(String userid, String shopcartid, int count) {
    ShopCartItem shopcartItem = shopcartItemRepository.findOne(shopcartid);
    if (null == shopcartItem) {
      throw new ShopCartException("400", "shopcart item not exist");
    }
    if (count <= 0) {
      return shopcartItem;
    }

    if (!userid.equals(shopcartItem.getUserid())) {
      throw new AuthException("403", "no permission chage other user's shopcart");
    }
    shopcartItem.setCount(count);
    shopcartItemRepository.saveAndFlush(shopcartItem);
    return shopcartItem;
  }

  /**
   * 删除购物车中的商品
   * 
   * @param goodid
   */
  public void deleteGood(String goodid) {
    shopcartItemRepository.deleteGood(goodid);
  }
}
