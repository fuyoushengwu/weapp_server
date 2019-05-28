package cn.aijiamuyingfang.server.it.goods.controller;

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

import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.domain.classify.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.client.domain.exception.GoodsException;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.domain.goods.GoodDetail;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.ITApplication;

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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class GoodControllerTest {
  @Autowired
  private GoodsTestActions testActions;

  @Autowired
  private GoodControllerClient goodcontrollerClient;

  @Before
  public void before() throws IOException {
    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();
  }

  @After
  public void after() {
    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "条目不存在")
  public void test_GetClassifyGoodList_001() throws IOException {
    goodcontrollerClient.getClassifyGoodList("not_exist_classify", null, null, null, null, 1, 10,
        testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "条目下没有商品")
  public void test_GetClassifyGoodList_002() throws IOException {
    Store storeOne = testActions.getStoreOne();
    Assert.assertNotNull(storeOne);
    Classify classifyOne = testActions.getClassifyOne();
    GetClassifyGoodListResponse response = goodcontrollerClient.getClassifyGoodList(classifyOne.getId(), null, null,
        null, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "条目下有商品")
  public void test_GetClassifyGoodList_003() throws IOException {
    Classify subClassifyOne = testActions.getSubClassifyOneForClassifyOne();
    testActions.addGoodOneForSubClassifyOne();
    GetClassifyGoodListResponse response = goodcontrollerClient.getClassifyGoodList(subClassifyOne.getId(), null, null,
        null, null, 1, 10, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "获取不存在的Good")
  public void test_GetGood_001() throws IOException {
    goodcontrollerClient.getGood("not_exist_goodid", testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "获取存在的Good")
  public void test_GetGood_002() throws IOException {
    Good goodOne = testActions.getGoodOne();
    Good good = goodcontrollerClient.getGood(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.assertEquals(goodOne.getId(), good.getId());
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "获取废弃的Good")
  public void test_GetGood_003() throws IOException {
    Good goodOne = testActions.getGoodOne();
    Good good = goodcontrollerClient.getGood(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.assertEquals(goodOne.getId(), good.getId());
    testActions.deleteGoodOne();
    goodcontrollerClient.getGood(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "update不存在的Good")
  public void test_UpdateGood_001() throws IOException {
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood("not_exist_goodid", goodRequest, testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "update废弃的Good")
  public void test_UpdateGood_002() throws IOException {
    Good goodOne = testActions.getGoodOne();
    testActions.deleteGoodOne();
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood(goodOne.getId(), goodRequest, testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "update Good")
  public void test_UpdateGood_003() throws IOException {
    Good goodOne = testActions.getGoodOne();
    Good goodRequest = new Good();
    goodRequest.setName("good one rename");
    goodcontrollerClient.updateGood(goodOne.getId(), goodRequest, testActions.getAdminAccessToken());
    Good good = goodcontrollerClient.getGood(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.assertEquals("good one rename", good.getName());
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "不存在的gooddetail")
  public void test_GetGoodDetail_001() throws IOException {
    goodcontrollerClient.getGoodDetail("not_exist_goodid", testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "废弃的gooddetail")
  public void test_GetGoodDetail_002() throws IOException {
    Good goodOne = testActions.getGoodOne();
    testActions.deleteGoodOne();
    goodcontrollerClient.getGoodDetail(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "gooddetail")
  public void test_GetGoodDetail_003() throws IOException {
    Good goodOne = testActions.getGoodOne();
    GoodDetail goodDetail = goodcontrollerClient.getGoodDetail(goodOne.getId(), testActions.getAdminAccessToken());
    Assert.assertNotNull(goodDetail);
    Assert.assertEquals(goodOne.getId(), goodDetail.getId());
  }
}
