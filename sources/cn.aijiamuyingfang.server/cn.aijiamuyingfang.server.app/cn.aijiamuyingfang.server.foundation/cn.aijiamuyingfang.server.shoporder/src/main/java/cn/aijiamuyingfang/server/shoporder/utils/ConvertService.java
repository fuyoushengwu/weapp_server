package cn.aijiamuyingfang.server.shoporder.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.CouponClient;
import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.server.feign.StoreClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.dto.PreviewOrderDTO;
import cn.aijiamuyingfang.server.shoporder.dto.PreviewOrderItemDTO;
import cn.aijiamuyingfang.server.shoporder.dto.SendTypeDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopCartDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderItemDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderStatusDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderVoucherDTO;
import cn.aijiamuyingfang.vo.review.PreviewOrder;
import cn.aijiamuyingfang.vo.review.PreviewOrderItem;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
import cn.aijiamuyingfang.vo.shoporder.SendType;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderItem;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.vo.utils.StringUtils;

@Service
public class ConvertService {
  @Autowired
  private StoreClient storeClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private CouponClient couponClient;

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private ShopCartRepository shopcartRepository;

  public PreviewOrder convertPreviewOrderDTO(PreviewOrderDTO previewOrderDTO) {
    if (null == previewOrderDTO) {
      return null;
    }
    PreviewOrder previewOrder = new PreviewOrder();
    previewOrder.setId(previewOrderDTO.getId());
    previewOrder.setUsername(previewOrderDTO.getUsername());
    if (StringUtils.hasContent(previewOrderDTO.getRecieveAddressId())
        && StringUtils.hasContent(previewOrderDTO.getUsername())) {
      previewOrder.setRecieveAddress(
          userClient.getRecieveAddress(previewOrderDTO.getUsername(), previewOrderDTO.getRecieveAddressId()).getData());
    }
    previewOrder.setOrderItemList(convertPreviewOrderItemDTOList(previewOrderDTO.getOrderItemList()));
    return previewOrder;
  }

  public PreviewOrderDTO convertPreviewOrder(PreviewOrder previewOrder) {
    if (null == previewOrder) {
      return null;
    }
    PreviewOrderDTO previewOrderDTO = new PreviewOrderDTO();
    previewOrderDTO.setId(previewOrder.getId());
    previewOrderDTO.setUsername(previewOrder.getUsername());
    if (previewOrder.getRecieveAddress() != null) {
      previewOrderDTO.setRecieveAddressId(previewOrder.getRecieveAddress().getId());
    }
    previewOrderDTO.setOrderItemList(convertPreviewOrderItemList(previewOrder.getOrderItemList()));
    return previewOrderDTO;
  }

  public PreviewOrderItem convertPreviewOrderItemDTO(PreviewOrderItemDTO previewOrderItemDTO) {
    if (null == previewOrderItemDTO) {
      return null;
    }
    PreviewOrderItem previewOrderItem = new PreviewOrderItem();
    previewOrderItem.setId(previewOrderItemDTO.getId());
    previewOrderItem.setCount(previewOrderItemDTO.getCount());
    if (StringUtils.hasContent(previewOrderItemDTO.getShopCartId())) {
      previewOrderItem.setShopCart(convertShopCartDTO(shopcartRepository.findOne(previewOrderItemDTO.getShopCartId())));
    }
    if (StringUtils.hasContent(previewOrderItemDTO.getGoodId())) {
      previewOrderItem.setGood(goodClient.getGood(previewOrderItemDTO.getGoodId()).getData());
    }
    return previewOrderItem;
  }

  public List<PreviewOrderItem> convertPreviewOrderItemDTOList(List<PreviewOrderItemDTO> previewOrderItemDTOList) {
    List<PreviewOrderItem> previewOrderItemList = new ArrayList<>();
    if (null == previewOrderItemDTOList) {
      return previewOrderItemList;
    }
    for (PreviewOrderItemDTO previewOrderItemDTO : previewOrderItemDTOList) {
      PreviewOrderItem previewOrderItem = convertPreviewOrderItemDTO(previewOrderItemDTO);
      if (previewOrderItem != null) {
        previewOrderItemList.add(previewOrderItem);
      }
    }
    return previewOrderItemList;
  }

  public PreviewOrderItemDTO convertPreviewOrderItem(PreviewOrderItem previewOrderItem) {
    if (null == previewOrderItem) {
      return null;
    }
    PreviewOrderItemDTO previewOrderItemDTO = new PreviewOrderItemDTO();
    previewOrderItemDTO.setId(previewOrderItem.getId());
    previewOrderItemDTO.setCount(previewOrderItem.getCount());
    if (previewOrderItem.getShopCart() != null) {
      previewOrderItemDTO.setShopCartId(previewOrderItem.getShopCart().getId());
    }
    if (previewOrderItem.getGood() != null) {
      previewOrderItemDTO.setGoodId(previewOrderItem.getGood().getId());
    }
    return previewOrderItemDTO;
  }

  public List<PreviewOrderItemDTO> convertPreviewOrderItemList(List<PreviewOrderItem> previewOrderItemList) {
    List<PreviewOrderItemDTO> previewOrderItemDTOList = new ArrayList<>();
    if (null == previewOrderItemList) {
      return previewOrderItemDTOList;
    }
    for (PreviewOrderItem previewOrderItem : previewOrderItemList) {
      PreviewOrderItemDTO previewOrderItemDTO = convertPreviewOrderItem(previewOrderItem);
      if (previewOrderItemDTO != null) {
        previewOrderItemDTOList.add(previewOrderItemDTO);
      }
    }
    return previewOrderItemDTOList;
  }

  public ShopCart convertShopCartDTO(ShopCartDTO shopCartDTO) {
    if (null == shopCartDTO) {
      return null;
    }
    ShopCart shopCart = new ShopCart();
    shopCart.setId(shopCartDTO.getId());
    shopCart.setUsername(shopCartDTO.getUsername());
    if (StringUtils.hasContent(shopCartDTO.getGoodId())) {
      shopCart.setGood(goodClient.getGood(shopCartDTO.getGoodId()).getData());
    }
    shopCart.setChecked(shopCartDTO.isChecked());
    shopCart.setCount(shopCartDTO.getCount());
    return shopCart;
  }

  public List<ShopCart> convertShopCartDTOList(List<ShopCartDTO> shopCartDTOList) {
    List<ShopCart> shopcartList = new ArrayList<>();
    if (null == shopCartDTOList) {
      return shopcartList;
    }
    for (ShopCartDTO shopCartDTO : shopCartDTOList) {
      ShopCart shopCart = convertShopCartDTO(shopCartDTO);
      if (shopCart != null) {
        shopcartList.add(shopCart);
      }
    }
    return shopcartList;
  }

  public ShopCartDTO convertShopCart(ShopCart shopCart) {
    if (null == shopCart) {
      return null;
    }
    ShopCartDTO shopCartDTO = new ShopCartDTO();
    shopCartDTO.setId(shopCart.getId());
    shopCartDTO.setUsername(shopCart.getUsername());
    if (shopCart.getGood() != null) {
      shopCartDTO.setGoodId(shopCart.getGood().getId());
    }
    shopCartDTO.setChecked(shopCart.isChecked());
    shopCartDTO.setCount(shopCart.getCount());
    return shopCartDTO;
  }

  public ShopOrder convertShopOrderDTO(ShopOrderDTO shoporderDTO) {
    if (null == shoporderDTO) {
      return null;
    }
    ShopOrder shoporder = new ShopOrder();
    shoporder.setId(shoporderDTO.getId());
    shoporder.setUsername(shoporderDTO.getUsername());
    shoporder.setOrderNo(shoporderDTO.getOrderNo());
    shoporder.setStatus(convertShopOrderStatusDTO(shoporderDTO.getStatus()));
    shoporder.setFromPreOrder(shoporderDTO.isFromPreOrder());
    shoporder.setSendType(convertSendTypeDTO(shoporderDTO.getSendType()));
    shoporder.setCreateTime(shoporderDTO.getCreateTime());
    shoporder.setFinishTime(shoporderDTO.getFinishTime());
    shoporder.setPickupTime(shoporderDTO.getPickupTime());
    if (StringUtils.hasContent(shoporderDTO.getPickupStoreAddressId())) {
      shoporder
          .setStoreAddress(storeClient.getStoreAddressByAddressId(shoporderDTO.getPickupStoreAddressId()).getData());
    }
    if (StringUtils.hasContent(shoporderDTO.getRecieveAddressId())
        && StringUtils.hasContent(shoporderDTO.getUsername())) {
      shoporder.setRecieveAddress(
          userClient.getRecieveAddress(shoporderDTO.getUsername(), shoporderDTO.getRecieveAddressId()).getData());
    }
    shoporder.setOrderItemList(convertShopOrderItemDTOList(shoporderDTO.getOrderItemList()));
    shoporder.setThirdsendCompany(shoporderDTO.getThirdsendCompany());
    shoporder.setThirdsendNo(shoporderDTO.getThirdsendNo());
    shoporder.setOperator(shoporderDTO.getOperator());
    shoporder.setLastModify(shoporderDTO.getLastModify());
    shoporder.setBuyerMessage(shoporderDTO.getBuyerMessage());
    shoporder.setFormid(shoporderDTO.getFormid());
    shoporder.setTotalGoodsPrice(shoporderDTO.getTotalGoodsPrice());
    shoporder.setSendPrice(shoporderDTO.getSendPrice());
    shoporder.setScore(shoporderDTO.getScore());
    shoporder
        .setOrderVoucher(convertShopOrderVoucherDTOList(shoporderDTO.getUsername(), shoporderDTO.getOrderVoucher()));
    shoporder.setTotalPrice(shoporderDTO.getTotalPrice());
    return shoporder;
  }

  public List<ShopOrder> convertShopOrderDTOList(List<ShopOrderDTO> shopOrderDTOList) {
    List<ShopOrder> shoporderList = new ArrayList<>();
    if (null == shopOrderDTOList) {
      return shoporderList;
    }
    for (ShopOrderDTO shoporderDTO : shopOrderDTOList) {
      ShopOrder shopOrder = convertShopOrderDTO(shoporderDTO);
      if (shopOrder != null) {
        shoporderList.add(shopOrder);
      }
    }
    return shoporderList;
  }

  public ShopOrderDTO convertShopOrder(ShopOrder shopOrder) {
    if (null == shopOrder) {
      return null;
    }
    ShopOrderDTO shopOrderDTO = new ShopOrderDTO();
    shopOrderDTO.setId(shopOrder.getId());
    shopOrderDTO.setUsername(shopOrder.getUsername());
    shopOrderDTO.setOrderNo(shopOrder.getOrderNo());
    shopOrderDTO.setStatus(convertShopOrderStatus(shopOrder.getStatus()));
    shopOrderDTO.setFromPreOrder(shopOrder.isFromPreOrder());
    shopOrderDTO.setSendType(convertSendType(shopOrder.getSendType()));
    shopOrderDTO.setCreateTime(shopOrder.getCreateTime());
    shopOrderDTO.setFinishTime(shopOrder.getFinishTime());
    shopOrderDTO.setPickupTime(shopOrder.getPickupTime());
    if (shopOrder.getStoreAddress() != null) {
      shopOrderDTO.setPickupStoreAddressId(shopOrder.getStoreAddress().getId());
    }
    if (shopOrder.getRecieveAddress() != null) {
      shopOrderDTO.setRecieveAddressId(shopOrder.getRecieveAddress().getId());
    }
    shopOrderDTO.setOrderItemList(convertShopOrderItemList(shopOrder.getOrderItemList()));
    shopOrderDTO.setThirdsendCompany(shopOrder.getThirdsendCompany());
    shopOrderDTO.setThirdsendNo(shopOrder.getThirdsendNo());
    shopOrderDTO.setOperator(shopOrder.getOperator());
    shopOrderDTO.setLastModify(shopOrder.getLastModify());
    shopOrderDTO.setBuyerMessage(shopOrder.getBuyerMessage());
    shopOrderDTO.setFormid(shopOrder.getFormid());
    shopOrderDTO.setTotalGoodsPrice(shopOrder.getTotalGoodsPrice());
    shopOrderDTO.setSendPrice(shopOrder.getSendPrice());
    shopOrderDTO.setScore(shopOrder.getScore());
    shopOrderDTO.setTotalPrice(shopOrder.getTotalPrice());
    shopOrderDTO.setOrderVoucher(convertShopOrderVoucherList(shopOrder.getOrderVoucher()));
    return shopOrderDTO;
  }

  public ShopOrderVoucher convertShopOrderVoucherDTO(String username, ShopOrderVoucherDTO shoporderVoucherDTO) {
    if (null == shoporderVoucherDTO) {
      return null;
    }
    ShopOrderVoucher shoporderVoucher = new ShopOrderVoucher();
    shoporderVoucher.setId(shoporderVoucherDTO.getId());
    if (StringUtils.hasContent(shoporderVoucherDTO.getUservoucherId()) && StringUtils.hasContent(username)) {
      shoporderVoucher
          .setUserVoucher(couponClient.getUserVoucher(username, shoporderVoucherDTO.getUservoucherId()).getData());
    }
    if (StringUtils.hasContent(shoporderVoucherDTO.getVoucherItemId())) {
      shoporderVoucher.setVoucherItem(couponClient.getVoucherItem(shoporderVoucherDTO.getVoucherItemId()).getData());
    }
    if (StringUtils.hasContent(shoporderVoucherDTO.getGoodId())) {
      shoporderVoucher.setGood(goodClient.getGood(shoporderVoucherDTO.getGoodId()).getData());
    }
    return shoporderVoucher;
  }

  public List<ShopOrderVoucher> convertShopOrderVoucherDTOList(String username,
      List<ShopOrderVoucherDTO> shoporderVoucherDTOList) {
    List<ShopOrderVoucher> shoporderVoucherList = new ArrayList<>();
    if (null == shoporderVoucherDTOList) {
      return shoporderVoucherList;
    }
    for (ShopOrderVoucherDTO shoporderVoucherDTO : shoporderVoucherDTOList) {
      ShopOrderVoucher shoporderVoucher = convertShopOrderVoucherDTO(username, shoporderVoucherDTO);
      if (shoporderVoucher != null) {
        shoporderVoucherList.add(shoporderVoucher);
      }
    }
    return shoporderVoucherList;
  }

  public ShopOrderVoucherDTO convertShopOrderVoucher(ShopOrderVoucher shoporderVoucher) {
    if (null == shoporderVoucher) {
      return null;
    }
    ShopOrderVoucherDTO shoporderVoucherDTO = new ShopOrderVoucherDTO();
    shoporderVoucherDTO.setId(shoporderVoucher.getId());
    if (shoporderVoucher.getUserVoucher() != null) {
      shoporderVoucherDTO.setUservoucherId(shoporderVoucher.getUserVoucher().getId());
    }
    if (shoporderVoucher.getVoucherItem() != null) {
      shoporderVoucherDTO.setVoucherItemId(shoporderVoucher.getVoucherItem().getId());
    }
    if (shoporderVoucher.getGood() != null) {
      shoporderVoucherDTO.setGoodId(shoporderVoucher.getGood().getId());
    }
    return shoporderVoucherDTO;
  }

  public List<ShopOrderVoucherDTO> convertShopOrderVoucherList(List<ShopOrderVoucher> shoporderVoucherList) {
    List<ShopOrderVoucherDTO> shoporderVoucherDTOList = new ArrayList<>();
    if (null == shoporderVoucherList) {
      return shoporderVoucherDTOList;
    }
    for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
      ShopOrderVoucherDTO shoporderVoucherDTO = convertShopOrderVoucher(shoporderVoucher);
      if (shoporderVoucherDTO != null) {
        shoporderVoucherDTOList.add(shoporderVoucherDTO);
      }
    }
    return shoporderVoucherDTOList;
  }

  public ShopOrderItem convertShopOrderItemDTO(ShopOrderItemDTO shoporderItemDTO) {
    if (null == shoporderItemDTO) {
      return null;
    }
    ShopOrderItem shoporderItem = new ShopOrderItem();
    shoporderItem.setId(shoporderItemDTO.getId());
    if (StringUtils.hasContent(shoporderItemDTO.getGoodId())) {
      shoporderItem.setGood(goodClient.getGood(shoporderItemDTO.getGoodId()).getData());
    }
    shoporderItem.setCount(shoporderItemDTO.getCount());
    shoporderItem.setPrice(shoporderItemDTO.getPrice());
    return shoporderItem;
  }

  public List<ShopOrderItem> convertShopOrderItemDTOList(List<ShopOrderItemDTO> shoporderItemDTOList) {
    List<ShopOrderItem> shoporderItemList = new ArrayList<>();
    if (null == shoporderItemDTOList) {
      return shoporderItemList;
    }
    for (ShopOrderItemDTO shoporderItemDTO : shoporderItemDTOList) {
      ShopOrderItem shoporderItem = convertShopOrderItemDTO(shoporderItemDTO);
      if (shoporderItem != null) {
        shoporderItemList.add(shoporderItem);
      }
    }
    return shoporderItemList;
  }

  public ShopOrderItemDTO convertShopOrderItem(ShopOrderItem shoporderItem) {
    if (null == shoporderItem) {
      return null;
    }
    ShopOrderItemDTO shoporderItemDTO = new ShopOrderItemDTO();
    shoporderItemDTO.setId(shoporderItem.getId());
    if (shoporderItem.getGood() != null) {
      shoporderItemDTO.setGoodId(shoporderItem.getGood().getId());
      shoporderItemDTO.setGoodName(shoporderItem.getGood().getName());
      shoporderItemDTO.setGoodPack(shoporderItem.getGood().getPack());
    }
    shoporderItemDTO.setCount(shoporderItem.getCount());
    shoporderItemDTO.setPrice(shoporderItem.getPrice());
    return shoporderItemDTO;
  }

  public List<ShopOrderItemDTO> convertShopOrderItemList(List<ShopOrderItem> shoporderItemList) {
    List<ShopOrderItemDTO> shoporderItemDTOList = new ArrayList<>();
    if (null == shoporderItemList) {
      return shoporderItemDTOList;
    }
    for (ShopOrderItem shoporderItem : shoporderItemList) {
      ShopOrderItemDTO shoporderItemDTO = convertShopOrderItem(shoporderItem);
      if (shoporderItemDTO != null) {
        shoporderItemDTOList.add(shoporderItemDTO);
      }
    }
    return shoporderItemDTOList;
  }

  public SendTypeDTO convertSendType(SendType sendType) {
    if (null == sendType) {
      return SendTypeDTO.UNKNOW;
    }
    for (SendTypeDTO sendtypeDTO : SendTypeDTO.values()) {
      if (sendtypeDTO.getValue() == sendType.getValue()) {
        return sendtypeDTO;
      }
    }
    return SendTypeDTO.UNKNOW;
  }

  public List<SendTypeDTO> convertSendTypeList(List<SendType> sendTypeList) {
    List<SendTypeDTO> sendtypeDTOList = new ArrayList<>();
    if (null == sendTypeList) {
      return sendtypeDTOList;
    }
    for (SendType sendType : sendTypeList) {
      SendTypeDTO sendTypeDTO = convertSendType(sendType);
      if (sendTypeDTO != null) {
        sendtypeDTOList.add(sendTypeDTO);
      }
    }
    return sendtypeDTOList;
  }

  public SendType convertSendTypeDTO(SendTypeDTO sendTypeDTO) {
    if (null == sendTypeDTO) {
      return SendType.UNKNOW;
    }
    for (SendType sendtype : SendType.values()) {
      if (sendTypeDTO.getValue() == sendtype.getValue()) {
        return sendtype;
      }
    }
    return SendType.UNKNOW;
  }

  public List<SendType> convertSendTypeDTOList(List<SendTypeDTO> sendTypeDTOList) {
    List<SendType> sendTypeList = new ArrayList<>();
    if (null == sendTypeDTOList) {
      return sendTypeList;
    }
    for (SendTypeDTO sendtypeDTO : sendTypeDTOList) {
      SendType sendType = convertSendTypeDTO(sendtypeDTO);
      if (sendType != null) {
        sendTypeList.add(sendType);
      }
    }
    return sendTypeList;
  }

  public ShopOrderStatusDTO convertShopOrderStatus(ShopOrderStatus shoporderStatus) {
    if (null == shoporderStatus) {
      return ShopOrderStatusDTO.UNKNOW;
    }
    for (ShopOrderStatusDTO shoporderStatusDTO : ShopOrderStatusDTO.values()) {
      if (shoporderStatusDTO.getValue() == shoporderStatus.getValue()) {
        return shoporderStatusDTO;
      }
    }
    return ShopOrderStatusDTO.UNKNOW;
  }

  public List<ShopOrderStatusDTO> convertShopOrderStatusList(List<ShopOrderStatus> shoporderStatusList) {
    List<ShopOrderStatusDTO> shoporderStatusDTOList = new ArrayList<>();
    if (null == shoporderStatusList) {
      return shoporderStatusDTOList;
    }
    for (ShopOrderStatus shoporderStatus : shoporderStatusList) {
      ShopOrderStatusDTO shopOrderStatusDTO = convertShopOrderStatus(shoporderStatus);
      if (shopOrderStatusDTO != null) {
        shoporderStatusDTOList.add(shopOrderStatusDTO);
      }
    }
    return shoporderStatusDTOList;
  }

  public ShopOrderStatus convertShopOrderStatusDTO(ShopOrderStatusDTO shoporderStatusDTO) {
    if (null == shoporderStatusDTO) {
      return ShopOrderStatus.UNKNOW;
    }
    for (ShopOrderStatus shoporderStatus : ShopOrderStatus.values()) {
      if (shoporderStatusDTO.getValue() == shoporderStatus.getValue()) {
        return shoporderStatus;
      }
    }
    return ShopOrderStatus.UNKNOW;
  }

  public List<ShopOrderStatus> convertShopOrderStatusDTOList(List<ShopOrderStatusDTO> shoporderStatusDTOList) {
    List<ShopOrderStatus> shoporderStatusList = new ArrayList<>();
    if (null == shoporderStatusDTOList) {
      return shoporderStatusList;
    }
    for (ShopOrderStatusDTO shoporderStatusDTO : shoporderStatusDTOList) {
      ShopOrderStatus shoporderStatus = convertShopOrderStatusDTO(shoporderStatusDTO);
      if (shoporderStatus != null) {
        shoporderStatusList.add(shoporderStatus);
      }
    }
    return shoporderStatusList;
  }
}
