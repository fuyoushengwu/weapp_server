package cn.aijiamuyingfang.server.it.shoporder.controller;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aijiamuyingfang.client.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrder;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrderItem;
import cn.aijiamuyingfang.client.rest.api.impl.PreviewOrderControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.ITApplication;

/***
 * [描述]:
 * <p>
 * PreviewOrderController 的集成测试类*
 * </p>
 *
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:11:41
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class PreviewOrderControllerTest {
  @Autowired
  private ShoporderTestActions testActions;

  @Autowired
  private PreviewOrderControllerClient previeworderControllerClient;

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

  @Test(expected = ShopOrderException.class)
  @UseCaseDescription(description = "更新不存在的预览项")
  public void test_UpdatePreviewOrderItem_001() throws IOException {
    PreviewOrderItem updatePrevieworderRequest = new PreviewOrderItem();
    updatePrevieworderRequest.setCount(10);
    previeworderControllerClient.updatePreviewOrderItem(AbstractTestAction.ADMIN_USER_ID, "not_exist_id",
        updatePrevieworderRequest, testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "更新存在的预览项")
  public void test_UpdatePreviewOrderItem_002() throws IOException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    PreviewOrder previeworder = testActions.senderOnePreviewAllGood();
    Assert.assertNotNull(previeworder);
    Assert.assertEquals(2, previeworder.getOrderItemList().size());
    PreviewOrderItem previeworderItem = previeworder.getOrderItemList().get(0);
    Assert.assertEquals(10, previeworderItem.getCount());

    PreviewOrderItem updatePreviewOrderItemRequest = new PreviewOrderItem();
    updatePreviewOrderItemRequest.setCount(1);
    updatePreviewOrderItemRequest.setShopcartId(previeworderItem.getId());
    updatePreviewOrderItemRequest.setGoodId(previeworderItem.getGoodId());
    PreviewOrderItem updatedItem = previeworderControllerClient.updatePreviewOrderItem(
        testActions.getSenderOne().getId(), previeworderItem.getId(), updatePreviewOrderItemRequest,
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, updatedItem.getCount());

    updatePreviewOrderItemRequest = new PreviewOrderItem();
    updatePreviewOrderItemRequest.setCount(0);
    updatePreviewOrderItemRequest.setShopcartId(previeworderItem.getId());
    updatePreviewOrderItemRequest.setGoodId(previeworderItem.getGoodId());
    updatedItem = previeworderControllerClient.updatePreviewOrderItem(testActions.getSenderOne().getId(),
        previeworderItem.getId(), updatePreviewOrderItemRequest, testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, updatedItem.getCount());
  }

  @Test(expected = ShopOrderException.class)
  @UseCaseDescription(description = "测试删除预览项")
  public void test_DeletePreviewOrderItem_001() throws IOException {
    testActions.senderOneAdd10GoodOne();
    testActions.senderOneAdd10GoodTwo();
    PreviewOrder previeworder = testActions.senderOnePreviewAllGood();
    PreviewOrderItem previeworderItem = previeworder.getOrderItemList().get(0);
    previeworderControllerClient.deletePreviewOrderItem(testActions.getSenderOne().getId(), previeworderItem.getId(),
        testActions.getSenderOneAccessToken(), false);

    PreviewOrderItem updatePreviewItemRequest = new PreviewOrderItem();
    updatePreviewItemRequest.setCount(1);
    updatePreviewItemRequest.setShopcartId(previeworderItem.getId());
    updatePreviewItemRequest.setGoodId(previeworderItem.getGoodId());
    previeworderControllerClient.updatePreviewOrderItem(testActions.getSenderOne().getId(), previeworderItem.getId(),
        updatePreviewItemRequest, testActions.getSenderOneAccessToken());
  }
}
