package cn.aijiamuyingfang.server.it.user.controller;

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aijiamuyingfang.client.domain.message.UserMessage;
import cn.aijiamuyingfang.client.domain.message.UserMessageType;
import cn.aijiamuyingfang.client.domain.message.response.GetMessagesListResponse;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.rest.api.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.ITApplication;

/***
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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class UserMessageControllerTest {

  @Autowired
  private UserTestActions testActions;

  @Autowired
  private UserMessageControllerClient client;

  @Before
  public void before() throws IOException {
    testActions.clearUserMessage();
    testActions.clearUser();
    testActions.clearRecieveAddress();
  }

  @After
  public void after() {
    testActions.clearUserMessage();
    testActions.clearUser();
    testActions.clearRecieveAddress();
  }

  @Test
  @UseCaseDescription(description = "用户没有消息")
  public void test_GetUserUnReadMessageCount_001() throws IOException {
    User senderOne = testActions.getSenderOne();
    int count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, count);
  }

  @Test
  @UseCaseDescription(description = "用户有消息")
  public void test_GetUserUnReadMessageCount_002() throws IOException {
    User senderOne = testActions.getSenderOne();
    int count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, count);

    UserMessage userMessage = testActions.getSenderOneMessage();
    count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, count);

    GetMessagesListResponse userMessageList = client.getUserMessageList(senderOne.getId(), 1, 10,
        testActions.getSenderOneAccessToken());
    Assert.assertNotNull(userMessageList);
    Assert.assertEquals(1, userMessageList.getDataList().size());
    Assert.assertEquals(userMessage.getId(), userMessageList.getDataList().get(0).getId());

    testActions.deleteSenderOneMessage();

    count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, count);

    userMessageList = client.getUserMessageList(senderOne.getId(), 1, 10, testActions.getSenderOneAccessToken());
    Assert.assertNotNull(userMessageList);
    Assert.assertEquals(0, userMessageList.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "用户没有消息,系统有消息")
  public void test_GetUserUnReadMessageCount_003() throws IOException {
    User senderOne = testActions.getSenderOne();
    int count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, count);

    UserMessage systemMessage = new UserMessage();
    systemMessage.setContent("系统消息");
    systemMessage.setCreateTime(new Date());
    systemMessage.setRoundup("系统消息");
    systemMessage.setTitle("系统消息");
    systemMessage.setType(UserMessageType.NOTICE);
    systemMessage.setUserId(UserTestActions.ADMIN_USER_ID);
    client.createMessage(senderOne.getId(), systemMessage, testActions.getSenderOneAccessToken());

    count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(1, count);

    UserMessage userMessage = testActions.getSenderOneMessage();
    count = client.getUserUnReadMessageCount(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertEquals(2, count);

    GetMessagesListResponse response = client.getUserMessageList(senderOne.getId(), 1, 10,
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(userMessage.getId(), response.getDataList().get(1).getId());
  }

}
