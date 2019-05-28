package ca.mimic.oauth2library;

import org.junit.Assert;
import org.junit.Test;

import cn.aijiamuyingfang.client.oauth2.OAuth2Client;
import cn.aijiamuyingfang.client.oauth2.OAuthResponse;
import okhttp3.OkHttpClient;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OAuth2ClientTest {
  @Test
  public void additionIsCorrect() throws Exception {
    OkHttpClient okHttpClient = new OkHttpClient();
    OAuth2Client.Builder builder = new OAuth2Client.Builder("weapp-manager", "weapp-manager",
        "http://192.168.0.201:8080/oauth/token").grantType("password").scope("read,write")
            .username("5c6a132a829f11e896fc00cfe0430e2a").password("admin").okHttpClient(okHttpClient);
    OAuth2Client client = builder.build();
    OAuthResponse response = client.requestAccessToken();
    Assert.assertTrue(response.isSuccessful());
    Assert.assertNotNull(response.getAccessToken());
    Assert.assertNotNull(response.getRefreshToken());

  }

}