package cn.aijiamuyingfang.server.shoporder.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.ShopOrderException;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.feign.domain.user.RecieveAddress;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderItemRepository;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrder;
import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrderItem;
import cn.aijiamuyingfang.server.shoporder.domain.ShopCart;

/**
 * [描述]:
 * <p>
 * 预览订单服务Service层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 23:03:13
 */
@Service
public class PreviewOrderService {

  @Autowired
  private PreviewOrderRepository previeworderRepository;

  @Autowired
  private PreviewOrderItemRepository previeworderItemRepository;

  @Autowired
  private ShopCartRepository shopcartRepository;

  @Autowired
  private UserClient userClient;

  /**
   * 更新预览的商品项
   * 
   * @param userid
   *          用户id
   * @param previewitemId
   * @param updateOrderItem
   * @return
   */
  public PreviewOrderItem updatePreviewOrderItem(String userid, String previewitemId,
      PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return null;
    }
    PreviewOrder previewOrder = previeworderRepository.findByUserid(userid);
    if (null == previewOrder) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDER_NOT_EXIST, userid);
    }
    PreviewOrderItem orderItem = previeworderItemRepository.findOne(previewitemId);
    if (null == orderItem) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDERITEM_NOT_EXIST, previewitemId);
    }
    if (!previewOrder.getOrderItemList().contains(orderItem)) {
      throw new AccessDeniedException("no permission update other user's preview item");
    }
    orderItem.update(updateOrderItem);
    previeworderItemRepository.saveAndFlush(orderItem);
    return orderItem;
  }

  /**
   * 删除预览的商品项
   * 
   * @param userid
   *          用户id
   * @param itemid
   */
  public void deletePreviewOrderItem(String userid, String itemid) {
    PreviewOrder previewOrder = previeworderRepository.findByUserid(userid);
    if (null == previewOrder) {
      return;
    }
    PreviewOrderItem orderItem = previeworderItemRepository.findOne(itemid);
    previewOrder.getOrderItemList().remove(orderItem);
    previeworderRepository.saveAndFlush(previewOrder);
    previeworderItemRepository.delete(orderItem.getId());
  }

  /**
   * 生成用户的预览订单
   * 
   * @param userid
   *          用户id
   * @param goodids
   * @return
   */
  @Transactional
  public PreviewOrder generatePreviewOrder(String userid, List<String> goodids) {
    PreviewOrder previeworder = new PreviewOrder();
    previeworder.setUserid(userid);
    List<ShopCart> itemList = getShopCartListByGoodIds(userid, goodids);
    for (ShopCart item : itemList) {
      previeworder.addOrderItem(PreviewOrderItem.fromShopCart(item));
    }

    List<RecieveAddress> addressList = userClient.getUserRecieveAddressList(userid).getData();
    for (RecieveAddress address : addressList) {
      if (address.isDef()) {
        previeworder.setRecieveAddressId(address.getId());
        break;
      }
    }

    previeworderRepository.deleteByUserid(userid);
    previeworderRepository.saveAndFlush(previeworder);
    return previeworder;
  }

  /**
   *
   * @param userid
   *          用户id
   * @param goodidList
   * @return
   */
  private List<ShopCart> getShopCartListByGoodIds(String userid, List<String> goodidList) {
    if (goodidList != null && !goodidList.isEmpty()) {
      return shopcartRepository.findByUseridAndGoodIdIn(userid, goodidList);
    } else {
      return shopcartRepository.findByUseridAndIschecked(userid, true);
    }
  }

}
