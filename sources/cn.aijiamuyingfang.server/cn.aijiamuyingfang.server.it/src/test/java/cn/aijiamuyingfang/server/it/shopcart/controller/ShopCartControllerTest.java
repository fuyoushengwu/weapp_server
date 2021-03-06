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

import cn.aijiamuyingfang.client.rest.api.impl.ShopCartControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.ITApplication;
import cn.aijiamuyingfang.server.it.UseCaseDescription;
import cn.aijiamuyingfang.vo.exception.ShopCartException;
import cn.aijiamuyingfang.vo.shopcart.PagableShopCartList;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;

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
  private ShopCartControllerClient shopCartControllerClient;

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
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "购物车中有商品时获取购物车信息")
  public void test_GetShopCart_002() throws IOException {
    testActions.addGoodOne10();
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());
  }

  @Test
  @UseCaseDescription(description = "别人购物车中有商品,自己购物车中没有商品")
  public void test_GetShopCart_003() throws IOException {
    testActions.addGoodOne10();
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());

    response = shopCartControllerClient.getShopCartList(testActions.getSenderOne().getUsername(), 1, 10,
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "购物车中原来有商品,后来被删除")
  public void test_GetShopCart_004() throws IOException {
    ShopCart shopCartOne = testActions.addGoodOne10();
    ShopCart shopCartTwo = testActions.addGoodTwo10();
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(shopCartTwo.getId(), response.getDataList().get(0).getId());
    Assert.assertEquals(shopCartOne.getId(), response.getDataList().get(1).getId());

    testActions.deleteShopCart(shopCartOne);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(shopCartTwo.getId(), response.getDataList().get(0).getId());
  }

  @Test
  @UseCaseDescription(description = "测试购物车中没有商品时,选中所有项")
  public void test_CheckAllShopCart_001() throws IOException {
    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, true,
        testActions.getAdminAccessToken(), false);
  }

  @Test
  @UseCaseDescription(description = "测试购物车中有两件商品时,选中所有项")
  public void test_CheckAllShopCart_002() throws IOException {
    testActions.addGoodOne10();
    testActions.addGoodTwo10();
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isChecked());
    Assert.assertTrue(response.getDataList().get(1).isChecked());

    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, false,
        testActions.getAdminAccessToken(), false);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isChecked());
    Assert.assertFalse(response.getDataList().get(1).isChecked());

  }

  @Test
  @UseCaseDescription(description = "测试多次选中所有项")
  public void test_CheckAllShopCart_003() throws IOException {
    ShopCart shopCartOne = testActions.addGoodOne10();
    testActions.addGoodTwo10();
    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertTrue(response.getDataList().get(0).isChecked());
    Assert.assertTrue(response.getDataList().get(1).isChecked());

    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, false,
        testActions.getAdminAccessToken(), false);
    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, false,
        testActions.getAdminAccessToken(), false);
    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, false,
        testActions.getAdminAccessToken(), false);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertFalse(response.getDataList().get(0).isChecked());
    Assert.assertFalse(response.getDataList().get(1).isChecked());

    shopCartControllerClient.checkAllShopCart(AbstractTestAction.ADMIN_USER_NAME, true,
        testActions.getAdminAccessToken(), false);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isChecked());
    Assert.assertTrue(response.getDataList().get(1).isChecked());

    shopCartControllerClient.checkShopCart(AbstractTestAction.ADMIN_USER_NAME, shopCartOne.getId(), false,
        testActions.getAdminAccessToken(), false);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isChecked());
    Assert.assertFalse(response.getDataList().get(1).isChecked());

    shopCartControllerClient.checkShopCart(AbstractTestAction.ADMIN_USER_NAME, shopCartOne.getId(), true,
        testActions.getAdminAccessToken(), false);
    response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1, 10,
        testActions.getAdminAccessToken());
    Assert.assertTrue(response.getDataList().get(0).isChecked());
  }

  @Test(expected = ShopCartException.class)
  @UseCaseDescription(description = "更新不存在的购物车数量")
  public void test_UpdateShopCartCount_001() throws IOException {
    shopCartControllerClient.updateShopCartCount(AbstractTestAction.ADMIN_USER_NAME, "not_exitst", 1,
        testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "更新存在的购物车数量")
  public void test_UpdateShopCartCount_002() throws IOException {
    ShopCart shopCart = testActions.addGoodOne10();
    testActions.addGoodTwo10();
    shopCartControllerClient.updateShopCartCount(AbstractTestAction.ADMIN_USER_NAME, shopCart.getId(), 20,
        testActions.getAdminAccessToken());

    PagableShopCartList response = shopCartControllerClient.getShopCartList(AbstractTestAction.ADMIN_USER_NAME, 1,
        10, testActions.getAdminAccessToken());
    Assert.assertEquals(2, response.getDataList().size());
    Assert.assertEquals(10, response.getDataList().get(0).getCount());
    Assert.assertEquals(20, response.getDataList().get(1).getCount());

  }
}
