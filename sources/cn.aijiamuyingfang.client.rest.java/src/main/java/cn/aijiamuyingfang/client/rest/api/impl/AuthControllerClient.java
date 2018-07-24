package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.AuthControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.domain.user.response.TokenResponse;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用AuthController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 17:17:01
 */
@Service
@SuppressWarnings("rawtypes")
public class AuthControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(AuthControllerClient.class);

  @HttpService
  private AuthControllerApi authControllerApi;

  /**
   * 获取JWT
   * 
   * @param jscode
   * @param nickname
   * @param avatar
   * @return
   * @throws IOException
   */
  public TokenResponse getToken(String jscode, String nickname, String avatar) throws IOException {
    Response<ResponseBean> response = authControllerApi.getToken(jscode, nickname, avatar).execute();
    return getTokenFromResponse(response);
  }

  /**
   * 注册用户
   * 
   * @param token
   * @param user
   * @return
   * @throws IOException
   */
  public User registerUser(String token, User user) throws IOException {
    Response<ResponseBean> response = authControllerApi.registerUser(token, user).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new AuthException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      User userResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), User.class);
      if (null == userResponse) {
        throw new AuthException("500", "register user return code is '200',but return data is null");
      }
      return userResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new AuthException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步注册用户
   * 
   * @param token
   * @param user
   * @param callback
   */
  public void registerUserAsync(String token, User user, Callback<ResponseBean> callback) {
    authControllerApi.registerUser(token, user).enqueue(callback);

  }

  /**
   * 刷新用户token
   * 
   * @param token
   * @return
   * @throws IOException
   */
  public TokenResponse refreshToken(String token) throws IOException {
    Response<ResponseBean> response = authControllerApi.refreshToken(token).execute();
    return getTokenFromResponse(response);
  }

  /**
   * 从response中获取Token
   * 
   * @param response
   * @return
   */
  private TokenResponse getTokenFromResponse(Response<ResponseBean> response) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new AuthException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      TokenResponse tokenResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          TokenResponse.class);
      if (null == tokenResponse) {
        throw new AuthException("500", "get token return code is '200',but return data is null");
      }
      return tokenResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new AuthException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步刷新用户token
   * 
   * @param token
   * @param callback
   */
  public void refreshTokenAsync(String token, Callback<ResponseBean> callback) {
    authControllerApi.refreshToken(token).enqueue(callback);
  }
}
