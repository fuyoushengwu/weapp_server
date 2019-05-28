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

import cn.aijiamuyingfang.client.domain.exception.UserException;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.rest.api.impl.UserControllerClient;
import cn.aijiamuyingfang.commons.annotation.UseCaseDescription;
import cn.aijiamuyingfang.server.it.ITApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class UserControllerTest {

  @Autowired
  private UserTestActions testActions;

  @Autowired
  private UserControllerClient userControllerClient;

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

  @Test(expected = UserException.class)
  @UseCaseDescription(description = "获取不存在的用户")
  public void test_GetUser_001() throws IOException {
    userControllerClient.getUser("not_exist_user", testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "获取存在的用户")
  public void test_GetUser_002() throws IOException {
    User user = userControllerClient.getUser(UserTestActions.ADMIN_USER_ID, testActions.getAdminAccessToken());
    Assert.assertNotNull(user);
    Assert.assertEquals(UserTestActions.ADMIN_USER_ID, user.getId());
  }

  @Test
  @UseCaseDescription(description = "更新用户")
  public void test_UpdateUser_001() throws IOException {
    User senderOne = testActions.getSenderOne();
    User user = userControllerClient.getUser(senderOne.getId(), testActions.getSenderOneAccessToken());
    Assert.assertNotNull(user);
    Assert.assertEquals("11111111111", user.getPhone());

    User updateUserRequest = new User();
    updateUserRequest.setPhone("88888888");
    user = userControllerClient.updateUser(senderOne.getId(), updateUserRequest, testActions.getSenderOneAccessToken());
    Assert.assertEquals("88888888", user.getPhone());

  }

  @Test
  @UseCaseDescription(description = "用户没有收件地址")
  public void test_GetUserRecieveAddressList_001() throws IOException {
    User senderOne = testActions.getSenderOne();
    List<RecieveAddress> addressList = userControllerClient.getUserRecieveAddressList(senderOne.getId(),
        testActions.getSenderOneAccessToken());
    Assert.assertNotNull(addressList);
    Assert.assertEquals(0, addressList.size());
  }

  @Test
  @UseCaseDescription(description = "用户有收件地址")
  public void test_GetUserRecieveAddressList_002() throws IOException {
    User senderOne = testActions.getSenderOne();
    RecieveAddress recieveAddressOne = testActions.getSenderOneRecieveOne();
    Assert.assertNotNull(recieveAddressOne);

    List<RecieveAddress> addressList = userControllerClient.getUserRecieveAddressList(senderOne.getId(),
        testActions.getSenderOneAccessToken());
    Assert.assertNotNull(addressList);
    Assert.assertEquals(1, addressList.size());
    Assert.assertEquals(recieveAddressOne.getId(), addressList.get(0).getId());

    RecieveAddress recieveaddress = userControllerClient.getRecieveAddress(senderOne.getId(), recieveAddressOne.getId(),
        testActions.getSenderOneAccessToken());
    Assert.assertNotNull(recieveaddress);
    Assert.assertEquals(recieveAddressOne.getId(), recieveaddress.getId());

    RecieveAddress updateRecieveAddressRequest = new RecieveAddress();
    updateRecieveAddressRequest.setReciever("SSSSSSSS");
    userControllerClient.updateRecieveAddress(senderOne.getId(), recieveAddressOne.getId(), updateRecieveAddressRequest,
        testActions.getSenderOneAccessToken());

    recieveaddress = userControllerClient.getRecieveAddress(senderOne.getId(), recieveAddressOne.getId(),
        testActions.getSenderOneAccessToken());
    Assert.assertEquals("SSSSSSSS", recieveaddress.getReciever());

    userControllerClient.deprecateRecieveAddress(senderOne.getId(), recieveAddressOne.getId(),
        testActions.getSenderOneAccessToken(), false);

    addressList = userControllerClient.getUserRecieveAddressList(senderOne.getId(),
        testActions.getSenderOneAccessToken());
    Assert.assertEquals(0, addressList.size());
  }
}
