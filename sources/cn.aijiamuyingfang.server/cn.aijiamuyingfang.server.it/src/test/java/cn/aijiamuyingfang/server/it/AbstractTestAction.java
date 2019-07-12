package cn.aijiamuyingfang.server.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.aijiamuyingfang.client.oauth2.OAuthResponse;
import cn.aijiamuyingfang.client.rest.api.impl.FileControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.PreviewOrderControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.UserControllerClient;
import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.db.coupon.GoodVoucherRepository;
import cn.aijiamuyingfang.server.it.db.coupon.UserVoucherRepository;
import cn.aijiamuyingfang.server.it.db.coupon.VoucherItemRepository;
import cn.aijiamuyingfang.server.it.db.goods.ClassifyRepository;
import cn.aijiamuyingfang.server.it.db.goods.GoodDetailRepository;
import cn.aijiamuyingfang.server.it.db.goods.GoodRepository;
import cn.aijiamuyingfang.server.it.db.goods.ImageSourceRepository;
import cn.aijiamuyingfang.server.it.db.goods.StoreRepository;
import cn.aijiamuyingfang.server.it.db.shoporder.PreviewOrderRepository;
import cn.aijiamuyingfang.server.it.db.shoporder.ShopCartRepository;
import cn.aijiamuyingfang.server.it.db.shoporder.ShopOrderRepository;
import cn.aijiamuyingfang.server.it.db.user.RecieveAddressRepository;
import cn.aijiamuyingfang.server.it.db.user.UserRepository;
import cn.aijiamuyingfang.server.it.utils.TestUtils;
import cn.aijiamuyingfang.vo.address.City;
import cn.aijiamuyingfang.vo.address.County;
import cn.aijiamuyingfang.vo.address.Province;
import cn.aijiamuyingfang.vo.address.Town;
import cn.aijiamuyingfang.vo.classify.Classify;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.review.PreviewOrder;
import cn.aijiamuyingfang.vo.shopcart.CreateShopCartRequest;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
import cn.aijiamuyingfang.vo.shoporder.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.vo.shoporder.CreateShopOrderRequest;
import cn.aijiamuyingfang.vo.shoporder.SendType;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.user.Gender;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.user.UserAuthority;
import cn.aijiamuyingfang.vo.utils.StringUtils;
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
  public static final String ADMIN_USER_NAME = "administrator";

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
  protected ShopCartRepository shopCartRepository;

  @Autowired
  protected UserVoucherRepository userVoucherRepository;

  @Autowired
  protected GoodVoucherRepository goodvoucherRepository;

  @Autowired
  protected VoucherItemRepository voucherItemRepository;

  @Autowired
  protected FileControllerClient fileControllerClient;

  @Autowired
  protected GoodControllerClient goodControllerClient;

  @Autowired
  protected UserControllerClient userControllerClient;

  @Autowired
  protected PreviewOrderRepository previeworderRepository;

  @Autowired
  protected ShopCartControllerClient shopCartControllerClient;

  @Autowired
  protected ShopOrderRepository shoporderRepository;

  @Autowired
  protected ShopOrderControllerClient shoporderControllerClient;

  @Autowired
  protected PreviewOrderControllerClient previeworderControllerClient;

  public String getAdminAccessToken() throws IOException {
    if (null == accessTokenResponse) {
      this.accessTokenResponse = TestUtils.getAccessToken(hostname, "weapp-manager", "weapp-manager", ADMIN_USER_NAME,
          "admin");
    }
    return accessTokenResponse.getAccessToken();
  }

  public String getAdminRefreshToken() throws IOException {
    if (null == accessTokenResponse) {
      this.accessTokenResponse = TestUtils.getAccessToken(hostname, "weapp-manager", "weapp-manager", ADMIN_USER_NAME,
          "admin");
    }
    return accessTokenResponse.getRefreshToken();
  }

  public User getSenderOne() throws IOException {
    if (null == senderOne) {
      User createSenderRequest = new User();
      createSenderRequest.setUsername("senderone");
      createSenderRequest.setPassword("senderone");
      createSenderRequest.setPhone("11111111111");
      createSenderRequest.setNickname("SenderOne User NickName");
      createSenderRequest.setGender(Gender.MALE);
      createSenderRequest.addAuthority(UserAuthority.SENDER_PERMISSION);
      this.senderOne = userControllerClient.registerUser(createSenderRequest, getAdminAccessToken());
      this.senderOne.setPassword("senderone");
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
      this.senderOneRecieveAddressOne = userControllerClient.addUserRecieveAddress(getSenderOne().getUsername(),
          recieveaddressRequest, getSenderOneAccessToken());
    }
    return this.senderOneRecieveAddressOne;
  }

  private RecieveAddress senderOneRecieveAddressTwo;

  public RecieveAddress getSenderOneRecieveTwo() throws IOException {
    if (null == senderOneRecieveAddressTwo) {
      RecieveAddress recieveaddressRequest = new RecieveAddress();
      recieveaddressRequest.setProvince(new Province("jiangsu", "210000"));
      recieveaddressRequest.setCity(new City("nanjing", "210100"));
      recieveaddressRequest.setCounty(new County("yuhuaqiao", "210101"));
      recieveaddressRequest.setTown(new Town("tiexinqiao", "000001"));
      recieveaddressRequest.setDef(true);
      recieveaddressRequest.setPhone("11111111111");
      this.senderOneRecieveAddressTwo = userControllerClient.addUserRecieveAddress(getSenderOne().getUsername(),
          recieveaddressRequest, getSenderOneAccessToken());
    }
    return this.senderOneRecieveAddressTwo;
  }

  public User getSenderTwo() throws IOException {
    if (null == senderTwo) {
      User createSenderRequest = new User();
      createSenderRequest.setUsername("sendertwo");
      createSenderRequest.setPassword("sendertwo");
      createSenderRequest.setPhone("22222222");
      createSenderRequest.setNickname("SenderTwo User NickName");
      this.senderTwo = userControllerClient.registerUser(createSenderRequest, getAdminAccessToken());
      this.senderTwo.setPassword("sendertwo");
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
      this.senderTwoRecieveAddressOne = userControllerClient.addUserRecieveAddress(getSenderTwo().getUsername(),
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
    request.setGoodId(getGoodOne().getId());
    request.setGoodNum(10);
    return shopCartControllerClient.addShopCart(ADMIN_USER_NAME, request, getAdminAccessToken());
  }

  public ShopCart addGoodTwo10() throws IOException {
    CreateShopCartRequest request = new CreateShopCartRequest();
    request.setGoodId(getGoodTwo().getId());
    request.setGoodNum(10);
    return shopCartControllerClient.addShopCart(ADMIN_USER_NAME, request, getAdminAccessToken());
  }

  public void deleteShopCart(ShopCart shopCart) throws IOException {
    shopCartControllerClient.deleteShopCart(ADMIN_USER_NAME, shopCart.getId(), getAdminAccessToken(), false);
  }

  public void deleteSenderOneShopCart(ShopCart shopCart) throws IOException {
    shopCartControllerClient.deleteShopCart(getSenderOne().getUsername(), shopCart.getId(), getSenderOneAccessToken(),
        false);
  }

  public void deleteSenderTwoShopCart(ShopCart shopCart) throws IOException {
    shopCartControllerClient.deleteShopCart(getSenderTwo().getUsername(), shopCart.getId(), getSenderTwoAccessToken(),
        false);
  }

  public ShopCart senderOneAdd5GoodOne() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodId(getGoodOne().getId());
    addShopcartItemRequest.setGoodNum(5);
    return shopCartControllerClient.addShopCart(getSenderOne().getUsername(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd5GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodId(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(5);
    return shopCartControllerClient.addShopCart(getSenderOne().getUsername(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd10GoodOne() throws IOException {
    CreateShopCartRequest createShopCartRequest = new CreateShopCartRequest();
    createShopCartRequest.setGoodId(getGoodOne().getId());
    createShopCartRequest.setGoodNum(10);
    return shopCartControllerClient.addShopCart(getSenderOne().getUsername(), createShopCartRequest,
        getSenderOneAccessToken());
  }

  public ShopCart senderOneAdd10GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodId(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopCartControllerClient.addShopCart(getSenderOne().getUsername(), addShopcartItemRequest,
        getSenderOneAccessToken());
  }

  public ShopOrder senderOneBuy() throws IOException {
    CreateShopOrderRequest shoporderRequest = new CreateShopOrderRequest();
    shoporderRequest.setSendType(SendType.THIRDSEND);
    shoporderRequest.setAddressId(getSenderOneRecieveOne().getId());
    return shoporderControllerClient.createUserShopOrder(getSenderOne().getUsername(), shoporderRequest,
        getSenderOneAccessToken());
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
    return shoporderControllerClient.confirmUserShopOrderFinished(getSenderOne().getUsername(), shoporder.getId(),
        getSenderOneAccessToken());
  }

  public ShopOrder senderTwoBuy() throws IOException {
    CreateShopOrderRequest shoporderRequest = new CreateShopOrderRequest();
    shoporderRequest.setSendType(SendType.THIRDSEND);
    shoporderRequest.setAddressId(getSenderTwoRecieveOne().getId());
    return shoporderControllerClient.createUserShopOrder(getSenderTwo().getUsername(), shoporderRequest,
        getSenderTwoAccessToken());
  }

  public PreviewOrder senderOnePreviewGoodOne() throws IOException {
    List<String> goodIdList = new ArrayList<>();
    goodIdList.add(getGoodOne().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderOne().getUsername(), goodIdList,
        getSenderOneAccessToken());
  }

  public PreviewOrder senderOnePreviewAllGood() throws IOException {
    List<String> goodIdList = new ArrayList<>();
    goodIdList.add(getGoodOne().getId());
    goodIdList.add(getGoodTwo().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderOne().getUsername(), goodIdList,
        getSenderOneAccessToken());
  }

  public PreviewOrder senderTwoPreviewAllGood() throws IOException {
    List<String> goodIdList = new ArrayList<>();
    goodIdList.add(getGoodOne().getId());
    goodIdList.add(getGoodTwo().getId());
    return previeworderControllerClient.generatePreviewOrder(getSenderTwo().getUsername(), goodIdList,
        getSenderTwoAccessToken());
  }

  public ShopCart senderTwoAdd10GoodOne() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodId(getGoodOne().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopCartControllerClient.addShopCart(getSenderTwo().getUsername(), addShopcartItemRequest,
        getSenderTwoAccessToken());
  }

  public ShopCart senderTwoAdd10GoodTwo() throws IOException {
    CreateShopCartRequest addShopcartItemRequest = new CreateShopCartRequest();
    addShopcartItemRequest.setGoodId(getGoodTwo().getId());
    addShopcartItemRequest.setGoodNum(10);
    return shopCartControllerClient.addShopCart(getSenderTwo().getUsername(), addShopcartItemRequest,
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
    userRepository.findAll().forEach(userDTO -> {
      if (!ADMIN_USER_NAME.equals(userDTO.getUsername())) {
        userRepository.delete(userDTO);
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
    this.senderOneRecieveAddressTwo = null;
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearGood() {
    goodRepository.findAll().forEach(goodDTO -> {
      if (null == goodDTO) {
        return;
      }
      goodRepository.delete(goodDTO.getId());
      if (goodDTO.getCoverImg() != null) {
        deleteImage(goodDTO.getCoverImg().getId());
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
    goodDetailRepository.findAll().forEach(goodDetailDTO -> {
      if (null == goodDetailDTO) {
        return;
      }
      goodDetailDTO.getDetailImgList().forEach(imageSource -> {
        if (imageSource != null) {
          deleteImage(imageSource.getId());
        }
      });
      goodDetailRepository.delete(goodDetailDTO);
    });

  }

  @TargetDataSource(name = "weapp-goods")
  public void clearClassify() {
    classifyRepository.findAll().forEach(classifyDTO -> {
      if (null == classifyDTO) {
        return;
      }
      if (classifyDTO.getCoverImg() != null) {
        deleteImage(classifyDTO.getCoverImg().getId());
      }
      classifyRepository.delete(classifyDTO);
    });
    classifyOne = null;
    subClassifyOne = null;
  }

  @TargetDataSource(name = "weapp-goods")
  public void clearStore() {
    storeRepository.findAll().forEach(storeDTO -> {
      if (null == storeDTO) {
        return;
      }
      if (storeDTO.getCoverImg() != null) {
        deleteImage(storeDTO.getCoverImg().getId());
      }
      storeDTO.getDetailImgList().forEach(imageSource -> {
        if (imageSource != null) {
          deleteImage(imageSource.getId());
        }
      });
      storeRepository.delete(storeDTO);
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
    shopCartRepository.deleteAll();
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
    voucherItemRepository.deleteAll();
    voucherItemOne = null;
    voucherItemTwo = null;
  }
}
