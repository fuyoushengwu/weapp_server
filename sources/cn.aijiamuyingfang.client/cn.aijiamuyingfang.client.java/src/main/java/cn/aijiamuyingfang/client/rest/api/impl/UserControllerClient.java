package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.UserControllerApi;
import cn.aijiamuyingfang.client.rest.utils.ResponseUtils;
import cn.aijiamuyingfang.vo.exception.OAuthException;
import cn.aijiamuyingfang.vo.exception.UserException;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.utils.JsonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用 UserController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-11 01:32:07
 */
@Service
@SuppressWarnings("rawtypes")
public class UserControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(UserControllerClient.class);

  private static final Callback<ResponseBean> Empty_Callback = new Callback<ResponseBean>() {

    @Override
    public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
      LOGGER.info("onResponse:{}", response.message());
    }

    @Override
    public void onFailure(Call<ResponseBean> call, Throwable t) {
      LOGGER.error(t.getMessage(), t);
    }
  };

  @HttpService
  private UserControllerApi userControllerApi;

  /**
   * 获取用户
   * 
   * @param username
   * @param accessToken
   * @return
   * @throws IOException
   */
  public User getUser(String username, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.getUser(username, accessToken).execute();
    return getUserFromResponse(response, "get user  return code is '200',but return data is null");
  }

  /**
   * 获取用户
   * 
   * @param username
   * @param accessToken
   * @return
   * @throws IOException
   */
  public String getUserPhone(String username, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.getUserPhone(username, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return (String) responseBean.getData();
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 注册用户
   * 
   * @param user
   * @param accessToken
   * @return
   * @throws IOException
   */
  public User registerUser(User user, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.registerUser(user, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new OAuthException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      User userResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), User.class);
      if (null == userResponse) {
        throw new OAuthException("500", "register user return code is '200',but return data is null");
      }
      return userResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new OAuthException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步注册用户
   * 
   * @param user
   * @param accessToken
   * @param callback
   */
  public void registerUserAsync(User user, String accessToken, Callback<ResponseBean> callback) {
    userControllerApi.registerUser(user, accessToken).enqueue(callback);
  }

  /**
   * 更新用户信息
   * 
   * @param username
   * @param user
   * @param accessToken
   * @return
   * @throws IOException
   */
  public User updateUser(String username, User user, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.updateUser(username, user, accessToken).execute();
    return getUserFromResponse(response, "update user  return code is '200',but return data is null");
  }

  /**
   * 从response中获取User
   * 
   * @param response
   * @param exceptionmsg
   * @return
   * @throws IOException
   */
  private User getUserFromResponse(Response<ResponseBean> response, String exceptionmsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      User user = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), User.class);
      if (null == user) {
        throw new UserException("500", exceptionmsg);
      }
      return user;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步更新用户信息
   * 
   * @param username
   * @param user
   * @param accessToken
   * @param callback
   */
  public void updateUserAsync(String username, User user, String accessToken, Callback<ResponseBean> callback) {
    userControllerApi.updateUser(username, user, accessToken).enqueue(callback);
  }

  /**
   * 获取用户收件地址
   * 
   * @param username
   * @param accessToken
   * @return
   * @throws IOException
   */
  public List<RecieveAddress> getUserRecieveAddressList(String username, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.getUserRecieveAddressList(username, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      List<RecieveAddress> recieveaddressList = JsonUtils.json2List(JsonUtils.list2Json((List<?>) returnData),
          RecieveAddress.class);
      if (null == recieveaddressList) {
        throw new UserException("500", "get user  recieve address list return code is '200',but return data is null");
      }
      return recieveaddressList;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 给用户添加收件地址
   * 
   * @param username
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public RecieveAddress addUserRecieveAddress(String username, RecieveAddress request, String accessToken)
      throws IOException {
    Response<ResponseBean> response = userControllerApi.addUserRecieveAddress(username, request, accessToken).execute();
    return getRecieveAddressFromResponse(response,
        "add user recieve address  return code is '200',but return data is null");
  }

  /**
   * 异步给用户添加收件地址
   * 
   * @param username
   * @param request
   * @param accessToken
   * @param callback
   */
  public void addUserRecieveAddressAsync(String username, RecieveAddress request, String accessToken,
      Callback<ResponseBean> callback) {
    userControllerApi.addUserRecieveAddress(username, request, accessToken).enqueue(callback);
  }

  /**
   * 获取收件地址
   * 
   * @param username
   * @param addressId
   * @param accessToken
   * @return
   * @throws IOException
   */
  public RecieveAddress getRecieveAddress(String username, String addressId, String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.getRecieveAddress(username, addressId, accessToken).execute();
    return getRecieveAddressFromResponse(response, "get recieve address  return code is '200',but return data is null");
  }

  /**
   * 更新收件地址信息
   * 
   * @param username
   * @param addressId
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public RecieveAddress updateRecieveAddress(String username, String addressId, RecieveAddress request,
      String accessToken) throws IOException {
    Response<ResponseBean> response = userControllerApi.updateRecieveAddress(username, addressId, request, accessToken)
        .execute();
    return getRecieveAddressFromResponse(response,
        "update recieve address  return code is '200',but return data is null");
  }

  /**
   * 从response中获取RecieveAddress
   * 
   * @param response
   * @param exceptionmsg
   * @return
   * @throws IOException
   */
  private RecieveAddress getRecieveAddressFromResponse(Response<ResponseBean> response, String exceptionmsg)
      throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      RecieveAddress recieveaddress = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          RecieveAddress.class);
      if (null == recieveaddress) {
        throw new UserException("500", exceptionmsg);
      }
      return recieveaddress;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());

  }

  /**
   * 异步更新收件地址信息
   * 
   * @param username
   * @param addressId
   * @param request
   * @param accessToken
   * @param callback
   */
  public void updateRecieveAddressAsync(String username, String addressId, RecieveAddress request, String accessToken,
      Callback<ResponseBean> callback) {
    userControllerApi.updateRecieveAddress(username, addressId, request, accessToken).enqueue(callback);
  }

  /**
   * 废弃收件地址
   * 
   * @param username
   * @param addressId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deprecateRecieveAddress(String username, String addressId, String accessToken, boolean async)
      throws IOException {
    if (async) {
      userControllerApi.deprecateRecieveAddress(username, addressId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleUserVOIDResponse(
        userControllerApi.deprecateRecieveAddress(username, addressId, accessToken).execute(), LOGGER);
  }

}
