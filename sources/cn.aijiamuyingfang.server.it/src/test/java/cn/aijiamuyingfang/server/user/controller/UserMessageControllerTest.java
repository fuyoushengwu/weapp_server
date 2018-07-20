package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.server.client.itapi.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.domain.user.GetMessagesListResponse;
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
 * UserMessageController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:12:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class UserMessageControllerTest {
  @Autowired
  private UserTestActions testActions;

  @Autowired
  private UserMessageControllerClient client;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test
  @TestDescription(description = "用户没有消息")
  public void test_GetUserUnReadMessageCount_001() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    int count = client.getUserUnReadMessageCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(0, count);
  }

  @Test
  @TestDescription(description = "用户有消息")
  public void test_GetUserUnReadMessageCount_002() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    int count = client.getUserUnReadMessageCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(0, count);

    testActions.createSenderOneMessage();
    count = client.getUserUnReadMessageCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(1, count);

    GetMessagesListResponse response = client.getUserMessageList(testActions.senderoneToken, testActions.senderoneId, 1,
        10);
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals(testActions.senderoneMessageId, response.getDataList().get(0).getId());

    client.deleteMessage(testActions.senderoneToken, testActions.senderoneId, testActions.senderoneMessageId, false);

    count = client.getUserUnReadMessageCount(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(0, count);

    response = client.getUserMessageList(testActions.senderoneToken, testActions.senderoneId, 1, 10);
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());
  }

}
