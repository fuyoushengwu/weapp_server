package cn.aijiamuyingfang.server.coupon.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_ID;
import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.response.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * [描述]:
 * <p>
 * TODO
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 13:07:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class CouponControllerTest {
  @Autowired
  private CouponTestActions testActions;

  @Autowired
  private PreviewOrderRepository previeworderRepository;

  @Autowired
  public UserRepository userRepository;

  @Autowired
  private CouponControllerClient couponControllerClient;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Autowired
  private PasswordEncoder encoder;

  @Test
  public void test() {
    System.out.println(encoder.encode("18061756483"));
  }

  @Test
  @TestDescription(description = "当前用户没有兑换券时获得不到用户兑换券")
  public void testGetUserVoucherList_001() throws IOException {
    GetUserVoucherListResponse response = couponControllerClient.getUserVoucherList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1,
        10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getCurrentpage());
    Assert.assertEquals(0, response.getTotalpage());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "当前系统有两个用户,一个用户有兑换券一个用户没有兑换券")
  public void testGetUserVoucherList_002() throws IOException {
    // 0.创建用户:Sender
    User senderUser = testActions.createSenderOneUser();
    Assert.assertNotNull(senderUser);

    String senderToken = testActions.getSenderOneToken();
    Assert.assertNotNull(senderToken);

    // 1.为用户添加收货地址
    RecieveAddress recieveaddress = testActions.addSenderOneRecieveOne();
    Assert.assertNotNull(recieveaddress);

    // 2.创建商品:good 1
    Good good = testActions.createGoodOne();
    Assert.assertNotNull(good);

    // 3.创建VoucherItem:voucher item 1
    VoucherItem voucherItem = testActions.createVoucherItemOneForGoodOne();
    Assert.assertNotNull(voucherItem);

    // 4.创建商品兑换券:good voucher 1
    GoodVoucher goodVoucher = testActions.createGoodVoucherOneWithVoucherItemOne();
    Assert.assertNotNull(goodVoucher);

    // 5.关联商品(good 1)和商品兑换券(good voucher 1)
    Good updatedGood = testActions.applyGoodVoucherOneForGoodOne();
    Assert.assertNotNull(updatedGood.getVoucherId());

    // 6.用户(Sender)添加购物车
    ShopCartItem shopcartItem = testActions.senderoneAdd10GoodOne();
    Assert.assertNotNull(shopcartItem);

    // 7.用户(Sender)预览订单
    PreviewOrder previeworder = testActions.senderonePreviewGoodOne();
    Assert.assertNotNull(previeworder);

    // 8.用户(Sender)购买商品
    ShopOrder shoporder = testActions.senderoneBuy();
    Assert.assertNotNull(shoporder);
    Assert.assertNull(previeworderRepository.findByUserid(senderUser.getId()));

    // 9.送货员送货
    testActions.sendSenderOneShopOrder();

    // 10.用户(Sender)确认订单
    ConfirmUserShopOrderFinishedResponse confirmResponse = testActions.senderoneConfirmOrder();
    Assert.assertNotNull(confirmResponse);

    // 11.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken,
        senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(150, uservoucher.getScore());
    Assert.assertEquals(goodVoucher.getId(), uservoucher.getGoodVoucher().getId());

    // 12.确认用户(Admin)名下没有兑换券
    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(0, getUserVoucherListResponse.getDataList().size());
  }

  @Test
  @TestDescription(description = "用户名下没有兑换券,买商品")
  public void testGetUserShopOrderVoucherList_001() throws IOException {
    // 1.创建用户
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    // 2.添加收货地址
    testActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One
    testActions.createGoodOne();
    // 4.创建VoucherItem:Voucher Item One for Good One
    testActions.createVoucherItemOneForGoodOne();
    // 5.创建GoodVoucher: Good Voucher One
    testActions.createGoodVoucherOneWithVoucherItemOne();
    // 6.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    // 7.添加GoodOne到购物车
    testActions.senderoneAdd10GoodOne();
    // 8.预览商品
    testActions.senderonePreviewGoodOne();
    // 9.购买商品
    ShopOrder shoporder = testActions.senderoneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1010, shoporder.getTotalPrice(), 0.0001);
  }

  @Test
  @TestDescription(description = "用户名下有兑换券,但是不足额,买商品")
  public void testGetUserShopOrderVoucherList_002() throws IOException {
    // 1.创建用户
    User senderUser = testActions.createSenderOneUser();
    String senderToken = testActions.getSenderOneToken();
    // 2.添加收货地址
    testActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One
    testActions.createGoodOne();
    // 4.创建VoucherItem:Voucher Item One for Good One
    testActions.createVoucherItemOneForGoodOne();
    // 5.创建GoodVoucher: Good Voucher One
    testActions.createGoodVoucherOneWithVoucherItemOne();
    // 6.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    // 7.添加GoodOne到购物车
    testActions.senderoneAdd5GoodOne();
    // 8.预览商品
    testActions.senderonePreviewGoodOne();
    // 9.购买商品
    testActions.senderoneBuy();
    // 9.送货员送货
    testActions.sendSenderOneShopOrder();
    // 10.用户(Sender)确认订单
    testActions.senderoneConfirmOrder();
    // 11.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken,
        senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(75, uservoucher.getScore());

    // 7.添加GoodOne到购物车
    testActions.senderoneAdd5GoodOne();
    // 8.预览商品
    testActions.senderonePreviewGoodOne();
    // 9.购买商品
    ShopOrder shoporder = testActions.senderoneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(510, shoporder.getTotalPrice(), 0.0001);

  }

  @Test
  @TestDescription(description = "用户名下有足额的兑换券,使用兑换券买商品(其中一种是可以用兑换券的,另一种是不可以用兑换券的)")
  public void testGetUserShopOrderVoucherList_003() throws IOException {
    // 1.创建用户
    User senderUser = testActions.createSenderOneUser();
    String senderToken = testActions.getSenderOneToken();
    // 2.添加收货地址
    testActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One,,Good Two
    testActions.createGoodOne();
    testActions.createGoodTwo();
    // 4.创建VoucherItem:Voucher Item One for Good One
    testActions.createVoucherItemOneForGoodOne();
    testActions.createVoucherItemTwoForGoodTwo();
    // 5.创建GoodVoucher: Good Voucher One
    testActions.createGoodVoucherOneWithVoucherItemOne();
    testActions.createGoodVoucherTwoWithVoucherItemTwo();
    // 6.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    testActions.applyGoodVoucherTwoForGoodTwo();
    // 7.添加GoodOne到购物车
    testActions.senderoneAdd10GoodOne();
    // 8.预览商品
    testActions.senderonePreviewGoodOne();
    // 9.购买商品
    testActions.senderoneBuy();
    // 9.送货员送货
    testActions.sendSenderOneShopOrder();
    // 10.用户(Sender)确认订单
    testActions.senderoneConfirmOrder();
    // 11.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken,
        senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(150, uservoucher.getScore());

    // 7.添加GoodOne,GoodTwo到购物车
    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    // 8.预览商品
    testActions.senderonePreviewAllGood();
    // 9.购买商品
    ShopOrder shoporder = testActions.senderoneBuy();
    Assert.assertEquals(1, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1910, shoporder.getTotalPrice(), 0.0001);

    // 9.送货员送货
    testActions.sendSenderOneShopOrder();
    // 10.用户(Sender)确认订单
    testActions.senderoneConfirmOrder();

    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken, senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(2, getUserVoucherListResponse.getDataList().size());
    Assert.assertEquals(testActions.goodvoucheroneId,
        getUserVoucherListResponse.getDataList().get(0).getGoodVoucher().getId());
    Assert.assertEquals(180, getUserVoucherListResponse.getDataList().get(0).getScore());
    Assert.assertEquals(testActions.goodvouchertwoId,
        getUserVoucherListResponse.getDataList().get(1).getGoodVoucher().getId());
    Assert.assertEquals(150, getUserVoucherListResponse.getDataList().get(1).getScore());

    GetVoucherItemListResponse getVoucherItemListResponse = couponControllerClient
        .getVoucherItemList(testActions.senderoneToken, 1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(2, getVoucherItemListResponse.getDataList().size());

    VoucherItem voucheritemOne = couponControllerClient.getVoucherItem(testActions.senderoneToken,
        testActions.voucheritemoneId);
    Assert.assertNotNull(voucheritemOne);
    VoucherItem voucheritemTwo = couponControllerClient.getVoucherItem(testActions.senderoneToken,
        testActions.voucheritemtwoId);
    Assert.assertNotNull(voucheritemTwo);

    List<String> goodids = new ArrayList<>();
    goodids.add(testActions.goodoneId);
    goodids.add(testActions.goodtwoId);
    GetShopOrderVoucherListResponse getShopOrderVoucherListResponse = couponControllerClient
        .getUserShopOrderVoucherList(testActions.senderoneToken, testActions.senderoneId, goodids);
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(2, getShopOrderVoucherListResponse.getVoucherList().size());

    couponControllerClient.deprecateVoucherItem(ADMIN_USER_TOKEN, testActions.voucheritemoneId, false);
    getVoucherItemListResponse = couponControllerClient.getVoucherItemList(testActions.senderoneToken, 1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(1, getVoucherItemListResponse.getDataList().size());
    Assert.assertEquals(testActions.voucheritemtwoId, getVoucherItemListResponse.getDataList().get(0).getId());

    getShopOrderVoucherListResponse = couponControllerClient.getUserShopOrderVoucherList(testActions.senderoneToken,
        testActions.senderoneId, goodids);
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(1, getShopOrderVoucherListResponse.getVoucherList().size());
    Assert.assertEquals(testActions.voucheritemtwoId,
        getShopOrderVoucherListResponse.getVoucherList().get(0).getVoucherItem().getId());

  }

  @Test
  @TestDescription(description = "原来有两个商品兑换券,废弃其中一个")
  public void test_GetGoodVoucherList_001() throws IOException {
    GetGoodVoucherListResponse response = couponControllerClient.getGoodVoucherList(ADMIN_USER_TOKEN, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
    Assert.assertEquals(0, response.getTotalpage());
    // 1.创建商品
    testActions.createGoodOne();
    testActions.createGoodTwo();
    // 2.创建VoucherItem
    testActions.createVoucherItemOneForGoodOne();
    testActions.createVoucherItemTwoForGoodTwo();
    // 3.创建GoodVoucher
    testActions.createGoodVoucherOneWithVoucherItemOne();
    testActions.createGoodVoucherTwoWithVoucherItemTwo();
    // 4.关联
    testActions.applyGoodVoucherOneForGoodOne();
    testActions.applyGoodVoucherTwoForGoodTwo();

    response = couponControllerClient.getGoodVoucherList(ADMIN_USER_TOKEN, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(1, response.getTotalpage());

    // 5.废弃GoodVoucherOne
    couponControllerClient.deprecateGoodVoucher(ADMIN_USER_TOKEN, testActions.goodvoucheroneId, false);

    response = couponControllerClient.getGoodVoucherList(ADMIN_USER_TOKEN, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(testActions.goodvouchertwoId, response.getDataList().get(0).getId());
    Assert.assertEquals(1, response.getTotalpage());

    GoodVoucher goodVoucher = couponControllerClient.getGoodVoucher(ADMIN_USER_TOKEN, testActions.goodvouchertwoId);
    Assert.assertNotNull(goodVoucher);
  }

}
