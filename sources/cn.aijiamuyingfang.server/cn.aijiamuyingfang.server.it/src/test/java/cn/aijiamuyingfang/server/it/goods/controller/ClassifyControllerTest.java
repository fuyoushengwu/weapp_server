package cn.aijiamuyingfang.server.it.goods.controller;

import java.io.IOException;
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

import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.domain.exception.GoodsException;
import cn.aijiamuyingfang.client.rest.api.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.ITApplication;

/**
 * [描述]:
 * <p>
 * ClassifyController集成测试类
 * </p>
 *
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:08:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class ClassifyControllerTest {

  @Autowired
  private GoodsTestActions testActions;

  @Autowired
  private ClassifyControllerClient classifyControllerClient;

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

  @Test
  @UseCaseDescription(description = "当门店中有一条顶级的条目数据时查询")
  public void testGetStoreTopClassifyList_003() throws IOException {
    testActions.getStoreOne();
    testActions.getClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getTopClassifyList(testActions.getAdminAccessToken());
    Assert.assertEquals(1, classifyList.size());
    classifyList = classifyControllerClient.getTopClassifyList(testActions.getAdminAccessToken());
    Assert.assertEquals(1, classifyList.size());
    testActions.deleteClassifyOne();
    classifyList = classifyControllerClient.getTopClassifyList(testActions.getAdminAccessToken());
    Assert.assertEquals(0, classifyList.size());
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "获取不存在的Classify")
  public void testGetClassify_001() throws IOException {
    classifyControllerClient.getClassify("not_exist_classify", testActions.getAdminAccessToken());
  }

  @Test
  @UseCaseDescription(description = "获取存在的Classify")
  public void testGetClassify_002() throws IOException {
    Classify classifyOne = testActions.getClassifyOne();
    Assert.assertNotNull(classifyOne);
    Classify actualClassify = classifyControllerClient.getClassify(classifyOne.getId(),
        testActions.getAdminAccessToken());
    Assert.assertNotNull(actualClassify);
    Assert.assertEquals(actualClassify.getId(), classifyOne.getId());
  }

  @Test
  @UseCaseDescription(description = "获取子条目(当没有子条目时)")
  public void test_GetSubClassifyList_001() throws IOException {
    Classify classifyOne = testActions.getClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(classifyOne.getId(),
        testActions.getAdminAccessToken());
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test
  @UseCaseDescription(description = "获取子条目(当有子条目时)")
  public void test_GetSubClassifyList_002() throws IOException {
    Classify classifyOne = testActions.getClassifyOne();
    Classify subClassifyOne = testActions.getSubClassifyOneForClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(classifyOne.getId(),
        testActions.getAdminAccessToken());
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(1, classifyList.size());
    Assert.assertEquals(subClassifyOne.getId(), classifyList.get(0).getId());
  }
}
