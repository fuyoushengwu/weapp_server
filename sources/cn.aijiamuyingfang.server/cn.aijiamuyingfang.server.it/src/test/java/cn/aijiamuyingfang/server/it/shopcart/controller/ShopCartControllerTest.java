package cn.aijiamuyingfang.server.it.shopcart.controller;

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

import cn.aijiamuyingfang.client.domain.exception.ShopCartException;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shopcart.response.GetShopCartListResponse;
import cn.aijiamuyingfang.client.rest.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.ITApplication;

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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class ShopCartControllerTest {

  @Autowired
  private ShopCartControllerClient shopcartControllerClient;

  @Autowired
  private ShopCartTestActions testActions;

  @Before
  public void before() throws IOException {
    testActions.clearShopCart();

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

    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();

    testActions.clearUser();
    testActions.clearRecieveAddress();
  }

  @Test
  @UseCaseDescription(description = "购物车中没有商品时获取购物车信息")
  public void test_GetShopCart_001() throws IOException {
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "购物车中有商品时获取购物车信息")
  public void test_GetShopCart_002() throws IOException {
    testActions.addGoodOne10();
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());
  }

  @Test
  @UseCaseDescription(description = "别人购物车中有商品,自己购物车中没有商品")
  public void test_GetShopCart_003() throws IOException {
    testActions.addGoodOne10();
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());

    response = shopcartControllerClient.getShopCartList(testActions.getSenderOne().getId(), 1, 10,
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "购物车中原来有商品,后来被删除")
  public void test_GetShopCart_004() throws IOException {
    ShopCart shopcartOne = testActions.addGoodOne10();
    ShopCart shopcartTwo = testActions.addGoodTwo10();
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(shopcartTwo.getId(), response.getDataList().get(0).getId());
    Assert.assertEquals(shopcartOne.getId(), response.getDataList().get(1).getId());

    testActions.deleteShopCart(shopcartOne);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(shopcartTwo.getId(), response.getDataList().get(0).getId());
  }

  @Test
  @UseCaseDescription(description = "测试购物车中没有商品时,选中所有项")
  public void test_CheckAllShopCart_001() throws IOException {
    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, true, testActions.getAdminAccessToken(),
        false);
  }

  @Test
  @UseCaseDescription(description = "测试购物车中有两件商品时,选中所有项")
  public void test_CheckAllShopCart_002() throws IOException {
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, false,
        testActions.getAdminAccessToken(), false);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

  }

  @Test
  @UseCaseDescription(description = "测试多次选中所有项")
  public void test_CheckAllShopCart_003() throws IOException {
    ShopCart shopcartOne = testActions.addGoodOne10();
    testActions.addGoodTwo10();
    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, false,
        testActions.getAdminAccessToken(), false);
    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, false,
        testActions.getAdminAccessToken(), false);
    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, false,
        testActions.getAdminAccessToken(), false);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_ID, true, testActions.getAdminAccessToken(),
        false);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertTrue(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkShopCart(AbstractTestAction.ADMIN_USER_ID, shopcartOne.getId(), false,
        testActions.getAdminAccessToken(), false);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
    Assert.assertFalse(response.getDataList().get(1).isIschecked());

    shopcartControllerClient.checkShopCart(AbstractTestAction.ADMIN_USER_ID, shopcartOne.getId(), true,
        testActions.getAdminAccessToken(), false);
    response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isIschecked());
  }

  @Test(expected = ShopCartException.class)
  @UseCaseDescription(description = "更新不存在的购物车数量")
  public void test_UpdateShopCartCount_001() throws IOException {
    shopcartControllerClient.updateShopCartCount(AbstractTestAction.ADMIN_USER_ID, "not_exitst", 1,
        testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "更新存在的购物车数量")
  public void test_UpdateShopCartCount_002() throws IOException {
    ShopCart shopcart = testActions.addGoodOne10();
    testActions.addGoodTwo10();
    shopcartControllerClient.updateShopCartCount(AbstractTestAction.ADMIN_USER_ID, shopcart.getId(), 20,
        testActions.getAdminAccessToken());

    GetShopCartListResponse response = shopcartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_ID, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());
    Assert.assertEquals(20, response.getDataList().get(1).getCount());

  }
}
