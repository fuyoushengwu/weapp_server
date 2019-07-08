package cn.aijiamuyingfang.server.it.shoporder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.ITApplication;
import cn.aijiamuyingfang.server.it.UseCaseDescription;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.preorder.PagablePreOrderGoodList;
import cn.aijiamuyingfang.vo.shoporder.PagableShopOrderList;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.user.RecieveAddress;

/**
 * [描述]:
 * <p>
 * ShopOrderController 的集成测试类
 * </p>
 *
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:11:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class ShopOrderControllerTest {
  @Autowired
  private ShoporderTestActions testActions;

  @Autowired
  private ShopOrderControllerClient shoporderControllerClient;

  @Autowired
  private GoodControllerClient goodControllerClient;

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
  @UseCaseDescription(description = "用户没有订单")
  public void test_GetUserShopOrderList_001() throws IOException {
    PagableShopOrderList response = shoporderControllerClient.getUserShopOrderList(AbstractTestAction.ADMIN_USER_NAME,
        null, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "用户有订单")
  public void test_GetUserShopOrderList_002() throws IOException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOnePreviewGoodOne();
    ShopOrder shoporder = testActions.senderOneBuy();

    PagableShopOrderList response = shoporderControllerClient.getUserShopOrderList(
        testActions.getSenderOne().getUsername(), null, null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    testActions.sendSenderOneShopOrder(shoporder);
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    testActions.senderOneConfirmOrder(shoporder);
    statusList.add(ShopOrderStatus.UNSTART);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), statusList,
        null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "系统中没有订单")
  public void test_GetShopOrderList_001() throws IOException {
    PagableShopOrderList response = shoporderControllerClient.getShopOrderList(null, null, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "系统中有订单")
  public void test_GetShopOrderList_002() throws IOException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    testActions.senderOnePreviewAllGood();
    ShopOrder shoporderOne = testActions.senderOneBuy();

    testActions.senderTwoAdd10GoodOne();
    testActions.senderTwoAdd10GoodTwo();
    testActions.senderTwoPreviewAllGood();
    testActions.senderTwoBuy();

    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    PagableShopOrderList response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());

    testActions.sendSenderOneShopOrder(shoporderOne);
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    testActions.senderOneConfirmOrder(shoporderOne);
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = shoporderControllerClient.getShopOrderList(statusList, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "删除100天外的订单")
  public void test_Delete100DaysFinishedShopOrder_001() throws IOException, InterruptedException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    testActions.senderOnePreviewAllGood();
    ShopOrder shoporderOne = testActions.senderOneBuy();

    testActions.senderTwoAdd10GoodOne();
    testActions.senderTwoAdd10GoodTwo();
    testActions.senderTwoPreviewAllGood();
    ShopOrder shoporderTwo = testActions.senderTwoBuy();

    testActions.sendSenderOneShopOrder(shoporderOne);
    testActions.senderOneConfirmOrder(shoporderOne);
    shoporderControllerClient.delete100DaysFinishedShopOrder(shoporderOne.getId(),
        testActions.getSenderOneAccessToken(), false);
    PagableShopOrderList response = shoporderControllerClient.getShopOrderList(null, null, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());

    cn.aijiamuyingfang.server.it.dto.shoporder.ShopOrderDTO shoporder = testActions.getShopOrder(shoporderOne.getId());
    shoporder.setFinishTime(new Date(System.currentTimeMillis() - 200 * 24 * 60 * 60 * 1000L));
    shoporder.setLastModify(new Date(System.currentTimeMillis() - 200 * 24 * 60 * 60 * 1000L));
    testActions.updateShopOrder(shoporder);

    Thread.sleep(5000);
    shoporderControllerClient.delete100DaysFinishedShopOrder(shoporderOne.getId(),
        testActions.getSenderOneAccessToken(), false);
    response = shoporderControllerClient.getShopOrderList(null, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());

    shoporderControllerClient.deleteUserShopOrder(testActions.getSenderTwo().getUsername(), shoporderTwo.getId(),
        testActions.getSenderTwoAccessToken(), false);
    response = shoporderControllerClient.getShopOrderList(null, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "更新订单收件地址")
  public void test_UpdateUserShopOrderRecieveAddress_001() throws IOException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    testActions.senderOnePreviewAllGood();
    ShopOrder senderOneShopOrder = testActions.senderOneBuy();

    RecieveAddress address = testActions.getSenderTwoRecieveOne();

    shoporderControllerClient.updateUserShopOrderRecieveAddress(testActions.getSenderOne().getUsername(),
        senderOneShopOrder.getId(), address.getId(), testActions.getSenderOneAccessToken(), false);

    ShopOrder shoporder = shoporderControllerClient.getUserShopOrder(testActions.getSenderOne().getUsername(),
        senderOneShopOrder.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(address.getId(), shoporder.getRecieveAddress().getId());
  }

  @Test
  @UseCaseDescription(description = "系统中没有预定订单")
  public void test_GetFinishedPreOrderList_001() throws IOException {
    PagableShopOrderList response = shoporderControllerClient.getFinishedPreOrderList(1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "系统中有预定订单")
  public void test_GetFinishedPreOrderList_002() throws IOException, InterruptedException {
    for (int i = 0; i < 10; i++) {
      testActions.senderOneAdd10GoodOne();
    }
    testActions.senderOneAdd10GoodTwo();
    testActions.senderOnePreviewAllGood();
    ShopOrder shoporderOne = testActions.senderOneBuy();
    testActions.sendSenderOneShopOrder(shoporderOne);

    Thread.sleep(2 * 1000);
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    testActions.senderOnePreviewAllGood();
    testActions.senderOneBuy();

    PagableShopOrderList response = shoporderControllerClient.getUserShopOrderList(
        testActions.getSenderOne().getUsername(), null, null, 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(ShopOrderStatus.PREORDER, response.getDataList().get(0).getStatus());
    Assert.assertEquals(ShopOrderStatus.DOING, response.getDataList().get(1).getStatus());

    PagablePreOrderGoodList getPreOrderGoodListResponse = shoporderControllerClient.getPreOrderGoodList(1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(1, getPreOrderGoodListResponse.getDataList().size());
    Assert.assertEquals(10, getPreOrderGoodListResponse.getDataList().get(0).getCount());
    Assert.assertEquals(testActions.getGoodOne().getId(),
        getPreOrderGoodListResponse.getDataList().get(0).getGood().getId());

    Map<String, Double> statusCount = shoporderControllerClient
        .getUserShopOrderStatusCount(testActions.getSenderOne().getUsername(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, statusCount.get("inOrderingCount").intValue());

    Good updateGoodRequest = new Good();
    updateGoodRequest.setCount(100);
    goodControllerClient.updateGood(testActions.getGoodOne().getId(), updateGoodRequest,
        testActions.getAdminAccessToken());

    Thread.sleep(10 * 1000);
    response = shoporderControllerClient.getUserShopOrderList(testActions.getSenderOne().getUsername(), null, null, 1,
        10, testActions.getSenderOneAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(ShopOrderStatus.UNSTART, response.getDataList().get(0).getStatus());
    Assert.assertEquals(ShopOrderStatus.DOING, response.getDataList().get(1).getStatus());

    statusCount = shoporderControllerClient.getUserShopOrderStatusCount(testActions.getSenderOne().getUsername(),
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(2, statusCount.get("tobeReceiveCount").intValue());
  }

}
