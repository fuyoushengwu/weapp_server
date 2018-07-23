package cn.aijiamuyingfang.server.shoporder.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_ID;
import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.PreviewOrderControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
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

/**
 * [描述]:
 * <p>
 * PreviewOrderController 的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:11:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class PreviewOrderControllerTest {
  @Autowired
  private ShoporderTestActions testActions;

  @Autowired
  private PreviewOrderControllerClient client;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test(expected = ShopOrderException.class)
  @TestDescription(description = "更新不存在的预览项")
  public void test_UpdatePreviewOrderItem_001() throws IOException {
    PreviewOrderItem request = new PreviewOrderItem();
    request.setCount(10);
    request.setShopcartItemId("not_exist");
    client.updatePreviewOrderItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, "not_exist_id", request);
  }

  @Test
  @TestDescription(description = "更新存在的预览项")
  public void test_UpdatePreviewOrderItem_002() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    PreviewOrder previeworder = testActions.senderonePreviewAllGood();
    Assert.assertNotNull(previeworder);
    Assert.assertEquals(2, previeworder.getOrderItemList().size());
    PreviewOrderItem previeworderItem = previeworder.getOrderItemList().get(0);
    Assert.assertEquals(10, previeworderItem.getCount());

    PreviewOrderItem request = new PreviewOrderItem();
    request.setCount(1);
    request.setShopcartItemId(previeworderItem.getId());
    request.setGood(previeworderItem.getGood());
    PreviewOrderItem updatedItem = client.updatePreviewOrderItem(testActions.senderoneToken, testActions.senderoneId,
        previeworderItem.getId(), request);
    Assert.assertEquals(1, updatedItem.getCount());

    request = new PreviewOrderItem();
    request.setCount(0);
    request.setShopcartItemId(previeworderItem.getId());
    request.setGood(previeworderItem.getGood());
    updatedItem = client.updatePreviewOrderItem(testActions.senderoneToken, testActions.senderoneId,
        previeworderItem.getId(), request);
    Assert.assertEquals(1, updatedItem.getCount());
  }

  @Test(expected = ShopOrderException.class)
  @TestDescription(description = "测试删除预览项")
  public void test_DeletePreviewOrderItem_001() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.senderoneAdd10GoodOne();
    testActions.senderoneAdd10GoodTwo();
    PreviewOrder previeworder = testActions.senderonePreviewAllGood();
    PreviewOrderItem previeworderItem = previeworder.getOrderItemList().get(0);
    client.deletePreviewOrderItem(testActions.senderoneToken, testActions.senderoneId, previeworderItem.getId(), false);

    PreviewOrderItem request = new PreviewOrderItem();
    request.setCount(1);
    request.setShopcartItemId(previeworderItem.getId());
    request.setGood(previeworderItem.getGood());
    client.updatePreviewOrderItem(testActions.senderoneToken, testActions.senderoneId, previeworderItem.getId(),
        request);
  }
}
