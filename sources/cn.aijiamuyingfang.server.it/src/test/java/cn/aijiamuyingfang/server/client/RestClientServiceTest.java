package cn.aijiamuyingfang.server.client;

import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * [描述]:
 * <p>
 * RestClientService集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:10:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = { GoodsApplication.class })
public class RestClientServiceTest {

  @Test
  @TestDescription
  public void testSendPreOrderUpdateMsg_001() {
    System.out.println("XXX");
  }

  @Test
  @TestDescription
  public void testSendPickupMsg_001() {
  }

  @Test
  @TestDescription
  public void testSendThirdSendMsg_001() {
  }

  @Test
  @TestDescription
  public void testSendOwnSendMsg_001() {
  }

  @Test
  @TestDescription
  public void testUpdatePreOrder_001() {
  }

  @Test
  @TestDescription
  public void testJSCode2Session_001() {

  }

  @Test
  @TestDescription
  public void testDeleteShopCartGood_001() {
  }

}
