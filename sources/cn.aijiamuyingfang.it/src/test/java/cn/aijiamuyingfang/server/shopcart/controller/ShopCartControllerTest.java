package cn.aijiamuyingfang.server.shopcart.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_ID;
import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.exception.ShopCartException;
import cn.aijiamuyingfang.commons.domain.shopcart.response.GetShopCartItemListResponse;
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
 * ShopCartController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:11:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class ShopCartControllerTest {
  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Autowired
  private ShopCartControllerClient shopcartControllerClient;

  @Autowired
  private ShopCartTestActions testActions;

  @Test
  @TestDescription(description = "购物车中没有商品时获取购物车信息")
  public void test_GetShopCartItem_001() throws IOException {
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "购物车中有商品时获取购物车信息")
  public void test_GetShopCartItem_002() throws IOException {
    testActions.createGoodOne();
    testActions.addGoodOne10();
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "别人购物车中有商品,自己购物车中没有商品")
  public void test_GetShopCartItem_003() throws IOException {
    testActions.createGoodOne();
    testActions.addGoodOne10();
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(1, response.getDataList().size());

    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    response = shopcartControllerClient.getShopCartItemList(testActions.senderoneToken, testActions.senderoneId, 1, 10);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "购物车中原来有商品,后来被删除")
  public void test_GetShopCartItem_004() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(2, response.getDataList().size());

    testActions.deleteItemOne();
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "测试购物车中没有商品时,选中所有项")
  public void test_CheckAllShopCartItem_001() throws IOException {
    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, true, false);
  }

  @Test
  @TestDescription(description = "测试购物车中有两件商品时,选中所有项")
  public void test_CheckAllShopCartItem_002() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, false, false);
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

  }

  @Test
  @TestDescription(description = "测试多次选中所有项")
  public void test_CheckAllShopCartItem_003() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, false, false);
    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, false, false);
    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, false, false);
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, true, false);
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, testActions.itemoneId, false, false);
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, testActions.itemoneId, true, false);
    response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID, 1, 10);
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
  }

  @Test(expected = ShopCartException.class)
  @TestDescription(description = "更新不存在的购物车数量")
  public void test_UpdateShopCartItemCount_001() throws IOException {
    shopcartControllerClient.updateShopCartItemCount(ADMIN_USER_TOKEN, ADMIN_USER_ID, "not_exitst", 1);
    Assert.fail();
  }

  @Test
  @TestDescription(description = "更新存在的购物车数量")
  public void test_UpdateShopCartItemCount_002() throws IOException {
    testActions.createGoodOne();
    testActions.createGoodTwo();
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    shopcartControllerClient.updateShopCartItemCount(ADMIN_USER_TOKEN, ADMIN_USER_ID, testActions.itemoneId, 20);

    GetShopCartItemListResponse response = shopcartControllerClient.getShopCartItemList(ADMIN_USER_TOKEN, ADMIN_USER_ID,
        1, 10);
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());
    Assert.assertEquals(20, response.getDataList().get(1).getCount());

  }
}
