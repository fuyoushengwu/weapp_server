package cn.aijiamuyingfang.server.client.api.impl;

import cn.aijiamuyingfang.server.client.annotation.HttpService;
import cn.aijiamuyingfang.server.client.api.AuthControllerApi;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.user.TokenResponse;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.UserRequest;
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
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      TokenResponse tokenResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          TokenResponse.class);
      if (null == tokenResponse) {
        throw new RuntimeException("get token return code is '200',.but return data is null");
      }
      return tokenResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new AuthException(returnCode, responseBean.getMsg());
  }

  /**
   * 注册用户
   * 
   * @param token
   * @param request
   * @return
   * @throws IOException
   */
  public User registerUser(String token, UserRequest request) throws IOException {
    Response<ResponseBean> response = authControllerApi.registerUser(token, request).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      User userResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), User.class);
      if (null == userResponse) {
        throw new RuntimeException("register user return code is '200',.but return data is null");
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
   * @param request
   * @param callback
   */
  public void registerUserAsync(String token, UserRequest request, Callback<ResponseBean> callback) {
    authControllerApi.registerUser(token, request).enqueue(callback);

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
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      TokenResponse tokenResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          TokenResponse.class);
      if (null == tokenResponse) {
        throw new RuntimeException("refresh token return code is '200',.but return data is null");
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
    ;
  }
}
