package cn.aijiamuyingfang.server.shoporder.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_ID;
import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.server.client.api.impl.ShopOrderControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.address.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.shoporder.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.GetUserShopOrderListResponse;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.db.ShopOrderRepository;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class ShopOrderControllerTest {
  @Autowired
  private ShoporderTestActions testActions;

  @Autowired
  private ShopOrderControllerClient client;

  @Autowired
  private ShopOrderRepository shoporderRepository;

  @Autowired
  private RecieveAddressRepository recieveaddressRepository;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test
  @TestDescription(description = "用户没有订单")
  public void test_GetUserShopOrderList_001() throws IOException {
    GetUserShopOrderListResponse response = client.getUserShopOrderList(ADMIN_USER_TOKEN, ADMIN_USER_ID, null, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "用户有订单")
  public void test_GetUserShopOrderList_002() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();

    testActions.createGoodOne();

    testActions.senderoneAdd10GoodOne();
    testActions.senderonePreviewGoodOne();
    testActions.senderoneBuy();

    GetUserShopOrderListResponse response = client.getUserShopOrderList(testActions.senderoneToken,
        testActions.senderoneId, null, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());

    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(1, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());

    testActions.sendSenderOneShopOrder();
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(1, response.getDataList().size());

    testActions.senderoneConfirmOrder();
    statusList.add(ShopOrderStatus.UNSTART);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());

    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(0, response.getDataList().size());
    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, statusList, null, 1,
        10);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "系统中没有订单")
  public void test_GetShopOrderList_001() throws IOException {
    GetShopOrderListResponse response = client.getShopOrderList(ADMIN_USER_TOKEN, null, null, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "系统中有订单")
  public void test_GetShopOrderList_002() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();

    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();
    testActions.createSenderTwoUser();
    testActions.getSenderTwoToken();
    testActions.addSenderTwoRecieveOne();

    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    testActions.senderonePreviewAllGood();
    testActions.senderoneBuy();

    testActions.sendertwoAdd10GoodOne();
    testActions.sendertwoAdd10GoodTwo();
    testActions.sendertwoPreviewAllGood();
    testActions.sendertwoBuy();
    List<ShopOrderStatus> statusList = new ArrayList<>();
    statusList.add(ShopOrderStatus.UNSTART);
    GetShopOrderListResponse response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());

    testActions.sendSenderOneShopOrder();
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());
    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());
    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());

    testActions.senderoneConfirmOrder();
    statusList.clear();
    statusList.add(ShopOrderStatus.UNSTART);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());
    statusList.clear();
    statusList.add(ShopOrderStatus.DOING);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());
    statusList.clear();
    statusList.add(ShopOrderStatus.FINISHED);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, statusList, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "删除100天外的订单")
  public void test_Delete100DaysFinishedShopOrder_001() throws IOException, InterruptedException {
    testActions.createGoodOne();
    testActions.createGoodTwo();

    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();
    testActions.createSenderTwoUser();
    testActions.getSenderTwoToken();
    testActions.addSenderTwoRecieveOne();

    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    testActions.senderonePreviewAllGood();
    testActions.senderoneBuy();

    testActions.sendertwoAdd10GoodOne();
    testActions.sendertwoAdd10GoodTwo();
    testActions.sendertwoPreviewAllGood();
    testActions.sendertwoBuy();

    testActions.sendSenderOneShopOrder();
    testActions.senderoneConfirmOrder();
    client.delete100DaysFinishedShopOrder(testActions.senderoneToken, testActions.senderoneShoporderId, false);
    GetShopOrderListResponse response = client.getShopOrderList(ADMIN_USER_TOKEN, null, null, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());

    ShopOrder shoporder = shoporderRepository.findOne(testActions.senderoneShoporderId);
    shoporder.setFinishTime(new Date(System.currentTimeMillis() - 101 * 24 * 60 * 60 * 1000L));
    shoporder.setLastModify(new Date(System.currentTimeMillis() - 101 * 24 * 60 * 60 * 1000L));
    shoporderRepository.saveAndFlush(shoporder);
    Thread.sleep(2000);
    client.delete100DaysFinishedShopOrder(testActions.senderoneToken, testActions.senderoneShoporderId, false);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, null, null, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());

    client.deleteUserShopOrder(testActions.sendertwoToken, testActions.sendertwoId, testActions.sendertwoShoporderId,
        false);
    response = client.getShopOrderList(ADMIN_USER_TOKEN, null, null, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());

  }

  @Test
  @TestDescription(description = "更新订单收件地址")
  public void test_UpdateUserShopOrderRecieveAddress_001() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();

    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();

    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    testActions.senderonePreviewAllGood();
    testActions.senderoneBuy();

    RecieveAddress address = new RecieveAddress();
    address.setUserid(testActions.senderoneId);
    recieveaddressRepository.saveAndFlush(address);

    client.updateUserShopOrderRecieveAddress(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneShoporderId, address.getId(), false);

    ShopOrder shoporder = client.getUserShopOrder(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneShoporderId);
    Assert.assertEquals(address.getId(), shoporder.getRecieveAddress().getId());
  }

  @Test
  @TestDescription(description = "系统中没有预定订单")
  public void test_GetFinishedPreOrderList_001() throws IOException {
    GetFinishedPreOrderListResponse response = client.getFinishedPreOrderList(ADMIN_USER_TOKEN, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "系统中有预定订单")
  public void test_GetFinishedPreOrderList_002() throws IOException, InterruptedException {
    testActions.createGoodOne();
    testActions.createGoodTwo();

    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();

    for (int i = 0; i < 10; i++) {
      testActions.senderoneAdd10GoodOne();
    }
    testActions.senderoneAdd10GoodTwo();
    testActions.senderonePreviewAllGood();
    testActions.senderoneBuy();
    testActions.sendSenderOneShopOrder();
    Thread.sleep(2 * 1000);
    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    testActions.senderonePreviewAllGood();
    testActions.senderoneBuy();

    GetUserShopOrderListResponse response = client.getUserShopOrderList(testActions.senderoneToken,
        testActions.senderoneId, null, null, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(ShopOrderStatus.PREORDER, response.getDataList().get(0).getStatus());
    Assert.assertEquals(ShopOrderStatus.DOING, response.getDataList().get(1).getStatus());

    GetPreOrderGoodListResponse getPreOrderGoodListResponse = client.getPreOrderGoodList(ADMIN_USER_TOKEN, 1, 10);
    Assert.assertEquals(1, getPreOrderGoodListResponse.getDataList().size());
    Assert.assertEquals(10, getPreOrderGoodListResponse.getDataList().get(0).getCount());
    Assert.assertEquals(testActions.goodoneId, getPreOrderGoodListResponse.getDataList().get(0).getGood().getId());

    Map<String,
        Double> statusCount = client.getUserShopOrderStatusCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(1, statusCount.get("inOrderingCount").intValue());

    GoodRequest request = new GoodRequest();
    request.setCount(100);
    testActions.goodControllerClient.updateGood(ADMIN_USER_TOKEN, testActions.goodoneId, request);
    Thread.sleep(10 * 1000);
    response = client.getUserShopOrderList(testActions.senderoneToken, testActions.senderoneId, null, null, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(ShopOrderStatus.UNSTART, response.getDataList().get(0).getStatus());
    Assert.assertEquals(ShopOrderStatus.DOING, response.getDataList().get(1).getStatus());

    statusCount = client.getUserShopOrderStatusCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(2, statusCount.get("tobeReceiveCount").intValue());
  }

}
