package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.server.client.api.impl.AuthControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.user.TokenResponse;
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
 * AuthController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-20 21:38:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class AuthControllerTest {
  @Autowired
  private UserTestActions testActions;

  @Autowired
  private AuthControllerClient client;

  @Before
  public void before() throws IOException {
    testActions.clearData();
  }

  @After
  public void after() {
    testActions.clearData();
  }

  @Test
  @TestDescription(description = "")
  public void test_RefreshToken_001() throws IOException {
    testActions.createSenderOneUser();
    String token = testActions.getSenderOneToken();
    TokenResponse response = client.refreshToken(token);
    Assert.assertNotNull(response);
    Assert.assertTrue(StringUtils.hasContent(response.getToken()));
  }
}
