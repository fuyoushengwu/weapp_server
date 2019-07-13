package cn.aijiamuyingfang.server.shoporder.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import cn.aijiamuyingfang.server.feign.CouponClient;
import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.server.feign.StoreClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.shoporder.dto.SendTypeDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderItemDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderStatusDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderVoucherDTO;
import cn.aijiamuyingfang.server.shoporder.utils.ConvertService;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.PagableUserVoucherList;
import cn.aijiamuyingfang.vo.coupon.UserVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.exception.ShopOrderException;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.goods.SaleGood;
import cn.aijiamuyingfang.vo.preorder.PagablePreOrderGoodList;
import cn.aijiamuyingfang.vo.preorder.PreOrderGood;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.review.PreviewOrder;
import cn.aijiamuyingfang.vo.review.PreviewOrderItem;
import cn.aijiamuyingfang.vo.shoporder.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.vo.shoporder.CreateShopOrderRequest;
import cn.aijiamuyingfang.vo.shoporder.PagableShopOrderList;
import cn.aijiamuyingfang.vo.shoporder.SendType;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderItem;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.vo.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.utils.CollectionUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;

/***
 * [描述]:
 * <p>
 * 订单服务Service层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 23:03:30
 */
@Service
public class ShopOrderService {
  @Autowired
  private CouponClient couponClient;

  @Autowired
  private ShopOrderRepository shopOrderRepository;

  @Autowired
  private PreviewOrderRepository previeworderRepository;

  @Autowired
  private ShopCartRepository shopCartRepository;

  @Autowired
  private UserClient userClient;

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private StoreClient storeClient;

  @Autowired
  private TemplateMsgService templateMsgService;

  @Autowired
  private ConvertService convertService;

  /**
   * 分页获取订单
   *
   * @param username
   *          用户id
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @return
   */
  public PagableShopOrderList getUserShopOrderList(String username, List<ShopOrderStatus> statusList,
      List<SendType> sendTypeList, int currentPage, int pageSize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(username, statusList, sendTypeList, currentPage, pageSize);
    PagableShopOrderList response = new PagableShopOrderList();
    response.setCurrentPage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;

  }

  /**
   * 分页获取所有的订单信息
   *
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @return
   */
  public PagableShopOrderList getShopOrderList(List<ShopOrderStatus> statusList, List<SendType> sendTypeList,
      int currentPage, int pageSize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(null, statusList, sendTypeList, currentPage, pageSize);
    PagableShopOrderList response = new PagableShopOrderList();
    response.setCurrentPage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;
  }

  /**
   * 分页获取订单
   *
   * @param username
   *          用户id
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @return
   */
  private Page<ShopOrder> getUserShopOrderPage(String username, List<ShopOrderStatus> statusList,
      List<SendType> sendTypeList, int currentPage, int pageSize) {
    if (null == statusList || statusList.isEmpty()) {
      statusList = new ArrayList<>();
      for (ShopOrderStatus status : ShopOrderStatus.values()) {
        statusList.add(status);
      }
    }

    if (null == sendTypeList || sendTypeList.isEmpty()) {
      sendTypeList = new ArrayList<>();
      for (SendType type : SendType.values()) {
        sendTypeList.add(type);
      }
    }

    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (statusList.size() == 1 && statusList.get(0).equals(ShopOrderStatus.FINISHED)) {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "finishTime");
    } else {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "orderNo");
    }
    Page<ShopOrderDTO> shoporderDTOList = null;
    if (StringUtils.hasContent(username)) {
      shoporderDTOList = shopOrderRepository.findByUsernameAndStatusInAndSendTypeIn(username,
          convertService.convertShopOrderStatusList(statusList), convertService.convertSendTypeList(sendTypeList),
          pageRequest);

    } else {
      shoporderDTOList = shopOrderRepository.findByStatusInAndSendTypeIn(
          convertService.convertShopOrderStatusList(statusList), convertService.convertSendTypeList(sendTypeList),
          pageRequest);
    }
    List<ShopOrder> content = new ArrayList<>();
    content.addAll(convertService.convertShopOrderDTOList(shoporderDTOList.getContent()));

    return new PageImpl<>(content, pageRequest, shoporderDTOList.getTotalElements());

  }

  /**
   * 更新订单状态(在送货时调用)
   * 
   * @param shopOrderId
   * @param requestBean
   */
  public void updateShopOrderStatus(String shopOrderId, UpdateShopOrderStatusRequest requestBean) {
    ShopOrder shoporder = convertService.convertShopOrderDTO(shopOrderRepository.findOne(shopOrderId));
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    ShopOrderStatus updateStatus = requestBean.getStatus();
    if (shoporder.getStatus() == updateStatus) {
      throw new ShopOrderException("500", "shoporder has get target status");
    }

    User user = userClient.getUserInternal(shoporder.getUsername()).getData();
    if (null == user) {
      throw new ShopOrderException(ResponseCode.USER_NOT_EXIST, shoporder.getUsername());
    }

    if (updateStatus != null) {
      shoporder.setStatus(updateStatus);
    }
    shoporder.setThirdsendCompany(requestBean.getThirdsendCompany());
    shoporder.setThirdsendNo(requestBean.getThirdsendno());
    shoporder.addOperator(requestBean.getOperator());

    if (updateStatus == ShopOrderStatus.DOING) {
      // 当开始发货的时候,才减去商品数量，面对买家恶意刷单
      List<SaleGood> saleGoodList = new ArrayList<>();
      for (ShopOrderItem shoporderGood : shoporder.getOrderItemList()) {
        saleGoodList.add(new SaleGood(shoporderGood.getGood().getId(), shoporderGood.getCount()));
      }
      goodClient.saleGoodList(saleGoodList);

      switch (shoporder.getSendType()) {
      case OWNSEND:
        templateMsgService.sendOwnSendMsg(user.getUsername(), shoporder);
        break;
      case THIRDSEND:
        templateMsgService.sendThirdSendMsg(user.getUsername(), shoporder);
        break;
      case PICKUP:
        templateMsgService.sendPickupMsg(user.getUsername(), shoporder);
        break;
      default:
        break;
      }
      shopOrderRepository.saveAndFlush(convertService.convertShopOrder(shoporder));
    }
  }

  /**
   * 如果订单已经完成100天,可以删除
   *
   * @param shopOrderId
   */
  public void delete100DaysFinishedShopOrder(String shopOrderId) {
    ShopOrderDTO shopOrderDTO = shopOrderRepository.findOne(shopOrderId);
    if (shopOrderDTO != null && shopOrderDTO.getStatus().equals(ShopOrderStatusDTO.FINISHED)
        && shopOrderDTO.getLastModifyTime() > 100) {
      shopOrderRepository.delete(shopOrderDTO);
    }
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   *
   * @param username
   *          用户id
   * @param shopOrderId
   */
  public void deleteUserShopOrder(String username, String shopOrderId) {
    ShopOrderDTO shopOrderDTO = shopOrderRepository.findOne(shopOrderId);
    if (null == shopOrderDTO) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }

    User user = userClient.getUserInternal(username).getData();
    if (null == user) {
      throw new ShopOrderException("404", "shoporder owner not exist");
    }

    if (ShopOrderStatusDTO.DOING.equals(shopOrderDTO.getStatus())) {
      throw new ShopOrderException("500", "shoporder is doing,cannot be deleted");
    }
    if (!username.equals(shopOrderDTO.getUsername())) {
      throw new AccessDeniedException("no permission delete other user's shoporder");
    }

    if (ShopOrderStatusDTO.UNSTART.equals(shopOrderDTO.getStatus())) {
      List<ShopOrderVoucherDTO> shoporderVoucherDTOList = shopOrderDTO.getOrderVoucher();
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucherDTO shoporderVoucherDTO : shoporderVoucherDTOList) {
        UserVoucher userVoucher = couponClient.getUserVoucher(username, shoporderVoucherDTO.getUservoucherId())
            .getData();
        VoucherItem voucherItem = couponClient.getVoucherItem(shoporderVoucherDTO.getVoucherItemId()).getData();
        userVoucher.increaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(username, updateUserVoucherList);

      user.increaseGenericScore(shopOrderDTO.getScore());
      userClient.updateUser(username, user);
    }

    if (shopOrderDTO.getStatus() != ShopOrderStatusDTO.DOING) {
      shopOrderRepository.delete(shopOrderDTO);
    }
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   *
   * @param username
   *          用户id
   * @param shopOrderId
   * @return
   */
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(String username, String shopOrderId) {
    ShopOrderDTO shoporderDTO = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporderDTO) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (shoporderDTO.getStatus() != ShopOrderStatusDTO.DOING) {
      throw new ShopOrderException("500", "only doing shoporder can be confirmed");
    }
    if (!username.equals(shoporderDTO.getUsername())) {
      throw new AccessDeniedException("no permission confirm other user's shoporder");
    }

    ConfirmShopOrderFinishedResponse response = new ConfirmShopOrderFinishedResponse();
    shoporderDTO.setStatus(ShopOrderStatusDTO.FINISHED);
    shoporderDTO.setFinishTime(new Date());
    shopOrderRepository.saveAndFlush(shoporderDTO);

    User user = userClient.getUserInternal(username).getData();
    if (null == user) {
      return response;
    }
    List<UserVoucher> updateUserVoucherList = new ArrayList<>();
    for (ShopOrderItemDTO shoporderItemDTO : shoporderDTO.getOrderItemList()) {
      Good good = goodClient.getGood(shoporderItemDTO.getGoodId()).getData();
      int genericScore = good.getScore() * shoporderItemDTO.getCount();
      user.increaseGenericScore(genericScore);
      response.addGenericScore(genericScore);

      GoodVoucher goodvoucher = good.getGoodVoucher();
      if (goodvoucher != null) {
        UserVoucher uservoucher = couponClient.getUserVoucherForGoodVoucher(username, goodvoucher.getId()).getData();
        if (null == uservoucher) {
          uservoucher = new UserVoucher();
          uservoucher.setGoodVoucher(goodvoucher);
          uservoucher.setUsername(username);
        }
        int voucherScore = goodvoucher.getScore() * shoporderItemDTO.getCount();
        uservoucher.increaseScore(voucherScore);
        response.addVoucherScore(voucherScore);
        updateUserVoucherList.add(uservoucher);
      }
    }
    couponClient.updateUserVoucherList(username, updateUserVoucherList);
    userClient.updateUser(username, user);
    return response;
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   *
   * @param username
   *          用户id
   * @param shopOrderId
   * @param addressId
   */
  public void updateUserShopOrderRecieveAddress(String username, String shopOrderId, String addressId) {
    ShopOrderDTO shoporderDTO = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporderDTO) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (!username.equals(shoporderDTO.getUsername())) {
      throw new AccessDeniedException("no permission update other user's shoporder recieve address");
    }

    if (shoporderDTO.getStatus() != ShopOrderStatusDTO.PREORDER
        && shoporderDTO.getStatus() != ShopOrderStatusDTO.UNSTART) {
      throw new ShopOrderException("500", "only preorder or unstart shoporder can update recieve address");
    }

    shoporderDTO.setRecieveAddressId(addressId);
    shopOrderRepository.saveAndFlush(shoporderDTO);
  }

  /**
   * 分页获取已完成预约单
   *
   * @param currentPage
   * @param pageSize
   * @return
   */
  public PagableShopOrderList getFinishedPreOrderList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "finishTime");
    Page<ShopOrderDTO> shoporderDTOPage = shopOrderRepository.findByStatus(ShopOrderStatusDTO.FINISHED, pageRequest);
    PagableShopOrderList response = new PagableShopOrderList();
    response.setCurrentPage(shoporderDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertShopOrderDTOList(shoporderDTOPage.getContent()));
    response.setTotalpage(shoporderDTOPage.getTotalPages());
    return response;
  }

  /**
   * 获得购买商品时可用的兑换券
   * 
   * @param username
   * @param goodIdList
   * @return
   */
  public List<ShopOrderVoucher> getUserShopOrderVoucherList(String username, List<String> goodIdList) {
    List<ShopOrderVoucher> shoporderVoucherList = new ArrayList<>();
    PagableUserVoucherList response = couponClient.getUserVoucherList(username, 1, Integer.MAX_VALUE).getData();
    for (UserVoucher uservoucher : response.getDataList()) {
      for (VoucherItem item : uservoucher.getGoodVoucher().getVoucherItemList()) {
        if (item != null && goodIdList.contains(item.getGoodId()) && item.getScore() <= uservoucher.getScore()) {
          ShopOrderVoucher shoporderVoucher = new ShopOrderVoucher();
          shoporderVoucher.setGood(goodClient.getGood(item.getGoodId()).getData());
          shoporderVoucher.setUserVoucher(uservoucher);
          shoporderVoucher.setVoucherItem(item);
          shoporderVoucherList.add(shoporderVoucher);
          break;
        }
      }
    }
    return shoporderVoucherList;
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   *
   * @param username
   *          用户id
   * @param shopOrderId
   * @return
   */
  public ShopOrder getUserShopOrder(String username, String shopOrderId) {
    ShopOrderDTO shoporderDTO = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporderDTO) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (!username.equals(shoporderDTO.getUsername())) {
      throw new AccessDeniedException("no permission get other user's shoporder");
    }
    return convertService.convertShopOrderDTO(shoporderDTO);
  }

  /**
   * 创建用户订单
   * 
   * @param username
   * @param request
   * @return
   */
  public ShopOrder createUserShopOrder(String username, CreateShopOrderRequest request) {
    PreviewOrder previeworder = convertService.convertPreviewOrderDTO(previeworderRepository.findByUsername(username));
    if (null == previeworder) {
      throw new ShopOrderException("500", "user should first preview order and then create shoporder");
    }
    ShopOrder shoporder = new ShopOrder();
    shoporder.setCreateTime(new Date());
    shoporder.setSendType(request.getSendType());
    if (SendType.PICKUP.equals(request.getSendType())) {
      shoporder.setStoreAddress(storeClient.getStoreAddressByAddressId(request.getAddressId()).getData());
    } else {
      shoporder.setRecieveAddress(userClient.getRecieveAddress(username, request.getAddressId()).getData());
    }
    shoporder.setStatus(request.getStatus() != null ? request.getStatus() : ShopOrderStatus.UNSTART);
    shoporder.setPickupTime(request.getPickupTime());
    shoporder.setBuyerMessage(request.getBuyerMessage());
    shoporder.setFormid(request.getFormid());

    double totalGoodsPrice = 0;
    List<String> goodIdList = new ArrayList<>();
    for (PreviewOrderItem previeworderItem : previeworder.getOrderItemList()) {
      Good good = previeworderItem.getGood();
      if (previeworderItem.getCount() > good.getCount()) {
        shoporder.setStatus(ShopOrderStatus.PREORDER);
        shoporder.setFromPreOrder(true);
      }
      ShopOrderItem shoporderItem = fromPreviewOrderItem(previeworderItem, good);
      shoporder.addOrderItem(shoporderItem);
      totalGoodsPrice += shoporderItem.getPrice();
      goodIdList.add(shoporderItem.getGood().getId());
      shopCartRepository.delete(previeworderItem.getShopCart().getId());
    }
    shoporder.setTotalGoodsPrice(totalGoodsPrice);

    int sendPrice = 0;
    if (SendType.THIRDSEND.equals(request.getSendType())) {
      sendPrice = 10;
    }
    shoporder.setSendPrice(sendPrice);
    shoporder.setScore(request.getJfNum());

    List<ShopOrderVoucher> shoporderVoucherList = getUserShopOrderVoucherList(username, goodIdList);
    shoporder.setOrderVoucher(shoporderVoucherList);
    double totalPrice = totalGoodsPrice + sendPrice - request.getJfNum() / 100.0;
    if (!CollectionUtils.isEmpty(shoporderVoucherList)) {
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        Good good = shoporderVoucher.getGood();
        UserVoucher userVoucher = shoporderVoucher.getUserVoucher();
        VoucherItem voucherItem = shoporderVoucher.getVoucherItem();

        totalPrice -= good.getPrice();
        userVoucher.decreaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(username, updateUserVoucherList);
    }

    shoporder.setTotalPrice(totalPrice);

    User user = userClient.getUserInternal(username).getData();
    if (user != null) {
      shoporder.setUsername(user.getUsername());

      user.decreaseGenericScore(shoporder.getScore());
      userClient.updateUser(username, user);
    }

    ShopOrderDTO shopOrderDTO = shopOrderRepository.saveAndFlush(convertService.convertShopOrder(shoporder));
    previeworderRepository.delete(previeworder.getId());
    shoporder.setId(shopOrderDTO.getId());
    return shoporder;
  }

  private ShopOrderItem fromPreviewOrderItem(PreviewOrderItem previeworderItem, Good good) {
    if (null == previeworderItem) {
      return null;
    }
    ShopOrderItem shoporderItem = new ShopOrderItem();
    shoporderItem.setGood(good);
    shoporderItem.setCount(previeworderItem.getCount());
    shoporderItem.setPrice(previeworderItem.getCount() * good.getPrice());
    return shoporderItem;
  }

  /**
   * 得到用户订单根据状态分类的数目
   *
   * @param username
   *          用户id
   * @return
   */
  public Map<String, Integer> getUserShopOrderStatusCount(String username) {
    Map<String, Integer> result = new HashMap<>();
    result.put("tobeReceiveCount", getTobeRecievedCount(username));
    result.put("tobePickUpCount", getTobePickupCount(username));
    result.put("inOrderingCount", getPreOrderCount(username));
    result.put("finishedCount", getFinishedCount(username));
    result.put("overPickUpCount", getOverTimeCount(username));
    return result;
  }

  /**
   * 获得待接收订单的数量
   *
   * @param username
   *          用户id
   * @return
   */
  private int getTobeRecievedCount(String username) {
    List<ShopOrderStatusDTO> statusDTOList = new ArrayList<>();
    statusDTOList.add(ShopOrderStatusDTO.UNSTART);
    statusDTOList.add(ShopOrderStatusDTO.DOING);
    List<SendTypeDTO> sendTypeDTOList = new ArrayList<>();
    sendTypeDTOList.add(SendTypeDTO.OWNSEND);
    sendTypeDTOList.add(SendTypeDTO.THIRDSEND);
    return shopOrderRepository.countByUsernameAndStatusInAndSendTypeIn(username, statusDTOList, sendTypeDTOList);
  }

  /**
   * 获得待自取订单的数量
   *
   * @param username
   *          用户id
   * @return
   */
  private int getTobePickupCount(String username) {
    List<ShopOrderStatusDTO> statusDTOList = new ArrayList<>();
    statusDTOList.add(ShopOrderStatusDTO.UNSTART);
    statusDTOList.add(ShopOrderStatusDTO.DOING);
    List<SendTypeDTO> sendTypeDTOList = new ArrayList<>();
    sendTypeDTOList.add(SendTypeDTO.PICKUP);
    return shopOrderRepository.countByUsernameAndStatusInAndSendTypeIn(username, statusDTOList, sendTypeDTOList);
  }

  /**
   * 获得预约单的数量
   *
   * @param username
   *          用户id
   * @return
   */
  private int getPreOrderCount(String username) {
    List<ShopOrderStatusDTO> statusDTOList = new ArrayList<>();
    statusDTOList.add(ShopOrderStatusDTO.PREORDER);
    List<SendTypeDTO> sendTypeDTOList = new ArrayList<>();
    for (SendTypeDTO typeDTO : SendTypeDTO.values()) {
      sendTypeDTOList.add(typeDTO);
    }
    return shopOrderRepository.countByUsernameAndStatusInAndSendTypeIn(username, statusDTOList, sendTypeDTOList);
  }

  /**
   * 获得已完成订单的数量
   *
   * @param username
   *          用户id
   * @return
   */
  private int getFinishedCount(String username) {
    List<ShopOrderStatusDTO> statusDTOList = new ArrayList<>();
    statusDTOList.add(ShopOrderStatusDTO.FINISHED);
    List<SendTypeDTO> sendTypeDTOList = new ArrayList<>();
    for (SendTypeDTO typeDTO : SendTypeDTO.values()) {
      sendTypeDTOList.add(typeDTO);
    }
    return shopOrderRepository.countByUsernameAndStatusInAndSendTypeIn(username, statusDTOList, sendTypeDTOList);
  }

  /**
   * 获得超时订单的数量
   *
   * @param username
   *          用户id
   * @return
   */
  private int getOverTimeCount(String username) {
    List<ShopOrderStatusDTO> statusDTOList = new ArrayList<>();
    statusDTOList.add(ShopOrderStatusDTO.OVERTIME);
    List<SendTypeDTO> sendTypeDTOList = new ArrayList<>();
    for (SendTypeDTO typeDTO : SendTypeDTO.values()) {
      sendTypeDTOList.add(typeDTO);
    }
    return shopOrderRepository.countByUsernameAndStatusInAndSendTypeIn(username, statusDTOList, sendTypeDTOList);
  }

  /**
   * 分页获取预定的商品信息
   *
   * @param currentPage
   * @param pageSize
   * @return
   */
  public PagablePreOrderGoodList getPreOrderGoodList(int currentPage, int pageSize) {
    PagablePreOrderGoodList response = new PagablePreOrderGoodList();
    response.setCurrentPage(currentPage);
    List<ShopOrderDTO> shoporderDTOList = shopOrderRepository.findByStatus(ShopOrderStatusDTO.PREORDER);
    Map<String, Integer> good2ShopOrderItemList = new HashMap<>();
    for (ShopOrderDTO shoporderDTO : shoporderDTOList) {
      for (ShopOrderItemDTO shoporderItemDTO : shoporderDTO.getOrderItemList()) {
        String goodId = shoporderItemDTO.getGoodId();
        Integer count = 0;
        if (good2ShopOrderItemList.containsKey(goodId)) {
          count = good2ShopOrderItemList.get(goodId);
        }
        count += shoporderItemDTO.getCount();
        good2ShopOrderItemList.put(goodId, count);
      }
    }

    List<Map.Entry<String, Integer>> data = new ArrayList<>();
    data.addAll(good2ShopOrderItemList.entrySet());
    data.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

    List<PreOrderGood> preorderGoodList = new ArrayList<>();
    int count = 0;
    for (int i = (currentPage - 1) * pageSize; i < data.size(); i++) {
      Map.Entry<String, Integer> entry = data.get(i);
      Good good = goodClient.getGood(entry.getKey()).getData();
      if (entry.getValue() > good.getCount()) {
        PreOrderGood preOrderGood = new PreOrderGood();
        preOrderGood.setCount(entry.getValue());
        preOrderGood.setGood(good);
        preorderGoodList.add(preOrderGood);
        count++;
      }
      if (count == pageSize) {
        // 这里不好算出totalpage,所以当所有数据还没有遍历完时,用currentPage+1作为totalpage,让调用者认为还有数据
        response.setTotalpage(currentPage + 1);
        break;
      }
    }
    response.setDataList(preorderGoodList);

    return response;
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodId
   */
  public void updatePreOrders(String goodId) {
    List<ShopOrderDTO> shoporderDTOList = shopOrderRepository.findPreOrderContainsGoodid(goodId);
    for (ShopOrderDTO shoporderDTO : shoporderDTOList) {
      ShopOrder shopOrder = convertService.convertShopOrderDTO(shoporderDTO);
      boolean update = true;
      List<SaleGood> saleGoodList = new ArrayList<>();
      Good updateGood = null;
      for (ShopOrderItem item : shopOrder.getOrderItemList()) {
        Good good = item.getGood();
        int buyCount = item.getCount();
        if (buyCount > good.getCount()) {
          update = false;
          break;
        }
        if (goodId.equals(good.getId())) {
          good.setCount(good.getCount() - buyCount);
          good.setSalecount(good.getSalecount() + buyCount);
          updateGood = good;
        }
        saleGoodList.add(new SaleGood(good.getId(), buyCount));
      }

      if (update) {
        goodClient.saleGoodList(saleGoodList);
        shopOrder.setStatus(ShopOrderStatus.UNSTART);
        shopOrderRepository.saveAndFlush(convertService.convertShopOrder(shopOrder));
        templateMsgService.sendPreOrderMsg(shopOrder.getUsername(), shopOrder, updateGood);
      }
    }
  }

}
