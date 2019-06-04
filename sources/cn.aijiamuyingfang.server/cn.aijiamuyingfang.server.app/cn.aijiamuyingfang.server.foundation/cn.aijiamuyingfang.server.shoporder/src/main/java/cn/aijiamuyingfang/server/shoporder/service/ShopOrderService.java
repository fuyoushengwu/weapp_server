package cn.aijiamuyingfang.server.shoporder.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.SendType;
import cn.aijiamuyingfang.server.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.ShopOrderException;
import cn.aijiamuyingfang.server.feign.CouponClient;
import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.feign.domain.coupon.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.feign.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.feign.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.server.feign.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.feign.domain.good.Good;
import cn.aijiamuyingfang.server.feign.domain.good.SaleGood;
import cn.aijiamuyingfang.server.feign.domain.user.User;
import cn.aijiamuyingfang.server.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.shoporder.domain.PreOrderGood;
import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrder;
import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrderItem;
import cn.aijiamuyingfang.server.shoporder.domain.ShopOrder;
import cn.aijiamuyingfang.server.shoporder.domain.ShopOrderItem;
import cn.aijiamuyingfang.server.shoporder.domain.ShopOrderVoucher;
import cn.aijiamuyingfang.server.shoporder.domain.request.CreateShopOrderRequest;
import cn.aijiamuyingfang.server.shoporder.domain.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.server.shoporder.domain.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.server.shoporder.domain.response.GetShopOrderVoucherListResponse;

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
  private TemplateMsgService templateMsgService;

  /**
   * 分页获取订单
   *
   * @param userId
   *          用户id
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetShopOrderListResponse getUserShopOrderList(String userId, List<ShopOrderStatus> statusList,
      List<SendType> sendTypeList, int currentPage, int pageSize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(userId, statusList, sendTypeList, currentPage, pageSize);
    GetShopOrderListResponse response = new GetShopOrderListResponse();
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
  public GetShopOrderListResponse getShopOrderList(List<ShopOrderStatus> statusList, List<SendType> sendTypeList,
      int currentPage, int pageSize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(null, statusList, sendTypeList, currentPage, pageSize);
    GetShopOrderListResponse response = new GetShopOrderListResponse();
    response.setCurrentPage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;
  }

  /**
   * 分页获取订单
   *
   * @param userId
   *          用户id
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @return
   */
  private Page<ShopOrder> getUserShopOrderPage(String userId, List<ShopOrderStatus> statusList,
      List<SendType> sendTypeList, int currentPage, int pageSize) {
    if (null == statusList || statusList.isEmpty()) {
      statusList = new ArrayList<>();
      for (ShopOrderStatus tmp : ShopOrderStatus.values()) {
        statusList.add(tmp);
      }
    }
    if (null == sendTypeList || sendTypeList.isEmpty()) {
      sendTypeList = new ArrayList<>();
      for (SendType tmp : SendType.values()) {
        sendTypeList.add(tmp);
      }
    }
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (statusList.size() == 1 && statusList.get(0).equals(ShopOrderStatus.FINISHED)) {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "finishTime");
    } else {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "orderNo");
    }
    if (StringUtils.hasContent(userId)) {
      return shopOrderRepository.findByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList, pageRequest);
    }
    return shopOrderRepository.findByStatusInAndSendtypeIn(statusList, sendTypeList, pageRequest);

  }

  /**
   * 更新订单状态(在送货时调用)
   * 
   * @param shopOrderId
   * @param requestBean
   */
  public void updateShopOrderStatus(String shopOrderId, UpdateShopOrderStatusRequest requestBean) {
    ShopOrder shoporder = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    ShopOrderStatus updateStatus = requestBean.getStatus();
    if (shoporder.getStatus() == updateStatus) {
      throw new ShopOrderException("500", "shoporder has get target status");
    }

    User user = userClient.getUserInternal(shoporder.getUserId(), null).getData();
    if (null == user) {
      throw new ShopOrderException(ResponseCode.USER_NOT_EXIST, shoporder.getUserId());
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
        saleGoodList.add(new SaleGood(shoporderGood.getGoodId(), shoporderGood.getCount()));
      }
      goodClient.saleGoodList(saleGoodList);

      switch (shoporder.getSendType()) {
      case OWNSEND:
        templateMsgService.sendOwnSendMsg(user.getOpenid(), shoporder);
        break;
      case THIRDSEND:
        templateMsgService.sendThirdSendMsg(user.getOpenid(), shoporder);
        break;
      case PICKUP:
        templateMsgService.sendPickupMsg(user.getOpenid(), shoporder);
        break;
      default:
        break;
      }
      shopOrderRepository.saveAndFlush(shoporder);
    }
  }

  /**
   * 如果订单已经完成100天,可以删除
   *
   * @param shopOrderId
   */
  public void delete100DaysFinishedShopOrder(String shopOrderId) {
    ShopOrder shopOrder = shopOrderRepository.findOne(shopOrderId);
    if (shopOrder != null && shopOrder.getStatus().equals(ShopOrderStatus.FINISHED)
        && shopOrder.getLastModifyTime() > 100) {
      shopOrderRepository.delete(shopOrder);
    }
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   *
   * @param userId
   *          用户id
   * @param shopOrderId
   */
  public void deleteUserShopOrder(String userId, String shopOrderId) {
    ShopOrder shopOrder = shopOrderRepository.findOne(shopOrderId);
    if (null == shopOrder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }

    User user = userClient.getUserInternal(userId, null).getData();
    if (null == user) {
      throw new ShopOrderException("404", "shoporder owner not exist");
    }

    if (ShopOrderStatus.DOING.equals(shopOrder.getStatus())) {
      throw new ShopOrderException("500", "shoporder is doing,cannot be deleted");
    }
    if (!userId.equals(shopOrder.getUserId())) {
      throw new AccessDeniedException("no permission delete other user's shoporder");
    }

    if (ShopOrderStatus.UNSTART.equals(shopOrder.getStatus())) {
      List<ShopOrderVoucher> shoporderVoucherList = shopOrder.getOrderVoucher();
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        UserVoucher userVoucher = couponClient.getUserVoucher(userId, shoporderVoucher.getUservoucherId()).getData();
        VoucherItem voucherItem = couponClient.getVoucherItem(shoporderVoucher.getVoucherItemId()).getData();
        userVoucher.increaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(userId, updateUserVoucherList);

      user.increaseGenericScore(shopOrder.getScore());
      userClient.updateUser(userId, user);
    }

    if (shopOrder.getStatus() != ShopOrderStatus.DOING) {
      shopOrderRepository.delete(shopOrder);
    }
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   *
   * @param userId
   *          用户id
   * @param shopOrderId
   * @return
   */
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(String userId, String shopOrderId) {
    ShopOrder shoporder = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (shoporder.getStatus() != ShopOrderStatus.DOING) {
      throw new ShopOrderException("500", "only doing shoporder can be confirmed");
    }
    if (!userId.equals(shoporder.getUserId())) {
      throw new AccessDeniedException("no permission confirm other user's shoporder");
    }

    ConfirmShopOrderFinishedResponse response = new ConfirmShopOrderFinishedResponse();
    shoporder.setStatus(ShopOrderStatus.FINISHED);
    shoporder.setFinishTime(new Date());
    shopOrderRepository.saveAndFlush(shoporder);

    User user = userClient.getUserInternal(userId, null).getData();
    if (null == user) {
      return response;
    }
    List<UserVoucher> updateUserVoucherList = new ArrayList<>();
    for (ShopOrderItem shoporderItem : shoporder.getOrderItemList()) {
      Good good = goodClient.getGood(shoporderItem.getGoodId()).getData();
      int genericScore = good.getScore() * shoporderItem.getCount();
      user.increaseGenericScore(genericScore);
      response.addGenericScore(genericScore);
      if (StringUtils.isEmpty(good.getVoucherId())) {
        continue;
      }

      GoodVoucher goodvoucher = couponClient.getGoodVoucher(good.getVoucherId()).getData();
      if (goodvoucher != null) {
        UserVoucher uservoucher = couponClient.getUserVoucherForGoodVoucher(userId, goodvoucher.getId()).getData();
        if (null == uservoucher) {
          uservoucher = new UserVoucher();
          uservoucher.setGoodVoucher(goodvoucher);
          uservoucher.setUserId(userId);
        }
        int voucherScore = goodvoucher.getScore() * shoporderItem.getCount();
        uservoucher.increaseScore(voucherScore);
        response.addVoucherScore(voucherScore);
        updateUserVoucherList.add(uservoucher);
      }
    }
    couponClient.updateUserVoucherList(userId, updateUserVoucherList);
    userClient.updateUser(userId, user);
    return response;
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   *
   * @param userId
   *          用户id
   * @param shopOrderId
   * @param addressId
   */
  public void updateUserShopOrderRecieveAddress(String userId, String shopOrderId, String addressId) {
    ShopOrder shoporder = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (!userId.equals(shoporder.getUserId())) {
      throw new AccessDeniedException("no permission update other user's shoporder recieve address");
    }

    if (shoporder.getStatus() != ShopOrderStatus.PREORDER && shoporder.getStatus() != ShopOrderStatus.UNSTART) {
      throw new ShopOrderException("500", "only preorder or unstart shoporder can update recieve address");
    }

    shoporder.setRecieveAddressId(addressId);
    shopOrderRepository.saveAndFlush(shoporder);
  }

  /**
   * 分页获取已完成预约单
   *
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetFinishedPreOrderListResponse getFinishedPreOrderList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "finishTime");
    Page<ShopOrder> shoporderPage = shopOrderRepository.findByStatus(ShopOrderStatus.FINISHED, pageRequest);
    GetFinishedPreOrderListResponse response = new GetFinishedPreOrderListResponse();
    response.setCurrentPage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;
  }

  /**
   * 获得购买商品时可用的兑换券
   * 
   * @param userId
   *          用户id
   * @param goodIdList
   * @return
   */
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(String userId, List<String> goodIdList) {
    GetShopOrderVoucherListResponse result = new GetShopOrderVoucherListResponse();

    GetUserVoucherListResponse response = couponClient.getUserVoucherList(userId, 1, Integer.MAX_VALUE).getData();
    for (UserVoucher uservoucher : response.getDataList()) {
      List<String> itemidList = uservoucher.getGoodVoucher().getVoucherItemIdList();
      for (String itemid : itemidList) {
        VoucherItem item = couponClient.getVoucherItem(itemid).getData();
        if (item != null && goodIdList.contains(item.getGoodId()) && item.getScore() <= uservoucher.getScore()) {
          ShopOrderVoucher shoporderVoucher = new ShopOrderVoucher();
          shoporderVoucher.setUservoucherId(uservoucher.getId());
          shoporderVoucher.setVoucherItemId(item.getId());
          shoporderVoucher.setGoodId(item.getGoodId());
          result.addUsefulHolderCart(shoporderVoucher);
          break;
        }
      }
    }
    return result;
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   *
   * @param userId
   *          用户id
   * @param shopOrderId
   * @return
   */
  public ShopOrder getUserShopOrder(String userId, String shopOrderId) {
    ShopOrder shoporder = shopOrderRepository.findOne(shopOrderId);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shopOrderId);
    }
    if (!userId.equals(shoporder.getUserId())) {
      throw new AccessDeniedException("no permission get other user's shoporder");
    }
    return shoporder;
  }

  /**
   * 创建用户订单
   * 
   * @param userId
   * @param request
   * @return
   */
  public ShopOrder createUserShopOrder(String userId, CreateShopOrderRequest request) {
    PreviewOrder previeworder = previeworderRepository.findByUserId(userId);
    if (null == previeworder) {
      throw new ShopOrderException("500", "user should first preview order and then create shoporder");
    }
    ShopOrder shoporder = new ShopOrder();
    shoporder.setCreateTime(new Date());
    shoporder.setSendType(request.getSendType());
    if (SendType.PICKUP.equals(request.getSendType())) {
      shoporder.setPickupStoreAddressId(request.getAddressId());
    } else {
      shoporder.setRecieveAddressId(request.getAddressId());
    }
    shoporder.setStatus(request.getStatus() != null ? request.getStatus() : ShopOrderStatus.UNSTART);
    shoporder.setPickupTime(request.getPickupTime());
    shoporder.setBuyerMessage(request.getBuyerMessage());
    shoporder.setFormid(request.getFormid());

    double totalGoodsPrice = 0;
    List<String> goodIdList = new ArrayList<>();
    for (PreviewOrderItem previeworderItem : previeworder.getOrderItemList()) {
      Good good = goodClient.getGood(previeworderItem.getGoodId()).getData();
      if (previeworderItem.getCount() > good.getCount()) {
        shoporder.setStatus(ShopOrderStatus.PREORDER);
        shoporder.setFromPreOrder(true);
      }
      ShopOrderItem shoporderItem = fromPreviewOrderItem(previeworderItem, good);
      shoporder.addOrderItem(shoporderItem);
      totalGoodsPrice += shoporderItem.getPrice();
      goodIdList.add(shoporderItem.getGoodId());
      shopCartRepository.delete(previeworderItem.getShopCartId());
    }
    shoporder.setTotalGoodsPrice(totalGoodsPrice);

    int sendPrice = 0;
    if (SendType.THIRDSEND.equals(request.getSendType())) {
      sendPrice = 10;
    }
    shoporder.setSendPrice(sendPrice);
    shoporder.setScore(request.getJfNum());

    List<ShopOrderVoucher> shoporderVoucherList = getUserShopOrderVoucherList(userId, goodIdList).getVoucherList();
    shoporder.setOrderVoucher(shoporderVoucherList);
    double totalPrice = totalGoodsPrice + sendPrice - request.getJfNum() / 100.0;
    if (!CollectionUtils.isEmpty(shoporderVoucherList)) {
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        Good good = goodClient.getGood(shoporderVoucher.getGoodId()).getData();
        UserVoucher userVoucher = couponClient.getUserVoucher(userId, shoporderVoucher.getUservoucherId()).getData();
        VoucherItem voucherItem = couponClient.getVoucherItem(shoporderVoucher.getVoucherItemId()).getData();

        totalPrice -= good.getPrice();
        userVoucher.decreaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(userId, updateUserVoucherList);
    }

    shoporder.setTotalPrice(totalPrice);

    User user = userClient.getUserInternal(userId, null).getData();
    if (user != null) {
      shoporder.setUserOpenId(user.getOpenid());
      shoporder.setUserId(userId);

      user.decreaseGenericScore(shoporder.getScore());
      userClient.updateUser(userId, user);
    }

    shopOrderRepository.saveAndFlush(shoporder);
    previeworderRepository.delete(previeworder.getId());
    return shoporder;
  }

  private ShopOrderItem fromPreviewOrderItem(PreviewOrderItem previeworderItem, Good good) {
    if (null == previeworderItem) {
      return null;
    }
    ShopOrderItem shoporderItem = new ShopOrderItem();
    shoporderItem.setGoodId(good.getId());
    shoporderItem.setGoodName(good.getName());
    shoporderItem.setGoodPack(good.getPack());
    shoporderItem.setCount(previeworderItem.getCount());
    shoporderItem.setPrice(previeworderItem.getCount() * good.getPrice());
    return shoporderItem;
  }

  /**
   * 得到用户订单根据状态分类的数目
   *
   * @param userId
   *          用户id
   * @return
   */
  public Map<String, Integer> getUserShopOrderStatusCount(String userId) {
    Map<String, Integer> result = new HashMap<>();
    result.put("tobeReceiveCount", getTobeRecievedCount(userId));
    result.put("tobePickUpCount", getTobePickupCount(userId));
    result.put("inOrderingCount", getPreOrderCount(userId));
    result.put("finishedCount", getFinishedCount(userId));
    result.put("overPickUpCount", getOverTimeCount(userId));
    return result;
  }

  /**
   * 获得待接收订单的数量
   *
   * @param userId
   *          用户id
   * @return
   */
  private int getTobeRecievedCount(String userId) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    statusList.add(ShopOrderStatus.DOING);
    List<SendType> sendTypeList = new ArrayList<>();
    sendTypeList.add(SendType.OWNSEND);
    sendTypeList.add(SendType.THIRDSEND);
    return shopOrderRepository.countByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList);
  }

  /**
   * 获得待自取订单的数量
   *
   * @param userId
   *          用户id
   * @return
   */
  private int getTobePickupCount(String userId) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    statusList.add(ShopOrderStatus.DOING);
    List<SendType> sendTypeList = new ArrayList<>();
    sendTypeList.add(SendType.PICKUP);
    return shopOrderRepository.countByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList);
  }

  /**
   * 获得预约单的数量
   *
   * @param userId
   *          用户id
   * @return
   */
  private int getPreOrderCount(String userId) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.PREORDER);
    List<SendType> sendTypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendTypeList.add(type);
    }
    return shopOrderRepository.countByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList);
  }

  /**
   * 获得已完成订单的数量
   *
   * @param userId
   *          用户id
   * @return
   */
  private int getFinishedCount(String userId) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.FINISHED);
    List<SendType> sendTypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendTypeList.add(type);
    }
    return shopOrderRepository.countByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList);
  }

  /**
   * 获得超时订单的数量
   *
   * @param userId
   *          用户id
   * @return
   */
  private int getOverTimeCount(String userId) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.OVERTIME);
    List<SendType> sendTypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendTypeList.add(type);
    }
    return shopOrderRepository.countByUserIdAndStatusInAndSendtypeIn(userId, statusList, sendTypeList);
  }

  /**
   * 分页获取预定的商品信息
   *
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetPreOrderGoodListResponse getPreOrderGoodList(int currentPage, int pageSize) {
    GetPreOrderGoodListResponse response = new GetPreOrderGoodListResponse();
    response.setCurrentPage(currentPage);
    List<ShopOrder> shoporderList = shopOrderRepository.findByStatus(ShopOrderStatus.PREORDER);
    Map<String, Integer> good2ShopOrderItemList = new HashMap<>();
    for (ShopOrder shoporder : shoporderList) {
      for (ShopOrderItem shoporderItem : shoporder.getOrderItemList()) {
        String goodId = shoporderItem.getGoodId();
        Integer count = 0;
        if (good2ShopOrderItemList.containsKey(goodId)) {
          count = good2ShopOrderItemList.get(goodId);
        }
        count += shoporderItem.getCount();
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
        preOrderGood.setGoodId(entry.getKey());
        preOrderGood.setGoodName(good.getName());
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
    List<ShopOrder> shoporderList = shopOrderRepository.findPreOrderContainsGoodid(goodId);
    for (ShopOrder shoporder : shoporderList) {
      boolean update = true;
      List<SaleGood> saleGoodList = new ArrayList<>();
      Good updateGood = null;
      for (ShopOrderItem item : shoporder.getOrderItemList()) {
        Good good = goodClient.getGood(item.getGoodId()).getData();
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
        shoporder.setStatus(ShopOrderStatus.UNSTART);
        shopOrderRepository.saveAndFlush(shoporder);
        templateMsgService.sendPreOrderMsg(shoporder.getUserOpenId(), shoporder, updateGood);
      }
    }
  }

}
