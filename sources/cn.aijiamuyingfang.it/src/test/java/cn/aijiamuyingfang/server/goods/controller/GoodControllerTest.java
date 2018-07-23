package cn.aijiamuyingfang.server.goods.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodDetail;
import cn.aijiamuyingfang.commons.domain.goods.response.GetClassifyGoodListResponse;
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
 * GoodController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:08:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class GoodControllerTest {
  @Autowired
  private GoodsTestActions testActions;

  @Autowired
  private GoodControllerClient goodcontrollerClient;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "条目不存在")
  public void test_GetClassifyGoodList_001() throws IOException {
    goodcontrollerClient.getClassifyGoodList(ADMIN_USER_TOKEN, "not_exist_classify", null, null, null, null, 1, 10);
    Assert.fail();
  }

  @Test
  @TestDescription(description = "条目下没有商品")
  public void test_GetClassifyGoodList_002() throws IOException {
    testActions.createStoreOne();
    testActions.createClassifyOne();
    testActions.applyClassifyOneForStoreOne();
    GetClassifyGoodListResponse response = goodcontrollerClient.getClassifyGoodList(ADMIN_USER_TOKEN,
        testActions.classifyoneId, null, null, null, null, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @TestDescription(description = "条目下有商品")
  public void test_GetClassifyGoodList_003() throws IOException {
    testActions.createStoreOne();
    testActions.createClassifyOne();
    testActions.createSubClassifyOneForClassifyOne();
    testActions.applyClassifyOneForStoreOne();
    testActions.createGoodOne();
    testActions.applyGoodOneForSubClassifyOne();
    GetClassifyGoodListResponse response = goodcontrollerClient.getClassifyGoodList(ADMIN_USER_TOKEN,
        testActions.subclassifyoneId, null, null, null, null, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "获取不存在的Good")
  public void test_GetGood_001() throws IOException {
    goodcontrollerClient.getGood(ADMIN_USER_TOKEN, "not_exist_goodid");
    Assert.fail();
  }

  @Test
  @TestDescription(description = "获取存在的Good")
  public void test_GetGood_002() throws IOException {
    testActions.createGoodOne();
    Good good = goodcontrollerClient.getGood(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.assertEquals(testActions.goodoneId, good.getId());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "获取废弃的Good")
  public void test_GetGood_003() throws IOException {
    testActions.createGoodOne();
    Good good = goodcontrollerClient.getGood(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.assertEquals(testActions.goodoneId, good.getId());
    testActions.deprecateGoodOne();
    goodcontrollerClient.getGood(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.fail();
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "update不存在的Good")
  public void test_UpdateGood_001() throws IOException {
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood(ADMIN_USER_TOKEN, "not_exist_goodid", goodRequest);
    Assert.fail();
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "update废弃的Good")
  public void test_UpdateGood_002() throws IOException {
    testActions.createGoodOne();
    testActions.deprecateGoodOne();
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood(ADMIN_USER_TOKEN, testActions.goodoneId, goodRequest);
    Assert.fail();
  }

  @Test
  @TestDescription(description = "update Good")
  public void test_UpdateGood_003() throws IOException {
    testActions.createGoodOne();
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood(ADMIN_USER_TOKEN, testActions.goodoneId, goodRequest);
    Good good = goodcontrollerClient.getGood(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.assertEquals("good one rename", good.getName());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "不存在的gooddetail")
  public void test_GetGoodDetail_001() throws IOException {
    goodcontrollerClient.getGoodDetail(ADMIN_USER_TOKEN, "not_exist_goodid");
    Assert.fail();
  }

  @Test
  @TestDescription(description = "废弃的gooddetail")
  public void test_GetGoodDetail_002() throws IOException {
    testActions.createGoodOne();
    testActions.deprecateGoodOne();
    GoodDetail gooddetail = goodcontrollerClient.getGoodDetail(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.assertNotNull(gooddetail);
    Assert.assertEquals(testActions.goodoneId, gooddetail.getId());
  }

  @Test
  @TestDescription(description = "gooddetail")
  public void test_GetGoodDetail_003() throws IOException {
    testActions.createGoodOne();
    GoodDetail goodDetail = goodcontrollerClient.getGoodDetail(ADMIN_USER_TOKEN, testActions.goodoneId);
    Assert.assertNotNull(goodDetail);
    Assert.assertEquals(testActions.goodoneId, goodDetail.getId());
  }
}
