package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.client.rest.utils.ResponseUtils;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.exception.UserException;
import cn.aijiamuyingfang.vo.message.PagableUserMessageList;
import cn.aijiamuyingfang.vo.message.UserMessage;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.JsonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用UserMessageController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-20 20:31:04
 */
@Service
@SuppressWarnings("rawtypes")
public class UserMessageControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(UserMessageControllerClient.class);

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
  private UserMessageControllerApi userMessageControllerApi;

  /**
   * 获得用户未读消息数量
   * 
   * @param username
   * @param accessToken
   * @return
   * @throws IOException
   */
  public int getUserUnReadMessageCount(String username, String accessToken) throws IOException {
    Response<
        ResponseBean> response = userMessageControllerApi.getUserUnReadMessageCount(username, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      return Double.valueOf(returnData.toString()).intValue();
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取用户消息
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagableUserMessageList getUserMessageList(String username, int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = userMessageControllerApi
        .getUserMessageList(username, currentPage, pageSize, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableUserMessageList getMessagesListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableUserMessageList.class);
      if (null == getMessagesListResponse) {
        throw new UserException("500", "get user message list  return code is '200',but return data is null");
      }
      return getMessagesListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 为用户创建消息
   * 
   * @param username
   * @param message
   * @param accessToken
   * @return
   * @throws IOException
   */
  public UserMessage createMessage(String username, UserMessage message, String accessToken) throws IOException {
    Response<ResponseBean> response = userMessageControllerApi.createMessage(username, message, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      UserMessage userMessage = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), UserMessage.class);
      if (null == userMessage) {
        throw new UserException("500", "create message  return code is '200',but return data is null");
      }
      return userMessage;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步为用户创建消息
   * 
   * @param username
   * @param message
   * @param accessToken
   * @param callback
   */
  public void createMessageAsync(String username, UserMessage message, String accessToken,
      Callback<ResponseBean> callback) {
    userMessageControllerApi.createMessage(username, message, accessToken).enqueue(callback);
  }

  /**
   * 删除消息
   * 
   * @param username
   * @param messageId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteMessage(String username, String messageId, String accessToken, boolean async) throws IOException {
    if (async) {
      userMessageControllerApi.deleteMessage(username, messageId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleGoodsVOIDResponse(
        userMessageControllerApi.deleteMessage(username, messageId, accessToken).execute(), LOGGER);
  }
}
