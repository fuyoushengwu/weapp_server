package cn.aijiamuyingfang.server.shoporder.service;

import cn.aijiamuyingfang.server.client.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.TemplateMsgControllerClient;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.commons.domain.SendType;
import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.address.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.domain.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.domain.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.domain.shopcart.db.ShopCartItemRepository;
import cn.aijiamuyingfang.server.domain.shoporder.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.domain.shoporder.CreateUserShoprderRequest;
import cn.aijiamuyingfang.server.domain.shoporder.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetUserShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.PreOrderGood;
import cn.aijiamuyingfang.server.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.server.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrderItem;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.server.domain.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.domain.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
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
  private CouponControllerClient couponControllerClient;

  @Autowired
  private ShopOrderRepository shopOrderRepository;

  @Autowired
  private PreviewOrderRepository previeworderRepository;

  @Autowired
  private ShopCartItemRepository shopcartitemRepository;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  private GoodVoucherRepository goodvoucherRepository;

  @Autowired
  private UserVoucherRepository uservoucherRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RecieveAddressRepository recieveaddressRepository;

  @Autowired
  private TemplateMsgControllerClient templatemsgControllerClient;

  /**
   * 分页获取订单
   * 
   * @param userid
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetUserShopOrderListResponse getUserShopOrderList(String userid, List<ShopOrderStatus> status,
      List<SendType> sendtype, int currentpage, int pagesize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(userid, status, sendtype, currentpage, pagesize);
    GetUserShopOrderListResponse response = new GetUserShopOrderListResponse();
    response.setCurrentpage(shoporderPage.getNumber() + 1);
    response.setDataList(shoporderPage.getContent());
    response.setTotalpage(shoporderPage.getTotalPages());
    return response;

  }

  /**
   * 分页获取所有的订单信息
   * 
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetShopOrderListResponse getShopOrderList(List<ShopOrderStatus> status, List<SendType> sendtype,
      int currentpage, int pagesize) {
    Page<ShopOrder> shoporderPage = getUserShopOrderPage(null, status, sendtype, currentpage, pagesize);
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
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  private Page<ShopOrder> getUserShopOrderPage(String userid, List<ShopOrderStatus> status, List<SendType> sendtype,
      int currentpage, int pagesize) {
    if (null == status || status.isEmpty()) {
      status = new ArrayList<>();
      for (ShopOrderStatus tmp : ShopOrderStatus.values()) {
        status.add(tmp);
      }
    }
    if (null == sendtype || sendtype.isEmpty()) {
      sendtype = new ArrayList<>();
      for (SendType tmp : SendType.values()) {
        sendtype.add(tmp);
      }
    }
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (status.size() == 1 && status.get(0).equals(ShopOrderStatus.FINISHED)) {
      pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "finishTime");
    } else {
      pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "orderNo");
    }
    if (StringUtils.hasContent(userid)) {
      return shopOrderRepository.findByUseridAndStatusInAndSendtypeIn(userid, status, sendtype, pageRequest);
    }
    return shopOrderRepository.findByStatusInAndSendtypeIn(status, sendtype, pageRequest);

  }

  /**
   * 更新订单状态(在送货时调用)
   * 
   * @param token
   * @param shoporderid
   * @param requestBean
   * @throws IOException
   */
  public void updateShopOrderStatus(String token, String shoporderid, UpdateShopOrderStatusRequest requestBean)
      throws IOException {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    ShopOrderStatus updateStatus = requestBean.getStatus();
    if (updateStatus != null) {
      shoporder.setStatus(updateStatus);
    }
    shoporder.setThirdsendCompany(requestBean.getThirdsendCompany());
    shoporder.setThirdsendNo(requestBean.getThirdsendno());
    shoporder.addOperator(requestBean.getOperator());

    if (updateStatus == ShopOrderStatus.DOING) {
      // 当开始发货的时候,才减去商品数量，面对买家恶意刷单
      List<Good> updateGoods = new ArrayList<>();
      for (ShopOrderItem shoporderGood : shoporder.getOrderItemList()) {
        int goodCount = shoporderGood.getCount();
        Good good = shoporderGood.getGood();
        good.setSalecount(good.getSalecount() + goodCount);
        good.setCount(good.getCount() - goodCount);
        updateGoods.add(good);
      }
      goodRepository.save(updateGoods);

      switch (shoporder.getSendtype()) {
        case OWNSEND:
          templatemsgControllerClient.sendOwnSendMsg(token, shoporder, true);
          break;
        case THIRDSEND:
          templatemsgControllerClient.sendThirdSendMsg(token, shoporder, true);
          break;
        case PICKUP:
          templatemsgControllerClient.sendPickupMsg(token, shoporder, true);
          break;
        default:
          break;
      }
      shopOrderRepository.save(shoporder);
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
   * @param shoporderid
   */
  public void deleteUserShopOrder(String userid, String shoporderid) {
    ShopOrder shopOrder = shopOrderRepository.findOne(shoporderid);
    if (null == shopOrder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (ShopOrderStatus.DOING.equals(shopOrder.getStatus())) {
      throw new ShopOrderException("500", "shoporder is doing,cannot be deleted");
    }
    if (!userid.equals(shopOrder.getUserid())) {
      throw new AuthException("403", "no permission delete other user's shoporder");
    }

    if (ShopOrderStatus.UNSTART.equals(shopOrder.getStatus())) {
      List<ShopOrderVoucher> shoporderVoucherList = shopOrder.getOrderVoucher();
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        UserVoucher userVoucher = shoporderVoucher.getUserVoucher();
        VoucherItem voucherItem = shoporderVoucher.getVoucherItem();
        userVoucher.increaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      uservoucherRepository.save(updateUserVoucherList);

      User user = userRepository.findOne(userid);
      if (user != null) {
        user.increaseGenericScore(shopOrder.getScore());
        userRepository.save(user);
      }
    }

    if (shopOrder.getStatus() != ShopOrderStatus.DOING) {
      shopOrderRepository.delete(shopOrder);
    }

  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param userid
   * @param shoporderid
   * @return
   */
  public ConfirmUserShopOrderFinishedResponse confirmUserShopOrderFinished(String userid, String shoporderid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (shoporder.getStatus() != ShopOrderStatus.DOING) {
      throw new ShopOrderException("500", "only doing shoporder can be confirmed");
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AuthException("403", "no permission confirm other user's shoporder");
    }

    ConfirmUserShopOrderFinishedResponse response = new ConfirmUserShopOrderFinishedResponse();
    shoporder.setStatus(ShopOrderStatus.FINISHED);
    shopOrderRepository.save(shoporder);

    User user = userRepository.findOne(userid);
    if (null == user) {
      return response;
    }
    for (ShopOrderItem shoporderItem : shoporder.getOrderItemList()) {
      Good good = shoporderItem.getGood();
      int genericScore = good.getScore() * shoporderItem.getCount();
      user.increaseGenericScore(genericScore);
      response.addGenericScore(genericScore);
      if (StringUtils.isEmpty(good.getVoucherId())) {
        continue;
      }
      GoodVoucher goodvoucher = goodvoucherRepository.findOne(good.getVoucherId());
      if (goodvoucher != null) {
        UserVoucher uservoucher = uservoucherRepository.findByUseridAndGoodVoucher(userid, goodvoucher.getId());
        if (null == uservoucher) {
          uservoucher = new UserVoucher();
          uservoucher.setGoodVoucher(goodvoucher);
          uservoucher.setUserid(userid);
        }
        int voucherScore = goodvoucher.getScore() * shoporderItem.getCount();
        uservoucher.increaseScore(voucherScore);
        response.addVoucherScore(voucherScore);
        uservoucherRepository.save(uservoucher);
      }
    }
    userRepository.save(user);
    return response;
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param userid
   * @param shoporderid
   * @param addressid
   */
  public void updateUserShopOrderRecieveAddress(String userid, String shoporderid, String addressid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AuthException("403", "no permission update other user's shoporder recieve address");
    }

    if (shoporder.getStatus() != ShopOrderStatus.PREORDER && shoporder.getStatus() != ShopOrderStatus.UNSTART) {
      throw new ShopOrderException("500", "only preorder or unstart shoporder can update recieve address");
    }

    RecieveAddress recieveaddress = recieveaddressRepository.findOne(addressid);
    if (null == recieveaddress) {
      return;
    }
    if (!userid.equals(recieveaddress.getUserid())) {
      throw new AuthException("403", "no permission update other user's recieve address to your shoporder");
    }

    shoporder.setRecieveAddress(recieveaddress);
    shopOrderRepository.save(shoporder);
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
    response.setTotalpage(shoporderPage.getSize());
    return response;
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param userid
   * @param shoporderid
   * @return
   */
  public ShopOrder getUserShopOrder(String userid, String shoporderid) {
    ShopOrder shoporder = shopOrderRepository.findOne(shoporderid);
    if (null == shoporder) {
      throw new ShopOrderException(ResponseCode.SHOPORDER_NOT_EXIST, shoporderid);
    }
    if (!userid.equals(shoporder.getUserid())) {
      throw new AuthException("403", "no permission get other user's shoporder");
    }
    return shoporder;
  }

  /**
   * 创建用户订单
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @return
   * @throws IOException
   */
  public ShopOrder createUserShopOrder(String token, String userid, CreateUserShoprderRequest requestBean)
      throws IOException {
    PreviewOrder previeworder = previeworderRepository.findByUserid(userid);
    if (null == previeworder) {
      throw new ShopOrderException("500", "user should first preview order and then create shoporder");
    }
    ShopOrder shoporder = new ShopOrder();
    shoporder.setCreateTime(new Date());
    shoporder.setUserid(userid);

    shoporder.setSendtype(requestBean.getSendtype());
    shoporder.setStatus(requestBean.getStatus() != null ? requestBean.getStatus() : ShopOrderStatus.UNSTART);
    shoporder.setPickupTime(requestBean.getPickupTime());
    shoporder.setRecieveAddress(recieveaddressRepository.findOne(requestBean.getAddressid()));
    shoporder.setBusinessMessage(requestBean.getBuyerMessage());
    shoporder.setFormid(requestBean.getFormid());

    double totalGoodsPrice = 0;
    List<String> goodids = new ArrayList<>();
    boolean ispreorder = false;
    for (PreviewOrderItem previeworderItem : previeworder.getOrderItemList()) {
      if (previeworderItem.getCount() > previeworderItem.getGood().getCount()) {
        ispreorder = true;
      }
      shoporder.addOrderItem(ShopOrderItem.fromPreviewOrderItem(previeworderItem));
      totalGoodsPrice += previeworderItem.getGood().getPrice() * previeworderItem.getCount();
      goodids.add(previeworderItem.getGood().getId());
      shopcartitemRepository.delete(previeworderItem.getShopcartItemId());
    }
    if (ispreorder) {
      shoporder.setStatus(ShopOrderStatus.PREORDER);
      shoporder.setFromPreOrder(true);
    }

    shoporder.setTotalGoodsPrice(totalGoodsPrice);
    int sendPrice = 0;
    if (SendType.THIRDSEND.equals(requestBean.getSendtype())) {
      sendPrice = 10;
    }
    shoporder.setSendPrice(sendPrice);
    shoporder.setScore(requestBean.getJfNum());

    List<ShopOrderVoucher> shoporderVoucherList = couponControllerClient
        .getUserShopOrderVoucherList(token, userid, goodids).getVoucherList();

    shoporder.setOrderVoucher(shoporderVoucherList);
    double totalPrice = totalGoodsPrice + sendPrice - requestBean.getJfNum() / 100.0;
    if (!CollectionUtils.isEmpty(shoporderVoucherList)) {
      List<UserVoucher> updateUserVoucherList = new ArrayList<>();
      for (ShopOrderVoucher shoporderVoucher : shoporderVoucherList) {
        totalPrice -= shoporderVoucher.getGood().getPrice();

        UserVoucher userVoucher = shoporderVoucher.getUserVoucher();
        VoucherItem voucherItem = shoporderVoucher.getVoucherItem();
        userVoucher.decreaseScore(voucherItem.getScore());
        updateUserVoucherList.add(userVoucher);
      }
      uservoucherRepository.save(updateUserVoucherList);
    }

    shoporder.setTotalPrice(totalPrice);

    User user = userRepository.findOne(userid);
    if (user != null) {
      user.decreaseGenericScore(shoporder.getScore());
      userRepository.save(user);
    }

    shopOrderRepository.save(shoporder);
    previeworderRepository.delete(previeworder.getId());
    return shoporder;
  }

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * @param userid
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
   * @return
   */
  private int getTobeRecievedCount(String userid) {
    List<ShopOrderStatus> status = new ArrayList<>();
    status.add(ShopOrderStatus.UNSTART);
    status.add(ShopOrderStatus.DOING);
    List<SendType> sendtype = new ArrayList<>();
    sendtype.add(SendType.OWNSEND);
    sendtype.add(SendType.THIRDSEND);
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, status, sendtype);
  }

  /**
   * 获得待自取订单的数量
   * 
   * @param userid
   * @return
   */
  private int getTobePickupCount(String userid) {
    List<ShopOrderStatus> status = new ArrayList<>();
    status.add(ShopOrderStatus.UNSTART);
    status.add(ShopOrderStatus.DOING);
    List<SendType> sendtype = new ArrayList<>();
    sendtype.add(SendType.PICKUP);
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, status, sendtype);
  }

  /**
   * 获得预约单的数量
   * 
   * @param userid
   * @return
   */
  private int getPreOrderCount(String userid) {
    List<ShopOrderStatus> status = new ArrayList<>();
    status.add(ShopOrderStatus.PREORDER);
    List<SendType> sendtype = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtype.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, status, sendtype);
  }

  /**
   * 获得已完成订单的数量
   * 
   * @param userid
   * @return
   */
  private int getFinishedCount(String userid) {
    List<ShopOrderStatus> status = new ArrayList<>();
    status.add(ShopOrderStatus.FINISHED);
    List<SendType> sendtype = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtype.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, status, sendtype);
  }

  /**
   * 获得超时订单的数量
   * 
   * @param userid
   * @return
   */
  private int getOverTimeCount(String userid) {
    List<ShopOrderStatus> status = new ArrayList<>();
    status.add(ShopOrderStatus.OVERTIME);
    List<SendType> sendtype = new ArrayList<>();
    for (SendType type : SendType.values()) {
      sendtype.add(type);
    }
    return shopOrderRepository.countByUseridAndStatusInAndSendtypeIn(userid, status, sendtype);
  }

  /**
   * 分页获取预定的商品信息
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetPreOrderGoodListResponse getPreOrderGoodList(int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<Object[]> pageResponse = shopOrderRepository.findPreOrder(pageRequest);
    List<PreOrderGood> preorderGoodList = new ArrayList<>();
    for (Object[] data : pageResponse.getContent()) {
      PreOrderGood preOrderGood = new PreOrderGood();
      preOrderGood.setCount(Integer.valueOf(data[0].toString()));
      preOrderGood.setGood(goodRepository.findOne(data[1].toString()));
      preorderGoodList.add(preOrderGood);
    }
    GetPreOrderGoodListResponse response = new GetPreOrderGoodListResponse();
    response.setCurrentpage(pageResponse.getNumber() + 1);
    response.setDataList(preorderGoodList);
    response.setTotalpage(pageResponse.getSize());
    return response;

  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param token
   * @param updatedGood
   * @throws IOException
   */
  public void updatePreOrders(String token, Good updatedGood) throws IOException {
    if (null == updatedGood || StringUtils.isEmpty(updatedGood.getId())) {
      return;
    }
    List<ShopOrder> shoporderList = shopOrderRepository.findPreOrderContainsGoodid(updatedGood.getId());
    for (ShopOrder shoporder : shoporderList) {

      if (checkUpdateable(shoporder)) {
        // 更新订单
        for (ShopOrderItem item : shoporder.getOrderItemList()) {
          Good good = item.getGood();
          int buycount = item.getCount();
          good.setCount(good.getCount() - buycount);
          goodRepository.save(good);
        }
        shoporder.setStatus(ShopOrderStatus.UNSTART);
        shopOrderRepository.save(shoporder);
        templatemsgControllerClient.sendPreOrderMsg(token, shoporder, updatedGood, true);

      }
    }
  }

  /**
   * 判断预约订单是否能够更新
   * 
   * @param shoporder
   * @return
   */
  private boolean checkUpdateable(ShopOrder shoporder) {
    for (ShopOrderItem item : shoporder.getOrderItemList()) {
      Good good = item.getGood();
      int buycount = item.getCount();
      if (buycount > good.getCount()) {
        return false;
      }
    }
    return true;
  }

}
