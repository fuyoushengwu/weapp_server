package cn.aijiamuyingfang.server.shoporder.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderItemRepository;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.dto.PreviewOrderDTO;
import cn.aijiamuyingfang.server.shoporder.dto.PreviewOrderItemDTO;
import cn.aijiamuyingfang.server.shoporder.utils.ConvertService;
import cn.aijiamuyingfang.vo.exception.ShopOrderException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.review.PreviewOrder;
import cn.aijiamuyingfang.vo.review.PreviewOrderItem;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
import cn.aijiamuyingfang.vo.user.RecieveAddress;

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

  @Autowired
  private ConvertService convertService;

  /**
   * 更新预览的商品项
   * 
   * @param username
   *          用户id
   * @param previewItemId
   * @param updateOrderItem
   * @return
   */
  public PreviewOrderItem updatePreviewOrderItem(String username, String previewItemId,
      PreviewOrderItem updateOrderItem) {
    if (null == updateOrderItem) {
      return null;
    }
    PreviewOrderDTO previewOrderDTO = previeworderRepository.findByUsername(username);
    if (null == previewOrderDTO) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDER_NOT_EXIST, username);
    }
    PreviewOrderItemDTO orderItemDTO = previeworderItemRepository.findOne(previewItemId);
    if (null == orderItemDTO) {
      throw new ShopOrderException(ResponseCode.PREVIEWORDERITEM_NOT_EXIST, previewItemId);
    }
    if (!previewOrderDTO.getOrderItemList().contains(orderItemDTO)) {
      throw new AccessDeniedException("no permission update other user's preview item");
    }
    orderItemDTO.update(updateOrderItem);
    return convertService.convertPreviewOrderItemDTO(previeworderItemRepository.saveAndFlush(orderItemDTO));
  }

  /**
   * 删除预览的商品项
   * 
   * @param username
   *          用户id
   * @param itemid
   */
  public void deletePreviewOrderItem(String username, String itemid) {
    PreviewOrderDTO previewOrderDTO = previeworderRepository.findByUsername(username);
    if (null == previewOrderDTO) {
      return;
    }
    PreviewOrderItemDTO orderItemDTO = previeworderItemRepository.findOne(itemid);
    previewOrderDTO.getOrderItemList().remove(orderItemDTO);
    previeworderRepository.saveAndFlush(previewOrderDTO);
    previeworderItemRepository.delete(orderItemDTO.getId());
  }

  /**
   * 生成用户的预览订单
   * 
   * @param username
   *          用户id
   * @param goodIdList
   * @return
   */
  @Transactional
  public PreviewOrder generatePreviewOrder(String username, List<String> goodIdList) {
    PreviewOrder previeworder = new PreviewOrder();
    previeworder.setUsername(username);
    List<ShopCart> itemList = getShopCartListByGoodIds(username, goodIdList);
    for (ShopCart item : itemList) {
      previeworder.addOrderItem(PreviewOrderItem.fromShopCart(item));
    }

    List<RecieveAddress> addressList = userClient.getUserRecieveAddressList(username).getData();
    for (RecieveAddress address : addressList) {
      if (address.isDef()) {
        previeworder.setRecieveAddress(address);
        break;
      }
    }

    previeworderRepository.deleteByUsername(username);
    previeworderRepository.saveAndFlush(convertService.convertPreviewOrder(previeworder));
    return previeworder;
  }

  /**
   *
   * @param username
   *          用户id
   * @param goodIdList
   * @return
   */
  private List<ShopCart> getShopCartListByGoodIds(String username, List<String> goodIdList) {
    if (goodIdList != null && !goodIdList.isEmpty()) {
      return convertService.convertShopCartDTOList(shopCartRepository.findByUsernameAndGoodIdIn(username, goodIdList));
    } else {
      return convertService.convertShopCartDTOList(shopCartRepository.findByUsernameAndChecked(username, true));
    }
  }

}
