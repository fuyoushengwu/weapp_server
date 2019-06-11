package cn.aijiamuyingfang.server.it.utils;

import java.io.IOException;

import cn.aijiamuyingfang.client.commons.domain.ResponseCode;
import cn.aijiamuyingfang.client.commons.exception.OAuthException;
import cn.aijiamuyingfang.client.oauth2.Constants;
import cn.aijiamuyingfang.client.oauth2.OAuth2Client;
import cn.aijiamuyingfang.client.oauth2.OAuthResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

/**
 * [描述]:
 * <p>
 * 测试工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-11 18:03:51
 */
@Slf4j
@UtilityClass
public class TestUtils {
  private static final OkHttpClient okhttpClient = new OkHttpClient();

  /**
   * 获取AccessToken
   * 
   * @param oauthHost
   * @param clientId
   * @param clientSecret
   * @param username
   * @param password
   * @return
   * @throws IOException
   */
  public static OAuthResponse getAccessToken(String oauthHost, String clientId, String clientSecret, String username,
      String password) throws IOException {
    OAuth2Client.Builder builder = new OAuth2Client.Builder(clientId, clientSecret, oauthHost + "oauth/token")
        .grantType(Constants.GRANT_TYPE_PASSWORD).scope("read,write").username(username).password(password)
        .okHttpClient(okhttpClient);
    OAuth2Client client = builder.build();
    OAuthResponse response = client.requestAccessToken();
    if (!response.isSuccessful()) {
      log.error("get  access token failed", response.getOAuthError());
      throw new OAuthException(ResponseCode.BAD_REQUEST, response.getOAuthError().getDescirption());
    }
    return response;
  }

  /**
   * 刷新AccessToken
   * 
   * @param oauthHost
   * @param refreshToken
   * @return
   * @throws IOException
   */
  public static OAuthResponse refreshToken(String oauthHost, String clientId, String clientSecret, String username,
      String password, String refreshToken) throws IOException {
    OAuth2Client.Builder builder = new OAuth2Client.Builder(clientId, clientSecret, oauthHost + "oauth/token")
        .grantType(Constants.GRANT_TYPE_REFRESH).scope("read,write")// .username(username).password(password)
        .okHttpClient(okhttpClient);
    OAuth2Client client = builder.build();
    OAuthResponse response = client.refreshAccessToken(refreshToken);
    if (!response.isSuccessful()) {
      log.error("refresh access token failed", response.getOAuthError());
      throw new OAuthException(ResponseCode.BAD_REQUEST, response.getOAuthError().getDescirption());
    }
    return response;
  }
}
