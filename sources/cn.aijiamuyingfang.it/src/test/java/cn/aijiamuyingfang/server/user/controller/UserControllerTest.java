package cn.aijiamuyingfang.server.user.controller;

import static cn.aijiamuyingfang.server.client.AbstractTestAction.ADMIN_USER_TOKEN;

import cn.aijiamuyingfang.client.rest.api.impl.UserControllerClient;
import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.exception.UserException;
import cn.aijiamuyingfang.commons.domain.user.Gender;
import cn.aijiamuyingfang.commons.domain.user.User;
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
 * UserController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 11:12:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class UserControllerTest {
  @Autowired
  private UserTestActions testActions;

  @Autowired
  private UserControllerClient client;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test(expected = UserException.class)
  @TestDescription(description = "获取不存在的用户")
  public void test_GetUser_001() throws IOException {
    client.getUser(ADMIN_USER_TOKEN, "not_exist_user");
    Assert.fail();
  }

  @Test
  @TestDescription(description = "获取存在的用户")
  public void test_GetUser_002() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    User user = client.getUser(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertNotNull(user);
    Assert.assertEquals(testActions.senderoneId, user.getId());
    Assert.assertEquals(Gender.MALE, user.getGender());
  }

  @Test
  @TestDescription(description = "更新用户")
  public void test_UpdateUser_001() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    User user = client.getUser(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertNotNull(user);
    Assert.assertEquals("11111111111", user.getPhone());

    User request = new User();
    request.setPhone("88888888");
    user = client.updateUser(testActions.senderoneToken, testActions.senderoneId, request);
    Assert.assertEquals("88888888", user.getPhone());
  }

  @Test
  @TestDescription(description = "用户没有收件地址")
  public void test_GetUserRecieveAddressList_001() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    List<RecieveAddress> addressList = client.getUserRecieveAddressList(testActions.senderoneToken,
        testActions.senderoneId);
    Assert.assertNotNull(addressList);
    Assert.assertEquals(0, addressList.size());
  }

  @Test
  @TestDescription(description = "用户有收件地址")
  public void test_GetUserRecieveAddressList_002() throws IOException {
    testActions.createSenderOneUser();
    testActions.getSenderOneToken();
    testActions.addSenderOneRecieveOne();
    List<RecieveAddress> addressList = client.getUserRecieveAddressList(testActions.senderoneToken,
        testActions.senderoneId);
    Assert.assertNotNull(addressList);
    Assert.assertEquals(1, addressList.size());
    Assert.assertEquals(testActions.senderoneRecieveAddressOneId, addressList.get(0).getId());
    RecieveAddress recieveaddress = client.getRecieveAddress(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneRecieveAddressOneId);
    Assert.assertNotNull(recieveaddress);
    Assert.assertEquals(recieveaddress.getId(), addressList.get(0).getId());
    RecieveAddress request = new RecieveAddress();
    request.setReciever("SSSSSSSS");
    client.updateRecieveAddress(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneRecieveAddressOneId, request);

    recieveaddress = client.getRecieveAddress(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneRecieveAddressOneId);
    Assert.assertEquals("SSSSSSSS", recieveaddress.getReciever());

    client.deprecateRecieveAddress(testActions.senderoneToken, testActions.senderoneId,
        testActions.senderoneRecieveAddressOneId, false);
    addressList = client.getUserRecieveAddressList(testActions.senderoneToken, testActions.senderoneId);
    Assert.assertEquals(0, addressList.size());
  }

}
