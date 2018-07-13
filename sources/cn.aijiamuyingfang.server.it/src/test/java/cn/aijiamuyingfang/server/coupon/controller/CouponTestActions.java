package cn.aijiamuyingfang.server.coupon.controller;

import cn.aijiamuyingfang.server.client.api.impl.AuthControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.PreviewOrderControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.server.client.api.impl.UserControllerClient;
import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.domain.address.City;
import cn.aijiamuyingfang.server.domain.address.County;
import cn.aijiamuyingfang.server.domain.address.Province;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.address.RecieveAddressRequest;
import cn.aijiamuyingfang.server.domain.address.Town;
import cn.aijiamuyingfang.server.domain.address.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucherRequest;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItemRequest;
import cn.aijiamuyingfang.server.domain.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.domain.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.domain.coupon.db.VoucherItemRepository;
import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.goods.db.GoodDetailRepository;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.domain.shopcart.AddShopCartItemRequest;
import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.server.domain.shopcart.db.ShopCartItemRepository;
import cn.aijiamuyingfang.server.domain.shoporder.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.domain.shoporder.CreateUserShoprderRequest;
import cn.aijiamuyingfang.server.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.server.domain.shoporder.SendType;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.server.domain.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.domain.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.domain.user.TokenResponse;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.UserRequest;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import cn.aijiamuyingfang.server.goods.controller.GoodsTestActions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-11 04:44:16
 */
@Service
public class CouponTestActions {
  /**
   * Admin用户的JWT
   */
  public static String ADMIN_USER_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1YzZhMTMyYS04MjlmLTE"
      + "xZTgtOTZmYy0wMGNmZTA0MzBlMmEiLCJjcmVhdGVkIjoxNTMxMTQ1NTY2NTcyLCJl"
      + "eHAiOjE1MzIwMDk1NjZ9.CVWIQTMg37WCzc5GUQwu-oblPKUTzZ60sWKLJrom7tZLY"
      + "hs1vMq3TSWNR-YQJWWfs-yD7y7uxbUgW9cPQMplsg";

  /**
   * Admin用户的Id
   */
  public static final String ADMIN_USER_ID = "5c6a132a-829f-11e8-96fc-00cfe0430e2a";

  /**
   * Sender 1用户的JSCODE
   */
  public static final String SENDERONE_USER_JSCODE = "senderonejscod";

  /**
   * Sender 1用户的电话号码
   */
  public static final String SENDERONE_USER_PHONE = "11111111111";

  @Autowired
  public GoodsTestActions goodsTestActions;

  @Autowired
  public AuthControllerClient authControllerClient;

  @Autowired
  public UserControllerClient userControllerClient;

  @Autowired
  public GoodControllerClient goodControllerClient;

  @Autowired
  public CouponControllerClient couponControllerClient;

  public String senderoneId;

  public String senderoneToken;

  public String goodoneId;

  public String voucheritemoneId;

  public User createSenderOneUser() throws IOException {
    UserRequest createSenderRequest = new UserRequest();
    createSenderRequest.setJscode(SENDERONE_USER_JSCODE);
    createSenderRequest.setPhone(SENDERONE_USER_PHONE);
    createSenderRequest.setNickname("SenderOne User NickName");
    User user = authControllerClient.registerUser(ADMIN_USER_TOKEN, createSenderRequest);
    if (user != null) {
      senderoneId = user.getId();
    }
    return user;
  }

  public String getSenderOneToken() throws IOException {
    TokenResponse tokenResponse = authControllerClient.getToken(SENDERONE_USER_JSCODE, null, null);
    if (tokenResponse != null) {
      senderoneToken = AuthConstants.TOKEN_PREFIX + tokenResponse.getToken();
      return senderoneToken;
    }
    return null;
  }

  public String addressoneId;

  public RecieveAddress addSenderOneRecieveOne() throws IOException {
    RecieveAddressRequest recieveaddressRequest = new RecieveAddressRequest();
    recieveaddressRequest.setProvince(new Province("jiangsu", "210000"));
    recieveaddressRequest.setCity(new City("nanjing", "210100"));
    recieveaddressRequest.setCounty(new County("yuhuaqiao", "210101"));
    recieveaddressRequest.setTown(new Town("tiexinqiao", "000001"));
    recieveaddressRequest.setDef(true);
    recieveaddressRequest.setPhone(SENDERONE_USER_PHONE);
    RecieveAddress address = userControllerClient.addUserRecieveAddress(senderoneToken, senderoneId,
        recieveaddressRequest);
    if (address != null) {
      addressoneId = address.getId();
    }
    return address;

  }

  public Good createGoodOne() throws IOException {
    GoodRequest goodRequest = new GoodRequest();
    goodRequest.setName("good 1");
    goodRequest.setCount(100);
    goodRequest.setPrice(100);
    goodRequest.setBarcode("111111111111");
    Good good = goodsTestActions.goodControllerClient.createGood(GoodsTestActions.ADMIN_USER_TOKEN, null, null,
        goodRequest);
    if (good != null) {
      goodoneId = good.getId();
    }
    return good;
  }

  public String goodtwoId;

  public Good createGoodTwo() throws IOException {
    GoodRequest goodRequest = new GoodRequest();
    goodRequest.setName("good 2");
    goodRequest.setCount(100);
    goodRequest.setPrice(100);
    goodRequest.setBarcode("2222222222");
    Good good = goodsTestActions.goodControllerClient.createGood(GoodsTestActions.ADMIN_USER_TOKEN, null, null,
        goodRequest);
    if (good != null) {
      goodtwoId = good.getId();
    }
    return good;
  }

  public VoucherItem createVoucherItemOneForGoodOne() throws IOException {
    VoucherItemRequest voucheritemRequest = new VoucherItemRequest();
    voucheritemRequest.setName("voucher item 1");
    voucheritemRequest.setDescription("voucher item 1");
    voucheritemRequest.setScore(120);
    voucheritemRequest.setGoodid(goodoneId);
    VoucherItem voucherItem = couponControllerClient.createVoucherItem(ADMIN_USER_TOKEN, voucheritemRequest);
    if (voucherItem != null) {
      voucheritemoneId = voucherItem.getId();
    }
    return voucherItem;
  }

  public String voucheritemtwoId;

  public VoucherItem createVoucherItemTwoForGoodTwo() throws IOException {
    VoucherItemRequest voucheritemRequest = new VoucherItemRequest();
    voucheritemRequest.setName("voucher item 2");
    voucheritemRequest.setDescription("voucher item 2");
    voucheritemRequest.setScore(120);
    voucheritemRequest.setGoodid(goodtwoId);
    VoucherItem voucherItem = couponControllerClient.createVoucherItem(ADMIN_USER_TOKEN, voucheritemRequest);
    if (voucherItem != null) {
      voucheritemtwoId = voucherItem.getId();
    }
    return voucherItem;
  }

  public String goodvoucheroneId;

  public GoodVoucher createGoodVoucherOneWithVoucherItemOne() throws IOException {
    GoodVoucherRequest goodVoucherRequest = new GoodVoucherRequest();
    goodVoucherRequest.setName("good voucher 1");
    goodVoucherRequest.setDescription("good voucher 1");
    goodVoucherRequest.setScore(15);
    goodVoucherRequest.addVoucheritemId(voucheritemoneId);
    GoodVoucher goodvoucher = couponControllerClient.createGoodVoucher(ADMIN_USER_TOKEN, goodVoucherRequest);
    if (goodvoucher != null) {
      goodvoucheroneId = goodvoucher.getId();
    }
    return goodvoucher;
  }

  public String goodvouchertwoId;

  public GoodVoucher createGoodVoucherTwoWithVoucherItemTwo() throws IOException {
    GoodVoucherRequest goodVoucherRequest = new GoodVoucherRequest();
    goodVoucherRequest.setName("good voucher 2");
    goodVoucherRequest.setDescription("good voucher 2");
    goodVoucherRequest.setScore(15);
    goodVoucherRequest.addVoucheritemId(voucheritemtwoId);
    GoodVoucher goodvoucher = couponControllerClient.createGoodVoucher(ADMIN_USER_TOKEN, goodVoucherRequest);
    if (goodvoucher != null) {
      goodvouchertwoId = goodvoucher.getId();
    }
    return goodvoucher;
  }

  public Good applyGoodVoucherOneForGoodOne() throws IOException {
    GoodRequest goodRequest = new GoodRequest();
    goodRequest.setVoucherId(goodvoucheroneId);
    return goodControllerClient.updateGood(ADMIN_USER_TOKEN, goodoneId, goodRequest);
  }

  public Good applyGoodVoucherTwoForGoodTwo() throws IOException {
    GoodRequest goodRequest = new GoodRequest();
    goodRequest.setVoucherId(goodvouchertwoId);
    return goodControllerClient.updateGood(ADMIN_USER_TOKEN, goodtwoId, goodRequest);
  }

  @Autowired
  public ShopCartControllerClient shopcartControllerClient;

  public ShopCartItem senderoneAdd5GoodOne() throws IOException {
    AddShopCartItemRequest addShopcartItemRequest = new AddShopCartItemRequest();
    addShopcartItemRequest.setGoodid(goodoneId);
    addShopcartItemRequest.setGoodNum(5);
    return shopcartControllerClient.addShopCartItem(senderoneToken, senderoneId, addShopcartItemRequest);
  }

  public ShopCartItem senderoneAdd5GoodTwo() throws IOException {
    AddShopCartItemRequest addShopcartItemRequest = new AddShopCartItemRequest();
    addShopcartItemRequest.setGoodid(goodtwoId);
    addShopcartItemRequest.setGoodNum(5);
    return shopcartControllerClient.addShopCartItem(senderoneToken, senderoneId, addShopcartItemRequest);
  }

  public ShopCartItem senderoneAdd10GoodOne() throws IOException {
    AddShopCartItemRequest addShopcartItemRequest = new AddShopCartItemRequest();
    addShopcartItemRequest.setGoodid(goodoneId);
    addShopcartItemRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCartItem(senderoneToken, senderoneId, addShopcartItemRequest);
  }

  public ShopCartItem senderoneAdd10GoodTwo() throws IOException {
    AddShopCartItemRequest addShopcartItemRequest = new AddShopCartItemRequest();
    addShopcartItemRequest.setGoodid(goodtwoId);
    addShopcartItemRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCartItem(senderoneToken, senderoneId, addShopcartItemRequest);
  }

  @Autowired
  public PreviewOrderControllerClient previewOrderControllerClient;

  public PreviewOrder senderonePreviewGoodOne() throws IOException {
    List<String> goodids = new ArrayList<>();
    goodids.add(goodoneId);
    return previewOrderControllerClient.generatePreviewOrder(senderoneToken, senderoneId, goodids);
  }

  public PreviewOrder senderonePreviewAllGood() throws IOException {
    List<String> goodids = new ArrayList<>();
    goodids.add(goodoneId);
    goodids.add(goodtwoId);
    return previewOrderControllerClient.generatePreviewOrder(senderoneToken, senderoneId, goodids);
  }

  @Autowired
  public ShopOrderControllerClient shoporderControllerClient;

  public String shoporderid;

  public ShopOrder senderoneBuy() throws IOException {
    CreateUserShoprderRequest shoporderRequest = new CreateUserShoprderRequest();
    shoporderRequest.setSendtype(SendType.THIRDSEND);
    shoporderRequest.setAddressid(addressoneId);
    ShopOrder shoporder = shoporderControllerClient.createUserShopOrder(senderoneToken, senderoneId, shoporderRequest);
    if (shoporder != null) {
      shoporderid = shoporder.getId();
    }
    return shoporder;
  }

  public void sendShopOrder() throws IOException {
    UpdateShopOrderStatusRequest updateShoporderStatusRequest = new UpdateShopOrderStatusRequest();
    updateShoporderStatusRequest.setOperator("Sender");
    updateShoporderStatusRequest.setStatus(ShopOrderStatus.DOING);
    updateShoporderStatusRequest.setThirdsendCompany("send company");
    updateShoporderStatusRequest.setThirdsendno("111111111111");
    shoporderControllerClient.updateShopOrderStatus(senderoneToken, shoporderid, updateShoporderStatusRequest, false);
  }

  public ConfirmUserShopOrderFinishedResponse senderoneConfirmOrder() throws IOException {
    return shoporderControllerClient.confirmUserShopOrderFinished(senderoneToken, senderoneId, shoporderid);
  }

  @Autowired
  public UserRepository userRepository;

  @Autowired
  public ShopOrderRepository shoporderRepository;

  @Autowired
  public PreviewOrderRepository previeworderRepository;

  @Autowired
  public ShopCartItemRepository shopcartitemRepository;

  @Autowired
  public GoodVoucherRepository goodvoucherRepository;

  @Autowired
  public UserVoucherRepository uservoucherRepository;

  @Autowired
  public VoucherItemRepository voucheritemRepository;

  @Autowired
  public RecieveAddressRepository recieveaddressRepository;

  @Autowired
  public GoodRepository goodRepository;

  @Autowired
  public GoodDetailRepository gooddetailRepository;

  public void clearData() {
    shoporderRepository.deleteAll();
    previeworderRepository.deleteAll();
    recieveaddressRepository.deleteAll();
    shopcartitemRepository.deleteAll();

    gooddetailRepository.deleteAll();
    goodRepository.deleteAll();
    uservoucherRepository.deleteAll();
    goodvoucherRepository.deleteAll();
    voucheritemRepository.deleteAll();

    senderoneId = null;
    senderoneToken = null;
    goodoneId = null;
    voucheritemoneId = null;
    shoporderid = null;
    goodvoucheroneId = null;
    addressoneId = null;
  }
}
