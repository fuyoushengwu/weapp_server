package cn.aijiamuyingfang.server.shoporder.service;

import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.server.domain.address.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.domain.shopcart.db.ShopCartItemRepository;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderItemRepository;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderRepository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private PreviewOrderItemRepository itemRepository;

	@Autowired
	private ShopCartItemRepository shopcartitemRepository;

	@Autowired
	private RecieveAddressRepository recieveaddressRepository;

	/**
	 * 更新预览的商品项
	 * 
	 * @param userid
	 * @param itemid
	 * @param updateOrderItem
	 * @return
	 */
	public PreviewOrderItem updatePreviewOrderItem(String userid, String itemid, PreviewOrderItem updateOrderItem) {
		if (null == updateOrderItem) {
			return null;
		}
		PreviewOrder previewOrder = previeworderRepository.findByUserid(userid);
		if (null == previewOrder) {
			throw new ShopOrderException(ResponseCode.PREVIEWORDER_NOT_EXIST, userid);
		}
		PreviewOrderItem orderItem = itemRepository.findOne(itemid);
		if (null == orderItem) {
			throw new ShopOrderException(ResponseCode.PREVIEWORDERITEM_NOT_EXIST, itemid);
		}
		if (!previewOrder.getOrderItemList().contains(orderItem)) {
			throw new AuthException("403", "no permission update other user's preview item");
		}
		orderItem.update(updateOrderItem);
		itemRepository.saveAndFlush(orderItem);
		return orderItem;
	}

	/**
	 * 删除预览的商品项
	 * 
	 * @param userid
	 * @param itemid
	 */
	public void deletePreviewOrderItem(String userid, String itemid) {
		PreviewOrder previewOrder = previeworderRepository.findByUserid(userid);
		if (null == previewOrder) {
			return;
		}
		PreviewOrderItem orderItem = itemRepository.findOne(itemid);
		previewOrder.getOrderItemList().remove(orderItem);
		previeworderRepository.saveAndFlush(previewOrder);
		itemRepository.delete(orderItem.getId());
	}

	/**
	 * 生成用户的预览订单
	 * 
	 * @param userid
	 * @param goodids
	 * @return
	 */
	@Transactional
	public PreviewOrder generatePreviewOrder(String userid, List<String> goodids) {
		PreviewOrder previeworder = new PreviewOrder();
		previeworder.setUserid(userid);
		List<ShopCartItem> itemList = getShopCartItemListByGoodIds(userid, goodids);
		for (ShopCartItem item : itemList) {
			previeworder.addOrderItem(PreviewOrderItem.fromShopCartItem(item));
		}

		List<RecieveAddress> addressList = recieveaddressRepository.findByUserid(userid);
		for (RecieveAddress address : addressList) {
			if (address.isDef()) {
				previeworder.setRecieveAddress(address);
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
	 * @param goodidList
	 * @return
	 */
	private List<ShopCartItem> getShopCartItemListByGoodIds(String userid, List<String> goodidList) {
		if (goodidList != null && !goodidList.isEmpty()) {
			return shopcartitemRepository.findByUseridAndGoodIdIn(userid, goodidList);
		} else {
			return shopcartitemRepository.findByUseridAndIschecked(userid, true);
		}
	}

}
