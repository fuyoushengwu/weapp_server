package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.UserControllerApi;
import cn.aijiamuyingfang.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddressRequest;
import cn.aijiamuyingfang.commons.domain.exception.UserException;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.domain.user.UserRequest;
import cn.aijiamuyingfang.commons.utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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
      LOGGER.info("onResponse:" + response.message());
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
   * @param token
   * @param userid
   * @return
   * @throws IOException
   */
  public User getUser(String token, String userid) throws IOException {
    Response<ResponseBean> response = userControllerApi.getUser(token, userid).execute();
    return getUserFromResponse(response, "get user  return code is '200',but return data is null");
  }

  /**
   * 更新用户信息
   * 
   * @param token
   * @param userid
   * @param request
   * @return
   */
  public User updateUser(String token, String userid, UserRequest request) throws IOException {
    Response<ResponseBean> response = userControllerApi.updateUser(token, userid, request).execute();
    return getUserFromResponse(response, "update user  return code is '200',but return data is null");
  }

  /**
   * 从response中获取User
   * 
   * @param response
   * @param exceptionmsg
   * @return
   */
  private User getUserFromResponse(Response<ResponseBean> response, String exceptionmsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param userid
   * @param request
   * @param callback
   */
  public void updateUserAsync(String token, String userid, UserRequest request, Callback<ResponseBean> callback) {
    userControllerApi.updateUser(token, userid, request).enqueue(callback);
  }

  /**
   * 获取用户收件地址
   * 
   * @param token
   * @param userid
   * @return
   */
  public List<RecieveAddress> getUserRecieveAddressList(String token, String userid) throws IOException {
    Response<ResponseBean> response = userControllerApi.getUserRecieveAddressList(token, userid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param userid
   * @param request
   * @return
   */
  public RecieveAddress addUserRecieveAddress(String token, String userid, RecieveAddressRequest request)
      throws IOException {
    Response<ResponseBean> response = userControllerApi.addUserRecieveAddress(token, userid, request).execute();
    return getRecieveAddressFromResponse(response,
        "add user recieve address  return code is '200',but return data is null");
  }

  /**
   * 异步给用户添加收件地址
   * 
   * @param token
   * @param userid
   * @param request
   * @param callback
   */
  public void addUserRecieveAddressAsync(String token, String userid, RecieveAddressRequest request,
      Callback<ResponseBean> callback) {
    userControllerApi.addUserRecieveAddress(token, userid, request).enqueue(callback);
  }

  /**
   * 获取收件地址
   * 
   * @param token
   * @param userid
   * @param addressid
   * @return
   */
  public RecieveAddress getRecieveAddress(String token, String userid, String addressid) throws IOException {
    Response<ResponseBean> response = userControllerApi.getRecieveAddress(token, userid, addressid).execute();
    return getRecieveAddressFromResponse(response, "get recieve address  return code is '200',but return data is null");
  }

  /**
   * 更新收件地址信息
   * 
   * @param token
   * @param userid
   * @param addressid
   * @param request
   * @return
   */
  public RecieveAddress updateRecieveAddress(String token, String userid, String addressid,
      RecieveAddressRequest request) throws IOException {
    Response<
        ResponseBean> response = userControllerApi.updateRecieveAddress(token, userid, addressid, request).execute();
    return getRecieveAddressFromResponse(response,
        "update recieve address  return code is '200',but return data is null");
  }

  /**
   * 从response中获取RecieveAddress
   * 
   * @param response
   * @param exceptionmsg
   * @return
   */
  private RecieveAddress getRecieveAddressFromResponse(Response<ResponseBean> response, String exceptionmsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param userid
   * @param addressid
   * @param request
   * @param callback
   */
  public void updateRecieveAddressAsync(String token, String userid, String addressid, RecieveAddressRequest request,
      Callback<ResponseBean> callback) {
    userControllerApi.updateRecieveAddress(token, userid, addressid, request).enqueue(callback);
  }

  /**
   * 废弃收件地址
   * 
   * @param token
   * @param userid
   * @param addressid
   * @param async
   * @return
   */
  public void deprecateRecieveAddress(String token, String userid, String addressid, boolean async) throws IOException {
    if (async) {
      userControllerApi.deprecateRecieveAddress(token, userid, addressid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = userControllerApi.deprecateRecieveAddress(token, userid, addressid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

}
