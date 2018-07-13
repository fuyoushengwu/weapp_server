package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.server.client.itapi.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.domain.exception.GoodsException;
import cn.aijiamuyingfang.server.domain.goods.Classify;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class ClassifyControllerTest {

  @Autowired
  private GoodsTestActions goodsTestActions;

  @Autowired
  private ClassifyControllerClient classifyControllerClient;

  @Before
  public void before() throws IOException {
    goodsTestActions.clearData();
  }

  @After
  public void after() {
    goodsTestActions.clearData();
  }

  @Test
  @TestDescription(description = "当门店中没有条目数据时查询")
  public void testGetStoreTopClassifyList_001() throws IOException {
    Store store = goodsTestActions.createStoreOne();
    Assert.assertNotNull(store);
    Assert.assertEquals(0, store.getClassifyList().size());
  }

  @Test
  @TestDescription(description = "当有一条顶级的条目数据但不属于门店时查询")
  public void testGetStoreTopClassifyList_002() throws IOException {
    goodsTestActions.createStoreOne();
    goodsTestActions.createClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getStoreTopClassifyList(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.storeoneId);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test
  @TestDescription(description = "当门店中有一条顶级的条目数据时查询")
  public void testGetStoreTopClassifyList_003() throws IOException {
    goodsTestActions.createStoreOne();
    goodsTestActions.createClassifyOne();
    goodsTestActions.applyClassifyOneForStoreOne();
    List<Classify> classifyList = classifyControllerClient.getStoreTopClassifyList(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.storeoneId);
    Assert.assertEquals(1, classifyList.size());
    goodsTestActions.deleteClassifyOne();
    classifyList = classifyControllerClient.getStoreTopClassifyList(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.storeoneId);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "获取不存在的Classify")
  public void testGetClassify_001() throws IOException {
    classifyControllerClient.getClassify(GoodsTestActions.ADMIN_USER_TOKEN, "not_exist_classify");
  }

  @Test
  @TestDescription(description = "获取存在的Classify")
  public void testGetClassify_002() throws IOException {
    goodsTestActions.createClassifyOne();
    Classify classify = classifyControllerClient.getClassify(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.classifyoneId);
    Assert.assertNotNull(classify);
    Assert.assertEquals(goodsTestActions.classifyoneId, classify.getId());
  }

  @Test
  @TestDescription(description = "获取子条目(当没有子条目时)")
  public void test_GetSubClassifyList_001() throws IOException {
    goodsTestActions.createClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.classifyoneId);
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test
  @TestDescription(description = "获取子条目(当有子条目时)")
  public void test_GetSubClassifyList_002() throws IOException {
    goodsTestActions.createClassifyOne();
    goodsTestActions.createSubClassifyOneForClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.classifyoneId);
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(1, classifyList.size());
  }
}
