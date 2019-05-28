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
 * [描述]:*
 * <p>
 * *订单服务Service层*
 * </p>
 * **
 * 
 * @version 1.0.0*@author ShiWei*@email shiweideyouxiang @sina.cn
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
  private ShopCartRepository shopcartRepository;

  @Autowired
  private UserClient userClient;

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private TemplateMsgService templateMsgService;

  /**
   * 分页获取订单
   *
   * @param userid
   *          用户id
   * @param statusList
   * @param sendtypeList
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetShopOrderListResponse getUserShopOrderList(String userid, List<ShopOrderStatus> statusList,
      List<SendType> sendtypeList, int currentpage, int pagesize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(userid, statusList, sendtypeList, currentpage, pagesize);
    GetShopOrderListResponse response = new GetShopOrderListResponse();
    response.setCurrentpage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;

  }

  /**
   * 分页获取所有的订单信息
   *
   * @param statusList
   * @param sendtypeList
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetShopOrderListResponse getShopOrderList(List<ShopOrderStatus> statusList, List<SendType> sendtypeList,
      int currentpage, int pagesize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(null, statusList, sendtypeList, currentpage, pagesize);
    GetShopOrderListResponse response = new GetShopOrderListResponse();
    response.setCurrentpage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;
  }

  /**
   * 分页获取订单
   *
   * @param userid
   *          用户id
   * @param statusList
   * @param sendtypeList
   * @param currentpage
   * @param pagesize
   * @return
   */
  private Page<ShopOrder> getUserShopOrderPage(String userid, List<ShopOrderStatus> statusList,
      List<SendType> sendtypeList, int currentpage, int pagesize) {
    if (null == statusList || statusList.isEmpty()) {
      statusList = new ArrayList<>();
      for (ShopOrderStatus tmp : ShopOrderStatus.values()) {
        statusList.add(tmp);
      }
    }
    if (null == sendtypeList || sendtypeList.isEmpty()) {
      sendtypeList = new ArrayList<>();
      for (SendType tmp : SendType.values()) {
        sendtypeList.add(tmp);
      }
    }
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (statusList.size() == 1 && statusList.get(0).equals(ShopOrderStatus.FINISHED)) {
      pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "finishTime");
    } else {
      pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "orderNo");
    }
    if (StringUtils.hasContent(userid)) {
      return shopOrderRepository.findByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList, pageRequest);
    }
    return shopOrderRepository.findByStatusInAndSendtypeIn(statusList, sendtypeList, pageRequest);

  }

  /**
   * 更新订单状态(在送货时调用)
   * 
   * @param shoporderid
   * @param requestBean
   */
  public void updateShopOrderStatus(String shoporderid, UpdateShopOrderStatusRequest requestBean) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    ShopOrderStatus updateStatus = requestBean.getStatus();
    if (shoporder.getStatus() == updateStatus) {
      throw new ShopOrderException("500", "shoporder has get target status");
    }

    User user = userClient.getUserById(shoporder.getUserid()).getData();
    if (null == user) {
      throw new ShopOrderException(ResponseCode.USER_NOT_EXIST, shoporder.getUserid());
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

      switch (shoporder.getSendtype()) {
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
   * @param shoporderid
   */
  public void delete100DaysFinishedShopOrder(String shoporderid) {
    ShopOrder shopOrder = shopOrderRepository.findOne(shoporderid);
    if (shopOrder != null && shopOrder.getStatus().equals(ShopOrderStatus.FINISHED)
        && shopOrder.getLastModifyTime() > 100) {
      shopOrderRepository.delete(shopOrder);
    }
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   *
   * @param userid
   *          用户id
   * @param shoporderid
   */
  public void deleteUserShopOrder(String userid, String shoporderid) {
    ShopOrder shopOrder = shopOrderRepository.findOne(shoporderid);
    if (null == shopOrder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }

    User user = userClient.getUserById(userid).getData();
    if (null == user) {
      throw new ShopOrderException("404", "shoporder owner not exist");
    }

    if (ShopOrderStatus.DOING.equals(shopOrder.getStatus())) {
      throw new ShopOrderException("500", "shoporder is doing,cannot be deleted");
    }
    if (!userid.equals(shopOrder.getUserid())) {
      throw new AccessDeniedException("no permission delete other user's shoporder");
    }

    if (ShopOrderStatus.UNSTART.equals(shopOrder.getStatus())) {
      List<ShopOrderVoucher> shoporderVoucherList = shopOrder.getOrderVoucher();
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        UserVoucher userVoucher = couponClient.getUserVoucher(userid, shoporderVoucher.getUservoucherId()).getData();
        VoucherItem voucherItem = couponClient.getVoucherItem(shoporderVoucher.getVoucheritemId()).getData();
        userVoucher.increaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(userid, updateUserVoucherList);

      user.increaseGenericScore(shopOrder.getScore());
      userClient.updateUser(userid, user);
    }

    if (shopOrder.getStatus() != ShopOrderStatus.DOING) {
      shopOrderRepository.delete(shopOrder);
    }
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   *
   * @param userid
   *          用户id
   * @param shoporderid
   * @return
   */
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(String userid, String shoporderid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (shoporder.getStatus() != ShopOrderStatus.DOING) {
      throw new ShopOrderException("500", "only doing shoporder can be confirmed");
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AccessDeniedException("no permission confirm other user's shoporder");
    }

    ConfirmShopOrderFinishedResponse response = new ConfirmShopOrderFinishedResponse();
    shoporder.setStatus(ShopOrderStatus.FINISHED);
    shoporder.setFinishTime(new Date());
    shopOrderRepository.saveAndFlush(shoporder);

    User user = userClient.getUserById(userid).getData();
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
        UserVoucher uservoucher = couponClient.getUserVoucherForGoodVoucher(userid, goodvoucher.getId()).getData();
        if (null == uservoucher) {
          uservoucher = new UserVoucher();
          uservoucher.setGoodVoucher(goodvoucher);
          uservoucher.setUserid(userid);
        }
        int voucherScore = goodvoucher.getScore() * shoporderItem.getCount();
        uservoucher.increaseScore(voucherScore);
        response.addVoucherScore(voucherScore);
        updateUserVoucherList.add(uservoucher);
      }
    }
    couponClient.updateUserVoucherList(userid, updateUserVoucherList);
    userClient.updateUser(userid, user);
    return response;
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   *
   * @param userid
   *          用户id
   * @param shoporderid
   * @param addressid
   */
  public void updateUserShopOrderRecieveAddress(String userid, String shoporderid, String addressid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AccessDeniedException("no permission update other user's shoporder recieve address");
    }

    if (shoporder.getStatus() != ShopOrderStatus.PREORDER && shoporder.getStatus() != ShopOrderStatus.UNSTART) {
      throw new ShopOrderException("500", "only preorder or unstart shoporder can update recieve address");
    }

    shoporder.setRecieveAddressId(addressid);
    shopOrderRepository.saveAndFlush(shoporder);
  }

  /**
   * 分页获取已完成预约单
   *
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetFinishedPreOrderListResponse getFinishedPreOrderList(int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "finishTime");
    Page<ShopOrder> shoporderPage = shopOrderRepository.findByStatus(ShopOrderStatus.FINISHED, pageRequest);
    GetFinishedPreOrderListResponse response = new GetFinishedPreOrderListResponse();
    response.setCurrentpage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;
  }

  /**
   * 获得购买商品时可用的兑换券
   * 
   * @param userid
   *          用户id
   * @param goodids
   * @return
   */
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(String userid, List<String> goodids) {
    GetShopOrderVoucherListResponse result = new GetShopOrderVoucherListResponse();

    GetUserVoucherListResponse response = couponClient.getUserVoucherList(userid, 1, Integer.MAX_VALUE).getData();
    for (UserVoucher uservoucher : response.getDataList()) {
      List<String> itemidList = uservoucher.getGoodVoucher().getVoucheritemIdList();
      for (String itemid : itemidList) {
        VoucherItem item = couponClient.getVoucherItem(itemid).getData();
        if (item != null && goodids.contains(item.getGoodid()) && item.getScore() <= uservoucher.getScore()) {
          ShopOrderVoucher shoporderVoucher = new ShopOrderVoucher();
          shoporderVoucher.setUservoucherId(uservoucher.getId());
          shoporderVoucher.setVoucheritemId(item.getId());
          shoporderVoucher.setGoodId(item.getGoodid());
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
   * @param userid
   *          用户id
   * @param shoporderid
   * @return
   */
  public ShopOrder getUserShopOrder(String userid, String shoporderid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AccessDeniedException("no permission get other user's shoporder");
    }
    return shoporder;
  }

  /**
   * 创建用户订单
   * 
   * @param userid
   * @param request
   * @return
   */
  public ShopOrder createUserShopOrder(String userid, CreateShopOrderRequest request) {
    PreviewOrder previeworder = previeworderRepository.findByUserid(userid);
    if (null == previeworder) {
      throw new ShopOrderException("500", "user should first preview order and then create shoporder");
    }
    ShopOrder shoporder = new ShopOrder();
    shoporder.setCreateTime(new Date());
    shoporder.setSendtype(request.getSendtype());
    if (SendType.PICKUP.equals(request.getSendtype())) {
      shoporder.setPickupStoreAddressId(request.getAddressid());
    } else {
      shoporder.setRecieveAddressId(request.getAddressid());
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
      shopcartRepository.delete(previeworderItem.getShopcartId());
    }
    shoporder.setTotalGoodsPrice(totalGoodsPrice);

    int sendPrice = 0;
    if (SendType.THIRDSEND.equals(request.getSendtype())) {
      sendPrice = 10;
    }
    shoporder.setSendPrice(sendPrice);
    shoporder.setScore(request.getJfNum());

    List<ShopOrderVoucher> shoporderVoucherList = getUserShopOrderVoucherList(userid, goodIdList).getVoucherList();
    shoporder.setOrderVoucher(shoporderVoucherList);
    double totalPrice = totalGoodsPrice + sendPrice - request.getJfNum() / 100.0;
    if (!CollectionUtils.isEmpty(shoporderVoucherList)) {
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        Good good = goodClient.getGood(shoporderVoucher.getGoodId()).getData();
        UserVoucher userVoucher = couponClient.getUserVoucher(userid, shoporderVoucher.getUservoucherId()).getData();
        VoucherItem voucherItem = couponClient.getVoucherItem(shoporderVoucher.getVoucheritemId()).getData();

        totalPrice -= good.getPrice();
        userVoucher.decreaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      couponClient.updateUserVoucherList(userid, updateUserVoucherList);
    }

    shoporder.setTotalPrice(totalPrice);

    User user = userClient.getUserById(userid).getData();
    if (user != null) {
      shoporder.setUserOpenId(user.getOpenid());
      shoporder.setUserid(userid);

      user.decreaseGenericScore(shoporder.getScore());
      userClient.updateUser(userid, user);
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
   * @param userid
   *          用户id
   * @return
   */
  public Map<String, Integer> getUserShopOrderStatusCount(String userid) {
    Map<String, Integer> result = new HashMap<>();
    result.put("tobeReceiveCount", getTobeRecievedCount(userid));
    result.put("tobePickUpCount", getTobePickupCount(userid));
    result.put("inOrderingCount", getPreOrderCount(userid));
    result.put("finishedCount", getFinishedCount(userid));
    result.put("overPickUpCount", getOverTimeCount(userid));
    return result;
  }

  /**
   * 获得待接收订单的数量
   *
   * @param userid
   *          用户id
   * @return
   */
  private int getTobeRecievedCount(String userid) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    statusList.add(ShopOrderStatus.DOING);
    List<SendType> sendtypeList = new ArrayList<>();
    sendtypeList.add(SendType.OWNSEND);
    sendtypeList.add(SendType.THIRDSEND);
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList);
  }

  /**
   * 获得待自取订单的数量
   *
   * @param userid
   *          用户id
   * @return
   */
  private int getTobePickupCount(String userid) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    statusList.add(ShopOrderStatus.DOING);
    List<SendType> sendtypeList = new ArrayList<>();
    sendtypeList.add(SendType.PICKUP);
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList);
  }

  /**
   * 获得预约单的数量
   *
   * @param userid
   *          用户id
   * @return
   */
  private int getPreOrderCount(String userid) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.PREORDER);
    List<SendType> sendtypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtypeList.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList);
  }

  /**
   * 获得已完成订单的数量
   *
   * @param userid
   *          用户id
   * @return
   */
  private int getFinishedCount(String userid) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.FINISHED);
    List<SendType> sendtypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtypeList.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList);
  }

  /**
   * 获得超时订单的数量
   *
   * @param userid
   *          用户id
   * @return
   */
  private int getOverTimeCount(String userid) {
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.OVERTIME);
    List<SendType> sendtypeList = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtypeList.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, statusList, sendtypeList);
  }

  
  /**
   * 分页获取预定的商品信息
   *
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetPreOrderGoodListResponse getPreOrderGoodList(int currentpage, int pagesize) {
    GetPreOrderGoodListResponse response = new GetPreOrderGoodListResponse();
    response.setCurrentpage(currentpage);
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
    for (int i = (currentpage - 1) * pagesize; i < data.size(); i++) {
      Map.Entry<String, Integer> entry = data.get(i);
      Good good = goodClient.getGood(entry.getKey()).getData();
      if (entry.getValue() > good.getCount()) {
        PreOrderGood preOrderGood = new PreOrderGood();
        preOrderGood.setCount(entry.getValue());
        preOrderGood.setGoodid(entry.getKey());
        preorderGoodList.add(preOrderGood);
        count++;
      }
      if (count == pagesize) {
        //这里不好算出totalpage,所以当所有数据还没有遍历完时,用currentpage+1作为totalpage,让调用者认为还有数据
        response.setTotalpage(currentpage + 1);
        break;
      }
    }
    response.setDataList(preorderGoodList);

    return response;
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodid
   */
  public void updatePreOrders(String goodid) {
    List<ShopOrder> shoporderList = shopOrderRepository.findPreOrderContainsGoodid(goodid);
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
        if (goodid.equals(good.getId())) {
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
