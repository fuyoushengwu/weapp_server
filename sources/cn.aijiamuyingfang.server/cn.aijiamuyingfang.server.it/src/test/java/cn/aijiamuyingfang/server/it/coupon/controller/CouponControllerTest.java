package cn.aijiamuyingfang.server.it.coupon.controller;

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

import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrder;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.client.domain.shoporder.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.client.domain.shoporder.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.client.rest.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.ITApplication;

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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class CouponControllerTest {
  @Autowired
  private CouponTestActions testActions;

  @Autowired
  private CouponControllerClient couponControllerClient;

  @Autowired
  private ShopOrderControllerClient shoporderControllerClient;

  @Before
  public void before() throws IOException {
    testActions.clearShopCart();
    testActions.clearShopOrder();
    testActions.clearPreviewOrder();

    testActions.clearUserVoucher();
    testActions.clearGoodVoucher();
    testActions.clearVoucherItem();

    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();

    testActions.clearUser();
    testActions.clearRecieveAddress();
  }

  @After
  public void after() {
    testActions.clearShopCart();
    testActions.clearShopOrder();
    testActions.clearPreviewOrder();

    testActions.clearUserVoucher();
    testActions.clearGoodVoucher();
    testActions.clearVoucherItem();

    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();

    testActions.clearUser();
    testActions.clearRecieveAddress();
  }

  @Test
  @UseCaseDescription(description = "当前用户没有兑换券时获得不到用户兑换券")
  public void testGetUserVoucherList_001() throws IOException {
    GetUserVoucherListResponse response = couponControllerClient.getUserVoucherList(AbstractTestAction.ADMIN_USER_NAME,
        1, 10, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getCurrentPage());
    Assert.assertEquals(0, response.getTotalpage());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "当前系统有两个用户,一个用户有兑换券一个用户没有兑换券")
  public void testGetUserVoucherList_002() throws IOException {
    // 1.创建VoucherItem:voucher item 1
    VoucherItem voucherItem = testActions.getVoucherItemOneForGoodOne();
    Assert.assertNotNull(voucherItem);

    // 2.创建商品兑换券:good voucher 1
    GoodVoucher goodVoucher = testActions.getGoodVoucherOneWithVoucherItemOne();
    Assert.assertNotNull(goodVoucher);

    // 3.关联商品(good 1)和商品兑换券(good voucher 1)
    Good updatedGood = testActions.applyGoodVoucherOneForGoodOne();
    Assert.assertNotNull(updatedGood.getVoucherId());

    // 4.用户(Sender)添加购物车
    ShopCart shopCart = testActions.senderOneAdd10GoodOne();
    Assert.assertNotNull(shopCart);

    // 5.用户(Sender)预览订单
    PreviewOrder previeworder = testActions.senderOnePreviewGoodOne();
    Assert.assertNotNull(previeworder);

    // 6.用户(Sender)购买商品
    ShopOrder shoporder = testActions.senderOneBuy();
    Assert.assertNotNull(shoporder);

    // 7.送货员送货
    testActions.sendSenderOneShopOrder(shoporder);

    // 8.用户(Sender)确认订单
    ConfirmShopOrderFinishedResponse confirmResponse = testActions.senderOneConfirmOrder(shoporder);
    Assert.assertNotNull(confirmResponse);

    // 9.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient
        .getUserVoucherList(testActions.getSenderOne().getUsername(), 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(150, uservoucher.getScore());
    Assert.assertEquals(goodVoucher.getId(), uservoucher.getGoodVoucher().getId());

    // 10.确认用户(Admin)名下没有兑换券
    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(0, getUserVoucherListResponse.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "用户名下没有兑换券,买商品")
  public void testGetUserShopOrderVoucherList_001() throws IOException {
    // 1.创建GoodVoucher: Good Voucher One
    testActions.getGoodVoucherOneWithVoucherItemOne();
    // 2.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    // 3.添加GoodOne到购物车
    testActions.senderOneAdd10GoodOne();
    // 4.预览商品
    testActions.senderOnePreviewGoodOne();
    // 5.购买商品
    ShopOrder shoporder = testActions.senderOneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1010, shoporder.getTotalPrice(), 0.0001);
  }

  @Test
  @UseCaseDescription(description = "用户名下有兑换券,但是不足额,买商品")
  public void testGetUserShopOrderVoucherList_002() throws IOException {
    // 1.创建GoodVoucher: Good Voucher One
    testActions.getGoodVoucherOneWithVoucherItemOne();
    // 2.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    // 3.添加GoodOne到购物车
    testActions.senderOneAdd5GoodOne();
    // 4.预览商品
    testActions.senderOnePreviewGoodOne();
    // 5.购买商品
    ShopOrder shoporder = testActions.senderOneBuy();
    // 6.送货员送货
    testActions.sendSenderOneShopOrder(shoporder);
    // 7.用户(Sender)确认订单
    testActions.senderOneConfirmOrder(shoporder);
    // 8.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient
        .getUserVoucherList(testActions.getSenderOne().getUsername(), 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(75, uservoucher.getScore());

    // 7.添加GoodOne到购物车
    testActions.senderOneAdd5GoodOne();
    // 8.预览商品
    testActions.senderOnePreviewGoodOne();
    // 9.购买商品
    shoporder = testActions.senderOneBuy();
    Assert.assertEquals(0, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(510, shoporder.getTotalPrice(), 0.0001);

  }

  @Test
  @UseCaseDescription(description = "用户名下有足额的兑换券,使用兑换券买商品(其中一种是可以用兑换券的,另一种是不可以用兑换券的)")
  public void testGetUserShopOrderVoucherList_003() throws IOException {
    // 1.创建VoucherItem:Voucher Item One for Good One
    VoucherItem voucherItemOne = testActions.getVoucherItemOneForGoodOne();
    VoucherItem voucherItemTwo = testActions.getVoucherItemTwoForGoodTwo();
    // 2.创建GoodVoucher: Good Voucher One
    testActions.getGoodVoucherOneWithVoucherItemOne();
    testActions.getGoodVoucherTwoWithVoucherItemTwo();
    // 3.关联GoodOne和GoodVoucherOne
    testActions.applyGoodVoucherOneForGoodOne();
    testActions.applyGoodVoucherTwoForGoodTwo();
    // 4.添加GoodOne到购物车
    testActions.senderOneAdd10GoodOne();
    // 5.预览商品
    testActions.senderOnePreviewGoodOne();
    // 6.购买商品
    ShopOrder shoporder = testActions.senderOneBuy();
    // 7.送货员送货
    testActions.sendSenderOneShopOrder(shoporder);
    // 8.用户(Sender)确认订单
    testActions.senderOneConfirmOrder(shoporder);
    // 9.确认用户(Sender)名下有兑换券
    GetUserVoucherListResponse getUserVoucherListResponse = couponControllerClient
        .getUserVoucherList(testActions.getSenderOne().getUsername(), 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(1, getUserVoucherListResponse.getDataList().size());
    UserVoucher uservoucher = getUserVoucherListResponse.getDataList().get(0);
    Assert.assertEquals(150, uservoucher.getScore());

    // 7.添加GoodOne,GoodTwo到购物车
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    // 8.预览商品
    testActions.senderOnePreviewAllGood();
    // 9.购买商品
    shoporder = testActions.senderOneBuy();
    Assert.assertEquals(1, shoporder.getOrderVoucher().size());
    Assert.assertEquals(10, shoporder.getSendPrice(), 0.0001);
    Assert.assertEquals(1910, shoporder.getTotalPrice(), 0.0001);

    // 9.送货员送货
    testActions.sendSenderOneShopOrder(shoporder);
    // 10.用户(Sender)确认订单
    testActions.senderOneConfirmOrder(shoporder);

    getUserVoucherListResponse = couponControllerClient.getUserVoucherList(testActions.getSenderOne().getUsername(), 1,
        10, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getUserVoucherListResponse);
    Assert.assertEquals(2, getUserVoucherListResponse.getDataList().size());
    Assert.assertEquals(testActions.getGoodVoucherOneWithVoucherItemOne().getId(),
        getUserVoucherListResponse.getDataList().get(0).getGoodVoucher().getId());
    Assert.assertEquals(180, getUserVoucherListResponse.getDataList().get(0).getScore());
    Assert.assertEquals(testActions.getGoodVoucherTwoWithVoucherItemTwo().getId(),
        getUserVoucherListResponse.getDataList().get(1).getGoodVoucher().getId());
    Assert.assertEquals(150, getUserVoucherListResponse.getDataList().get(1).getScore());

    GetVoucherItemListResponse getVoucherItemListResponse = couponControllerClient.getVoucherItemList(1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(2, getVoucherItemListResponse.getDataList().size());

    voucherItemOne = couponControllerClient.getVoucherItem(voucherItemOne.getId());
    Assert.assertNotNull(voucherItemOne);
    voucherItemTwo = couponControllerClient.getVoucherItem(voucherItemTwo.getId());
    Assert.assertNotNull(voucherItemTwo);

    List<String> goodIdList = new ArrayList<>();
    goodIdList.add(testActions.getGoodOne().getId());
    goodIdList.add(testActions.getGoodTwo().getId());
    GetShopOrderVoucherListResponse getShopOrderVoucherListResponse = shoporderControllerClient
        .getUserShopOrderVoucherList(testActions.getSenderOne().getUsername(), goodIdList,
            testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(2, getShopOrderVoucherListResponse.getVoucherList().size());

    couponControllerClient.deprecateVoucherItem(voucherItemOne.getId(), testActions.getAdminAccessToken(), false);
    getVoucherItemListResponse = couponControllerClient.getVoucherItemList(1, 10);
    Assert.assertNotNull(getVoucherItemListResponse);
    Assert.assertEquals(1, getVoucherItemListResponse.getDataList().size());
    Assert.assertEquals(voucherItemTwo.getId(), getVoucherItemListResponse.getDataList().get(0).getId());

    getShopOrderVoucherListResponse = shoporderControllerClient.getUserShopOrderVoucherList(
        testActions.getSenderOne().getUsername(), goodIdList, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(getShopOrderVoucherListResponse);
    Assert.assertEquals(1, getShopOrderVoucherListResponse.getVoucherList().size());
    Assert.assertEquals(voucherItemTwo.getId(),
        getShopOrderVoucherListResponse.getVoucherList().get(0).getVoucherItemId());
  }

  @Test
  @UseCaseDescription(description = "原来有两个商品兑换券,废弃其中一个")
  public void test_GetGoodVoucherList_001() throws IOException {
    GetGoodVoucherListResponse response = couponControllerClient.getGoodVoucherList(1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
    Assert.assertEquals(0, response.getTotalpage());
    // 1.创建VoucherItem
    testActions.getVoucherItemOneForGoodOne();
    testActions.getVoucherItemTwoForGoodTwo();
    // 2.创建GoodVoucher
    GoodVoucher goodVoucherOne = testActions.getGoodVoucherOneWithVoucherItemOne();
    GoodVoucher goodVoucherTwo = testActions.getGoodVoucherTwoWithVoucherItemTwo();
    // 3.关联
    testActions.applyGoodVoucherOneForGoodOne();
    testActions.applyGoodVoucherTwoForGoodTwo();

    response = couponControllerClient.getGoodVoucherList(1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(1, response.getTotalpage());

    // 4.废弃GoodVoucherOne
    couponControllerClient.deprecateGoodVoucher(goodVoucherOne.getId(), false, testActions.getAdminAccessToken());

    response = couponControllerClient.getGoodVoucherList(1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(goodVoucherTwo.getId(), response.getDataList().get(0).getId());
    Assert.assertEquals(1, response.getTotalpage());

    GoodVoucher goodVoucher = couponControllerClient.getGoodVoucher(goodVoucherTwo.getId());
    Assert.assertNotNull(goodVoucher);
  }

}
