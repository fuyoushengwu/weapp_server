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
  private ShopCartRepository shopCartRepository;

  @Autowired
  private UserClient userClient;

  /**
   * 更新预览的商品项
   * 
   * @param userId
   *          用户id
   * @param previewItemId
   * @param updateOrderItem
   * @return
   */
  public PreviewOrderItem updatePreviewOrderItem(String userId, String previewItemId,
      PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return null;
    }
    PreviewOrder previewOrder = previeworderRepository.findByUserId(userId);
    if (null == previewOrder) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDER_NOT_EXIST, userId);
    }
    PreviewOrderItem orderItem = previeworderItemRepository.findOne(previewItemId);
    if (null == orderItem) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDERITEM_NOT_EXIST, previewItemId);
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
   * @param userId
   *          用户id
   * @param itemid
   */
  public void deletePreviewOrderItem(String userId, String itemid) {
    PreviewOrder previewOrder = previeworderRepository.findByUserId(userId);
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
   * @param userId
   *          用户id
   * @param goodIdList
   * @return
   */
  @Transactional
  public PreviewOrder generatePreviewOrder(String userId, List<String> goodIdList) {
    PreviewOrder previeworder = new PreviewOrder();
    previeworder.setUserId(userId);
    List<ShopCart> itemList = getShopCartListByGoodIds(userId, goodIdList);
    for (ShopCart item : itemList) {
      previeworder.addOrderItem(PreviewOrderItem.fromShopCart(item));
    }

    List<RecieveAddress> addressList = userClient.getUserRecieveAddressList(userId).getData();
    for (RecieveAddress address : addressList) {
      if (address.isDef()) {
        previeworder.setRecieveAddressId(address.getId());
        break;
      }
    }

    previeworderRepository.deleteByUserId(userId);
    previeworderRepository.saveAndFlush(previeworder);
    return previeworder;
  }

  /**
   *
   * @param userId
   *          用户id
   * @param goodIdList
   * @return
   */
  private List<ShopCart> getShopCartListByGoodIds(String userId, List<String> goodIdList) {
    if (goodIdList != null && !goodIdList.isEmpty()) {
      return shopCartRepository.findByUserIdAndGoodIdIn(userId, goodIdList);
    } else {
      return shopCartRepository.findByUserIdAndIschecked(userId, true);
    }
  }

}
