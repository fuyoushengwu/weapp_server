package cn.aijiamuyingfang.server.shoporder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.dto.ShopCartDTO;
import cn.aijiamuyingfang.server.shoporder.utils.ConvertService;
import cn.aijiamuyingfang.vo.exception.ShopCartException;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.shopcart.PagableShopCartList;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
import cn.aijiamuyingfang.vo.user.User;

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

  @Autowired
  private ConvertService convertService;

  /**
   * 往购物车添加商品
   * 
   * @param username
   *          用户id
   * @param goodId
   *          商品id
   * @param goodNum
   * @return
   */
  public ShopCart addShopCart(String username, String goodId, int goodNum) {
    User user = userClient.getUserInternal(username).getData();
    if (null == user) {
      throw new ShopCartException(ResponseCode.USER_NOT_EXIST, username);
    }
    if (goodNum <= 0) {
      return null;
    }

    Good good = goodClient.getGood(goodId).getData();
    if (null == good) {
      throw new ShopCartException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    ShopCart shopCart = convertService.convertShopCartDTO(shopCartRepository.findByUsernameAndGoodId(username, goodId));
    if (null == shopCart) {
      shopCart = new ShopCart();
      shopCart.setUsername(username);
      shopCart.setGood(good);
      shopCart.setCount(0);
    }
    shopCart.addCount(goodNum);
    return convertService.convertShopCartDTO(shopCartRepository.saveAndFlush(convertService.convertShopCart(shopCart)));
  }

  /**
   * 分页获取购物车中的项目
   * 
   * @param username
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  public PagableShopCartList getShopCartList(String username, int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
    Page<ShopCartDTO> shopCartDTOPage = shopCartRepository.findByUsername(username, pageRequest);
    PagableShopCartList response = new PagableShopCartList();
    response.setCurrentPage(shopCartDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertShopCartDTOList(shopCartDTOPage.getContent()));
    response.setTotalpage(shopCartDTOPage.getTotalPages());
    return response;
  }

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param username
   *          用户id
   * @param checked
   */
  public void checkAllShopCart(String username, boolean checked) {
    shopCartRepository.checkAllShopCart(username, checked);
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param username
   *          用户id
   * @param shopCartId
   * @param checked
   */
  public void checkShopCart(String username, String shopCartId, boolean checked) {
    ShopCartDTO shopCartDTO = shopCartRepository.findOne(shopCartId);
    if (null == shopCartDTO) {
      return;
    }
    if (!username.equals(shopCartDTO.getUsername())) {
      throw new AccessDeniedException("no permission check other user's ShopCart");
    }
    shopCartDTO.setChecked(checked);
    shopCartRepository.saveAndFlush(shopCartDTO);
  }

  /**
   * 删除购物项
   * 
   * @param username
   *          用户id
   * @param shopCartId
   */
  public void deleteShopCart(String username, String shopCartId) {
    ShopCartDTO shopCartDTO = shopCartRepository.findOne(shopCartId);
    if (null == shopCartDTO) {
      return;
    }
    if (!username.equals(shopCartDTO.getUsername())) {
      throw new AccessDeniedException("no permission delete other user's ShopCart");
    }
    shopCartRepository.delete(shopCartId);
  }

  /**
   * 修改购物项商品数量
   * 
   * @param username
   *          用户id
   * @param shopCartId
   * @param count
   * @return
   */
  public ShopCart updateShopCartCount(String username, String shopCartId, int count) {
    ShopCartDTO shopCartDTO = shopCartRepository.findOne(shopCartId);
    if (null == shopCartDTO) {
      throw new IllegalArgumentException("ShopCart item not exist");
    }
    if (count <= 0) {
      return convertService.convertShopCartDTO(shopCartDTO);
    }

    if (!username.equals(shopCartDTO.getUsername())) {
      throw new AccessDeniedException("no permission chage other user's ShopCart");
    }
    shopCartDTO.setCount(count);
    return convertService.convertShopCartDTO(shopCartRepository.saveAndFlush(shopCartDTO));
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
