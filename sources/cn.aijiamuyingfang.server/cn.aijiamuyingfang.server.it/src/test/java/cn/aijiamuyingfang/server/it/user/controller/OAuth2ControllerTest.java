package cn.aijiamuyingfang.server.it.user.controller;

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

import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.rest.api.impl.UserControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.it.ITApplication;

/***
 * [描述]:*
 * <p>
 * *AuthController的集成测试类*
 * </p>
 * **
 * 
 * @version 1.0.0*@author ShiWei*@email shiweideyouxiang @sina.cn
 * @date 2018-07-20 21:38:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class OAuth2ControllerTest {
  @Autowired
  private UserTestActions testActions;

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
  @UseCaseDescription(description = "")
  public void test_RefreshToken_001() throws IOException {
    User senderOne = testActions.getSenderOne();
    String senderOneAccessToken = testActions.getSenderOneAccessToken();
    checkSenderOneAccessToken(senderOne, senderOneAccessToken);

    String newSenderOneAccessToken = testActions.refreshSenderOneAccessToken();
    Assert.assertTrue(StringUtils.hasContent(newSenderOneAccessToken));
    Assert.assertNotEquals(senderOneAccessToken, newSenderOneAccessToken);
    checkSenderOneAccessToken(senderOne, newSenderOneAccessToken);
  }

  @Autowired
  private UserControllerClient userControllerClient;

  private void checkSenderOneAccessToken(User senderOne, String accessToken) throws IOException {
    List<RecieveAddress> addressList = userControllerClient.getUserRecieveAddressList(senderOne.getId(), accessToken);
    Assert.assertNotNull(addressList);
    Assert.assertEquals(0, addressList.size());
  }
}
