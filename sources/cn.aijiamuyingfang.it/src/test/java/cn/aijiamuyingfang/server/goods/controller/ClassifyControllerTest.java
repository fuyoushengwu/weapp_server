package cn.aijiamuyingfang.server.goods.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
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
  private GoodsTestActions testActions;

  @Autowired
  private ClassifyControllerClient classifyControllerClient;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test
  @TestDescription(description = "当门店中有一条顶级的条目数据时查询")
  public void testGetStoreTopClassifyList_003() throws IOException {
    testActions.createStoreOne();
    testActions.createClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getTopClassifyList(ADMIN_USER_TOKEN);
    Assert.assertEquals(1, classifyList.size());
    classifyList = classifyControllerClient.getTopClassifyList(ADMIN_USER_TOKEN);
    Assert.assertEquals(1, classifyList.size());
    testActions.deleteClassifyOne();
    classifyList = classifyControllerClient.getTopClassifyList(ADMIN_USER_TOKEN);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "获取不存在的Classify")
  public void testGetClassify_001() throws IOException {
    classifyControllerClient.getClassify(ADMIN_USER_TOKEN, "not_exist_classify");
  }

  @Test
  @TestDescription(description = "获取存在的Classify")
  public void testGetClassify_002() throws IOException {
    testActions.createClassifyOne();
    Classify classify = classifyControllerClient.getClassify(ADMIN_USER_TOKEN, testActions.classifyoneId);
    Assert.assertNotNull(classify);
    Assert.assertEquals(testActions.classifyoneId, classify.getId());
  }

  @Test
  @TestDescription(description = "获取子条目(当没有子条目时)")
  public void test_GetSubClassifyList_001() throws IOException {
    testActions.createClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(ADMIN_USER_TOKEN,
        testActions.classifyoneId);
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(0, classifyList.size());
  }

  @Test
  @TestDescription(description = "获取子条目(当有子条目时)")
  public void test_GetSubClassifyList_002() throws IOException {
    testActions.createClassifyOne();
    testActions.createSubClassifyOneForClassifyOne();
    List<Classify> classifyList = classifyControllerClient.getSubClassifyList(ADMIN_USER_TOKEN,
        testActions.classifyoneId);
    Assert.assertNotNull(classifyList);
    Assert.assertEquals(1, classifyList.size());
  }
}
