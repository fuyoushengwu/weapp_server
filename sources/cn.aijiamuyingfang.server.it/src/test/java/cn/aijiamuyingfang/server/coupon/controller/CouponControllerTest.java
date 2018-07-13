package cn.aijiamuyingfang.server.coupon.controller;

import cn.aijiamuyingfang.server.client.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.coupon.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetVoucherItemListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.server.domain.shoporder.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.server.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.db.PreviewOrderRepository;
import cn.aijiamuyingfang.server.domain.user.User;
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
  private CouponTestActions couponTestActions;

  @Autowired
  private PreviewOrderRepository previeworderRepository;

  @Autowired
  public UserRepository userRepository;

  @Autowired
  private CouponControllerClient couponControllerClient;

  @Before
  public void before() throws IOException {
    couponTestActions.clearData();
  }

  @After
  public void after() {
    couponTestActions.clearData();
  }

  @Test
  @TestDescription(description = "当前用户没有兑换券时获得不到用户兑换券")
  public void testGetUserVoucherList_001() throws IOException {
    GetUserVoucherListResponse response = couponControllerClient.getUserVoucherList(CouponTestActions.ADMIN_USER_TOKEN,
        CouponTestActions.ADMIN_USER_ID, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getCurrentpage());
    Assert.assertEquals(0, response.getTotalpage());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "当前系统有两个用户,一个用户有兑换券一个用户没有兑换券")
  public void testGetUserVoucherList_002() throws IOException {
    // 0.创建用户:Sender
    User senderUser = couponTestActions.createSenderOneUser();
    Assert.assertNotNull(senderUser);

    String senderToken = couponTestActions.getSenderOneToken();
    Assert.assertNotNull(senderToken);

    // 1.为用户添加收货地址
    RecieveAddress recieveaddress = couponTestActions.addSenderOneRecieveOne();
    Assert.assertNotNull(recieveaddress);

    // 2.创建商品:good 1
    Good good = couponTestActions.createGoodOne();
    Assert.assertNotNull(good);

    // 3.创建VoucherItem:voucher item 1
    VoucherItem voucherItem = couponTestActions.createVoucherItemOneForGoodOne();
    Assert.assertNotNull(voucherItem);

    // 4.创建商品兑换券:good voucher 1
    GoodVoucher goodVoucher = couponTestActions.createGoodVoucherOneWithVoucherItemOne();
    Assert.assertNotNull(goodVoucher);

    // 5.关联商品(good 1)和商品兑换券(good voucher 1)
    Good updatedGood = couponTestActions.applyGoodVoucherOneForGoodOne();
    Assert.assertNotNull(updatedGood.getVoucherId());

    // 6.用户(Sender)添加购物车
    ShopCartItem shopcartItem = couponTestActions.senderoneAdd10GoodOne();
    Assert.assertNotNull(shopcartItem);

    // 7.用户(Sender)预览订单
    PreviewOrder previeworder = couponTestActions.senderonePreviewGoodOne();
    Assert.assertNotNull(previeworder);

    // 8.用户(Sender)购买商品
    ShopOrder shoporder = couponTestActions.senderoneBuy();
    Assert.assertNotNull(shoporder);
    Assert.assertNull(previeworderRepository.findByUserid(senderUser.getId()));

    // 9.送货员送货
    couponTestActions.sendShopOrder();

    // 10.用户(Sender)确认订单
    ConfirmUserShopOrderFinishedResponse confirmResponse = couponTestActions.senderoneConfirmOrder();
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
    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(CouponTestActions.ADMIN_USER_TOKEN,
        CouponTestActions.ADMIN_USER_ID, 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(0, getUserVoucherListResponse.getDataList().size());
    userRepository.delete(senderUser.getId());
  }

  @Test
  @TestDescription(description = "用户名下没有兑换券,买商品")
  public void testGetUserShopOrderVoucherList_001() throws IOException {
    // 1.创建用户
    User senderUser = couponTestActions.createSenderOneUser();
    couponTestActions.getSenderOneToken();
    // 2.添加收货地址
    couponTestActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One
    couponTestActions.createGoodOne();
    // 4.创建VoucherItem:Voucher Item One for Good One
    couponTestActions.createVoucherItemOneForGoodOne();
    // 5.创建GoodVoucher: Good Voucher One
    couponTestActions.createGoodVoucherOneWithVoucherItemOne();
    // 6.关联GoodOne和GoodVoucherOne
    couponTestActions.applyGoodVoucherOneForGoodOne();
    // 7.添加GoodOne到购物车
    couponTestActions.senderoneAdd10GoodOne();
    // 8.预览商品
    couponTestActions.senderonePreviewGoodOne();
    // 9.购买商品
    ShopOrder shoporder = couponTestActions.senderoneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1010, shoporder.getTotalPrice(), 0.0001);
    userRepository.delete(senderUser.getId());
  }

  @Test
  @TestDescription(description = "用户名下有兑换券,但是不足额,买商品")
  public void testGetUserShopOrderVoucherList_002() throws IOException {
    // 1.创建用户
    User senderUser = couponTestActions.createSenderOneUser();
    String senderToken = couponTestActions.getSenderOneToken();
    // 2.添加收货地址
    couponTestActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One
    couponTestActions.createGoodOne();
    // 4.创建VoucherItem:Voucher Item One for Good One
    couponTestActions.createVoucherItemOneForGoodOne();
    // 5.创建GoodVoucher: Good Voucher One
    couponTestActions.createGoodVoucherOneWithVoucherItemOne();
    // 6.关联GoodOne和GoodVoucherOne
    couponTestActions.applyGoodVoucherOneForGoodOne();
    // 7.添加GoodOne到购物车
    couponTestActions.senderoneAdd5GoodOne();
    // 8.预览商品
    couponTestActions.senderonePreviewGoodOne();
    // 9.购买商品
    couponTestActions.senderoneBuy();
    // 9.送货员送货
    couponTestActions.sendShopOrder();
    // 10.用户(Sender)确认订单
    couponTestActions.senderoneConfirmOrder();
    // 11.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken,
        senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(75, uservoucher.getScore());

    // 7.添加GoodOne到购物车
    couponTestActions.senderoneAdd5GoodOne();
    // 8.预览商品
    couponTestActions.senderonePreviewGoodOne();
    // 9.购买商品
    ShopOrder shoporder = couponTestActions.senderoneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(510, shoporder.getTotalPrice(), 0.0001);

    userRepository.delete(senderUser.getId());
  }

  @Test
  @TestDescription(description = "用户名下有足额的兑换券,使用兑换券买商品(其中一种是可以用兑换券的,另一种是不可以用兑换券的)")
  public void testGetUserShopOrderVoucherList_003() throws IOException {
    // 1.创建用户
    User senderUser = couponTestActions.createSenderOneUser();
    String senderToken = couponTestActions.getSenderOneToken();
    // 2.添加收货地址
    couponTestActions.addSenderOneRecieveOne();
    // 3.创建商品:Good One,,Good Two
    couponTestActions.createGoodOne();
    couponTestActions.createGoodTwo();
    // 4.创建VoucherItem:Voucher Item One for Good One
    couponTestActions.createVoucherItemOneForGoodOne();
    couponTestActions.createVoucherItemTwoForGoodTwo();
    // 5.创建GoodVoucher: Good Voucher One
    couponTestActions.createGoodVoucherOneWithVoucherItemOne();
    couponTestActions.createGoodVoucherTwoWithVoucherItemTwo();
    // 6.关联GoodOne和GoodVoucherOne
    couponTestActions.applyGoodVoucherOneForGoodOne();
    couponTestActions.applyGoodVoucherTwoForGoodTwo();
    // 7.添加GoodOne到购物车
    couponTestActions.senderoneAdd10GoodOne();
    // 8.预览商品
    couponTestActions.senderonePreviewGoodOne();
    // 9.购买商品
    couponTestActions.senderoneBuy();
    // 9.送货员送货
    couponTestActions.sendShopOrder();
    // 10.用户(Sender)确认订单
    couponTestActions.senderoneConfirmOrder();
    // 11.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken,
        senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(150, uservoucher.getScore());

    // 7.添加GoodOne,GoodTwo到购物车
    couponTestActions.senderoneAdd10GoodOne();
    couponTestActions.senderoneAdd10GoodTwo();
    // 8.预览商品
    couponTestActions.senderonePreviewAllGood();
    // 9.购买商品
    ShopOrder shoporder = couponTestActions.senderoneBuy();
    Assert.assertEquals(1, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1910, shoporder.getTotalPrice(), 0.0001);

    // 9.送货员送货
    couponTestActions.sendShopOrder();
    // 10.用户(Sender)确认订单
    couponTestActions.senderoneConfirmOrder();

    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(senderToken, senderUser.getId(), 1, 10);
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(2, getUserVoucherListResponse.getDataList().size());
    Assert.assertEquals(couponTestActions.goodvoucheroneId,
        getUserVoucherListResponse.getDataList().get(0).getGoodVoucher().getId());
    Assert.assertEquals(180, getUserVoucherListResponse.getDataList().get(0).getScore());
    Assert.assertEquals(couponTestActions.goodvouchertwoId,
        getUserVoucherListResponse.getDataList().get(1).getGoodVoucher().getId());
    Assert.assertEquals(150, getUserVoucherListResponse.getDataList().get(1).getScore());

    GetVoucherItemListResponse getVoucherItemListResponse = couponControllerClient
        .getVoucherItemList(couponTestActions.senderoneToken, 1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(2, getVoucherItemListResponse.getDataList().size());

    List<String> goodids = new ArrayList<>();
    goodids.add(couponTestActions.goodoneId);
    goodids.add(couponTestActions.goodtwoId);
    GetShopOrderVoucherListResponse getShopOrderVoucherListResponse = couponControllerClient
        .getUserShopOrderVoucherList(couponTestActions.senderoneToken, couponTestActions.senderoneId, goodids);
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(2, getShopOrderVoucherListResponse.getVoucherList().size());

    couponControllerClient.deprecateVoucherItem(CouponTestActions.ADMIN_USER_TOKEN, couponTestActions.voucheritemoneId,
        false);
    getVoucherItemListResponse = couponControllerClient.getVoucherItemList(couponTestActions.senderoneToken, 1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(1, getVoucherItemListResponse.getDataList().size());
    Assert.assertEquals(couponTestActions.voucheritemtwoId, getVoucherItemListResponse.getDataList().get(0).getId());

    getShopOrderVoucherListResponse = couponControllerClient
        .getUserShopOrderVoucherList(couponTestActions.senderoneToken, couponTestActions.senderoneId, goodids);
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(1, getShopOrderVoucherListResponse.getVoucherList().size());
    Assert.assertEquals(couponTestActions.voucheritemtwoId,
        getShopOrderVoucherListResponse.getVoucherList().get(0).getVoucherItem().getId());

    userRepository.delete(senderUser.getId());
  }

  @Test
  @TestDescription(description = "原来有两个商品兑换券,废弃其中一个")
  public void test_GetGoodVoucherList_001() throws IOException {
    GetGoodVoucherListResponse response = couponControllerClient.getGoodVoucherList(CouponTestActions.ADMIN_USER_TOKEN,
        1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
    Assert.assertEquals(0, response.getTotalpage());
    // 1.创建商品
    couponTestActions.createGoodOne();
    couponTestActions.createGoodTwo();
    // 2.创建VoucherItem
    couponTestActions.createVoucherItemOneForGoodOne();
    couponTestActions.createVoucherItemTwoForGoodTwo();
    // 3.创建GoodVoucher
    couponTestActions.createGoodVoucherOneWithVoucherItemOne();
    couponTestActions.createGoodVoucherTwoWithVoucherItemTwo();
    // 4.关联
    couponTestActions.applyGoodVoucherOneForGoodOne();
    couponTestActions.applyGoodVoucherTwoForGoodTwo();

    response = couponControllerClient.getGoodVoucherList(CouponTestActions.ADMIN_USER_TOKEN, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(1, response.getTotalpage());

    // 5.废弃GoodVoucherOne
    couponControllerClient.deprecateGoodVoucher(CouponTestActions.ADMIN_USER_TOKEN, couponTestActions.goodvoucheroneId,
        false);

    response = couponControllerClient.getGoodVoucherList(CouponTestActions.ADMIN_USER_TOKEN, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(couponTestActions.goodvouchertwoId, response.getDataList().get(0).getId());
    Assert.assertEquals(1, response.getTotalpage());
  }

}
