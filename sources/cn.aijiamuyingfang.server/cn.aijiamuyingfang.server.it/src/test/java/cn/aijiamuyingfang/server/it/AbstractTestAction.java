package cn.aijiamuyingfang.server.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.aijiamuyingfang.client.domain.address.City;
import cn.aijiamuyingfang.client.domain.address.County;
import cn.aijiamuyingfang.client.domain.address.Province;
import cn.aijiamuyingfang.client.domain.address.Town;
import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrder;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shoporder.SendType;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopCartRequest;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopOrderRequest;
import cn.aijiamuyingfang.client.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.client.domain.shoporder.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.domain.user.Gender;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.domain.user.UserAuthority;
import cn.aijiamuyingfang.client.oauth2.OAuthResponse;
import cn.aijiamuyingfang.client.rest.api.impl.FileControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.PreviewOrderControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.UserControllerClient;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.it.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.it.coupon.db.VoucherItemRepository;
import cn.aijiamuyingfang.server.it.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.it.goods.db.GoodDetailRepository;
import cn.aijiamuyingfang.server.it.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.it.goods.db.ImageSourceRepository;
import cn.aijiamuyingfang.server.it.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.it.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.it.shoporder.db.ShopCartRepository;
import cn.aijiamuyingfang.server.it.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.it.user.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.it.user.db.UserRepository;
import cn.aijiamuyingfang.server.it.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-19 18:13:01
 */
@Slf4j
public abstract class AbstractTestAction {
  /**
   * Admin用户的Id
   */
  public static final String ADMIN_USER_ID = "5c6a132a829f11e896fc00cfe0430e2a";

  /**
   * Admin用户的ACCESS_TOKEN
   */
  public OAuthResponse accessTokenResponse;

  /**
   * 1号送货员的ID
   */
  protected User senderOne;

  /**
   * 1号送货员的AccessToken
   */
  protected OAuthResponse senderOneAccessToken;

  /**
   * 1号送货员的收货地址1
   */
  protected RecieveAddress senderOneRecieveAddressOne;

  /**
   * 2号送货员的ID
   */
  protected User senderTwo;

  /**
   * 2号送货员的AccessToken
   */
  protected OAuthResponse senderTwoAccessToken;

  /**
   * 2号送货员的收货地址1
   */
  protected RecieveAddress senderTwoRecieveAddressOne;

  protected Store storeOne;

  protected Store storeTwo;

  protected Classify classifyOne;

  protected Classify subClassifyOne;

  protected Good goodOne;

  protected Good goodTwo;

  protected VoucherItem voucherItemOne;

  protected VoucherItem voucherItemTwo;

  protected GoodVoucher goodVoucherOne;

  protected GoodVoucher goodVoucherTwo;

  @Value("${weapp.host_name}")
  protected String hostname;

  @Autowired
  protected StoreRepository storeRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected ClassifyRepository classifyRepository;

  @Autowired
  protected GoodRepository goodRepository;

  @Autowired
  protected GoodDetailRepository goodDetailRepository;

  @Autowired
  protected RecieveAddressRepository recieveAddressRepository;

  @Autowired
  protected ImageSourceRepository imageSourceRepository;

  @Autowired
  protected ShopCartRepository shopcartRepository;

  @Autowired
  protected UserVoucherRepository userVoucherRepository;

  @Autowired
  protected GoodVoucherRepository goodvoucherRepository;

  @Autowired
  protected VoucherItemRepository voucheritemRepository;

  @Autowired
  protected FileControllerClient fileControllerClient;

  @Autowired
  protected GoodControllerClient goodControllerClient;

  @Autowired
  protected UserControllerClient userControllerClient;

  @Autowired
  protected PreviewOrderRepository previeworderRepository;

  @Autowired
  protected ShopCartControllerClient shopcartControllerClient;

  @Autowired
  protected ShopOrderRepository shoporderRepository;

  @Autowired
  protected ShopOrderControllerClient shoporderControllerClient;

  @Autowired
  protected PreviewOrderControllerClient previeworderControllerClient;

  public String getAdminAccessToken() throws IOException {
    if (null == accessTokenResponse) {
      this.accessTokenResponse = TestUtils.getAccessToken(hostname, "weapp-manager", "weapp-manager", ADMIN_USER_ID,
          "admin");
    }
    return accessTokenResponse.getAccessToken();
  }

  public String getAdminRefreshToken() throws IOException {
    if (null == accessTokenResponse) {
      this.accessTokenResponse = TestUtils.getAccessToken(hostname, "weapp-manager", "weapp-manager", ADMIN_USER_ID,
          "admin");
    }
    return accessTokenResponse.getRefreshToken();
  }

  public User getSenderOne() throws IOException {
    if (null == senderOne) {
      User createSenderRequest = new User();
      createSenderRequest.setJscode("senderonejscode");
      createSenderRequest.setPassword("senderonejscode");
      createSenderRequest.setPhone("11111111111");
      createSenderRequest.setNickname("SenderOne User NickName");
      createSenderRequest.setGender(Gender.MALE);
      createSenderRequest.addAuthority(UserAuthority.SENDER_PERMISSION);
      this.senderOne = userControllerClient.registerUser(createSenderRequest, getAdminAccessToken());
      this.senderOne.setPassword("senderonejscode");
    }
    return this.senderOne;
  }

  public String getSenderOneAccessToken() throws IOException {
    if (null == senderOneAccessToken) {
      User senderOne = getSenderOne();
      this.senderOneAccessToken = TestUtils.getAccessToken(hostname, "weapp-sender", "weapp-sender",
          senderOne.getUsername(), senderOne.getPassword());
    }
    return this.senderOneAccessToken.getAccessToken();
  }

  public String refreshSenderOneAccessToken() throws IOException {
    if (null == senderOneAccessToken) {
      User senderOne = getSenderOne();
      this.senderOneAccessToken = TestUtils.getAccessToken(hostname, "weapp-sender", "weapp-sender",
          senderOne.getUsername(), senderOne.getPassword());
    }
    this.senderOneAccessToken = TestUtils.refreshToken(hostname, "weapp-sender", "weapp-sender",
        this.senderOne.getUsername(), this.senderOne.getPassword(), this.senderOneAccessToken.getRefreshToken());
    return this.senderOneAccessToken.getAccessToken();
  }

  public RecieveAddress getSenderOneRecieveOne() throws IOException {
    if (null == senderOneRecieveAddressOne) {
      RecieveAddress recieveaddressRequest = new RecieveAddress();
      recieveaddressRequest.setProvince(new Province("jiangsu", "210000"));
      recieveaddressRequest.setCity(new City("nanjing", "210100"));
      recieveaddressRequest.setCounty(new County("yuhuaqiao", "210101"));
      recieveaddressRequest.setTown(new Town("tiexinqiao", "000001"));
      recieveaddressRequest.setDef(true);
      recieveaddressRequest.setPhone("11111111111");
      this.senderOneRecieveAddressOne = userControllerClient.addUserRecieveAddress(getSenderOne().getId(),
          recieveaddressRequest, getSenderOneAccessToken());
    }
    return this.senderOneRecieveAddressOne;
  }

  public User getSenderTwo() throws IOException {
    if (null == senderTwo) {
      User createSenderRequest = new User();
      createSenderRequest.setJscode("sendertwojscode");
      createSenderRequest.setPassword("sendertwojscode");
      createSenderRequest.setPhone("22222222");
      createSenderRequest.setNickname("SenderTwo User NickName");
      this.senderTwo = userControllerClient.registerUser(createSenderRequest, getAdminAccessToken());
      this.senderTwo.setPassword("sendertwojscode");
    }
    return senderTwo;
  }

  public String getSenderTwoAccessToken() throws IOException {
    if (null == senderTwoAccessToken) {
      User senderTwo = getSenderTwo();
      this.senderTwoAccessToken = TestUtils.getAccessToken(hostname, "weapp-sender", "weapp-sender",
          senderTwo.getUsername(), senderTwo.getPassword());
    }
    return this.senderTwoAccessToken.getAccessToken();
  }

  public RecieveAddress getSenderTwoRecieveOne() throws IOException {
    if (null == senderTwoRecieveAddressOne) {
      RecieveAddress recieveaddressRequest = new RecieveAddress();
      recieveaddressRequest.setProvince(new Province("jiangsu", "210000"));
      recieveaddressRequest.setCity(new City("nanjing", "210100"));
      recieveaddressRequest.setCounty(new County("yuhuaqiao", "210101"));
      recieveaddressRequest.setTown(new Town("tiexinqiao", "000001"));
      recieveaddressRequest.setDef(true);
      recieveaddressRequest.setPhone("11111111111");
      this.senderTwoRecieveAddressOne = userControllerClient.addUserRecieveAddress(getSenderTwo().getId(),
          recieveaddressRequest, getSenderTwoAccessToken());
    }
    return senderTwoRecieveAddressOne;
  }

  public String refreshSenderTwoAccessToken() throws IOException {
    if (null == senderTwoAccessToken) {
      User senderTwo = getSenderTwo();
      this.senderTwoAccessToken = TestUtils.getAccessToken(hostname, "weapp-sender", "weapp-sender",
          senderTwo.getUsername(), senderTwo.getPassword());
    }
    this.senderTwoAccessToken = TestUtils.refreshToken(hostname, "weapp-sender", "weapp-sender",
        this.senderTwo.getUsername(), this.senderTwo.getPassword(), this.senderTwoAccessToken.getRefreshToken());
    return this.senderTwoAccessToken.getAccessToken();
  }

  public ShopCart addGoodOne10() throws IOException {
    CreateShopCartRequest request = new CreateShopCartRequest();
    request.setGoodid(getGoodOne().getId());
    request.setGoodNum(10);
    return shopcartControllerClient.addShopCart(ADMIN_USER_ID, request, getAdminAccessToken());
  }

  public ShopCart addGoodTwo10() throws IOException {
    CreateShopCartRequest request = new CreateShopCartRequest();
    request.setGoodid(getGoodTwo().getId());
    request.setGoodNum(10);
    return shopcartControllerClient.addShopCart(ADMIN_USER_ID, request, getAdminAccessToken());
  }

  public void deleteShopCart(ShopCart shopcart) throws IOException {
    shopcartControllerClient.deleteShopCart(ADMIN_USER_ID, shopcart.getId(), getAdminAccessToken(), false);
  }

  public void deleteSenderOneShopCart(ShopCart shopcart) throws IOException {
    shopcartControllerClient.deleteShopCart(getSenderOne().getId(), shopcart.getId(), getSenderOneAccessToken(), false);
  }

  public void deleteSenderTwoShopCart(ShopCart shopcart) throws IOException {
    shopcartControllerClient.deleteShopCart(getSenderTwo().getId(), shopcart.getId(), getSenderTwoAccessToken(), false);
  }

  public ShopCart senderOneAdd5GoodOne() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodid(getGoodOne().getId());
    addShopcartItemRequest.setGoodNum(5);
    return shopcartControllerClient.addShopCart(getSenderOne().getId(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd5GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodid(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(5);
    return shopcartControllerClient.addShopCart(getSenderOne().getId(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd10GoodOne() throws IOException {
    CreateShopCartRequest createShopCartRequest = new CreateShopCartRequest();
    createShopCartRequest.setGoodid(getGoodOne().getId());
    createShopCartRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCart(getSenderOne().getId(), createShopCartRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd10GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodid(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCart(getSenderOne().getId(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopOrder senderOneBuy() throws IOException {
      CreateShopOrderRequest shoporderRequest = new CreateShopOrderRequest();
      shoporderRequest.setSendtype(SendType.THIRDSEND);
      shoporderRequest.setAddressid(getSenderOneRecieveOne().getId());
      return shoporderControllerClient.createUserShopOrder(getSenderOne().getId(),
          shoporderRequest, getSenderOneAccessToken());
  }

  public void sendSenderOneShopOrder(ShopOrder shoporder) throws IOException {
    UpdateShopOrderStatusRequest updateShoporderStatusRequest = new UpdateShopOrderStatusRequest();
    updateShoporderStatusRequest.setOperator("Sender");
    updateShoporderStatusRequest.setStatus(ShopOrderStatus.DOING);
    updateShoporderStatusRequest.setThirdsendCompany("send company");
    updateShoporderStatusRequest.setThirdsendno("111111111111");
    shoporderControllerClient.updateShopOrderStatus(shoporder.getId(), updateShoporderStatusRequest,
        getSenderOneAccessToken(), false);
  }

  public ConfirmShopOrderFinishedResponse senderOneConfirmOrder(ShopOrder shoporder) throws IOException {
    return shoporderControllerClient.confirmUserShopOrderFinished(getSenderOne().getId(), shoporder.getId(),
        getSenderOneAccessToken());
  }

  public ShopOrder senderTwoBuy() throws IOException {
      CreateShopOrderRequest shoporderRequest = new CreateShopOrderRequest();
      shoporderRequest.setSendtype(SendType.THIRDSEND);
      shoporderRequest.setAddressid(getSenderTwoRecieveOne().getId());
      return shoporderControllerClient.createUserShopOrder(getSenderTwo().getId(),
          shoporderRequest, getSenderTwoAccessToken());
  }

  public PreviewOrder senderOnePreviewGoodOne() throws IOException {
    List<String> goodids = new ArrayList<>();
    goodids.add(getGoodOne().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderOne().getId(), goodids,
        getSenderOneAccessToken());
  }

  public PreviewOrder senderOnePreviewAllGood() throws IOException {
    List<String> goodids = new ArrayList<>();
    goodids.add(getGoodOne().getId());
    goodids.add(getGoodTwo().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderOne().getId(), goodids,
        getSenderOneAccessToken());
  }

  public PreviewOrder senderTwoPreviewAllGood() throws IOException {
    List<String> goodids = new ArrayList<>();
    goodids.add(getGoodOne().getId());
    goodids.add(getGoodTwo().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderTwo().getId(), goodids,
        getSenderTwoAccessToken());
  }

  public ShopCart senderTwoAdd10GoodOne() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodid(getGoodOne().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCart(getSenderTwo().getId(), addShopcartItemRequest,
        getSenderTwoAccessToken());
  }

  public ShopCart senderTwoAdd10GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodid(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopcartControllerClient.addShopCart(getSenderTwo().getId(), addShopcartItemRequest,
        getSenderTwoAccessToken());
  }

  public Good getGoodOne() throws IOException {
    if (null == goodOne) {
      Good goodRequest = new Good();
      goodRequest.setName("good 1");
      goodRequest.setCount(100);
      goodRequest.setPrice(100);
      goodRequest.setBarcode("111111111111");
      this.goodOne = goodControllerClient.createGood(null, null, goodRequest, getAdminAccessToken());
    }
    return goodOne;
  }

  public Good getGoodTwo() throws IOException {
    if (null == goodTwo) {
      Good goodRequest = new Good();
      goodRequest.setName("good 2");
      goodRequest.setCount(100);
      goodRequest.setPrice(100);
      goodRequest.setBarcode("2222222222");
      this.goodTwo = goodControllerClient.createGood(null, null, goodRequest, getAdminAccessToken());
    }
    return goodTwo;
  }

  @TargetDataSource(name = "weapp-user")
  public void clearUser() {
    userRepository.findAll().forEach(user -> {
      if (!ADMIN_USER_ID.equals(user.getId())) {
        userRepository.delete(user);
      }
    });
    this.senderOne = null;
    this.senderOneAccessToken = null;

    this.senderTwo = null;
    this.senderTwoAccessToken = null;
  }

  @TargetDataSource(name = "weapp-user")
  public void clearRecieveAddress() {
    recieveAddressRepository.deleteAll();
    this.senderOneRecieveAddressOne = null;
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearGood() {
    goodRepository.findAll().forEach(good -> {
      if (null == good) {
        return;
      }
      goodRepository.delete(good.getId());
      if (good.getCoverImg() != null) {
        deleteImage(good.getCoverImg().getId());
      }

    });
    goodOne = null;
    goodTwo = null;
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearImageSource() {
    imageSourceRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearGoodDetail() {
    goodDetailRepository.findAll().forEach(goodDetail -> {
      if (null == goodDetail) {
        return;
      }
      goodDetail.getDetailImgList().forEach(imageSource -> {
        if (imageSource != null) {
          deleteImage(imageSource.getId());
        }
      });
      goodDetailRepository.delete(goodDetail);
    });

  }

  @TargetDataSource(name = "weapp-goods")
  public void clearClassify() {
    classifyRepository.findAll().forEach(classify -> {
      if (null == classify) {
        return;
      }
      if (classify.getCoverImg() != null) {
        deleteImage(classify.getCoverImg().getId());
      }
      classifyRepository.delete(classify);
    });
    classifyOne = null;
    subClassifyOne = null;
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearStore() {
    storeRepository.findAll().forEach(store -> {
      if (null == store) {
        return;
      }
      if (store.getCoverImg() != null) {
        deleteImage(store.getCoverImg().getId());
      }
      store.getDetailImgList().forEach(imageSource -> {
        if (imageSource != null) {
          deleteImage(imageSource.getId());
        }
      });
      storeRepository.delete(store);
    });
    storeOne = null;
    storeTwo = null;
  }

  protected void deleteImage(String imageSourceId) {
    if (StringUtils.isEmpty(imageSourceId)) {
      return;
    }
    try {
      fileControllerClient.delete(imageSourceId, getAdminAccessToken(), false);
    } catch (IOException e) {
      log.error("delete image failed", e);
    }
  }

  @TargetDataSource(name = "weapp-shoporder")
  public void clearPreviewOrder() {
    previeworderRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-shoporder")
  public void clearShopCart() {
    shopcartRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-shoporder")
  public void clearShopOrder() {
    shoporderRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-coupon")
  public void clearUserVoucher() {
    userVoucherRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-coupon")
  public void clearGoodVoucher() {
    goodvoucherRepository.deleteAll();
    goodVoucherOne = null;
    goodVoucherTwo = null;
  }

  @TargetDataSource(name = "weapp-coupon")
  public void clearVoucherItem() {
    voucheritemRepository.deleteAll();
    voucherItemOne = null;
    voucherItemTwo = null;
  }
}
