package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.exception.GoodsException;
import cn.aijiamuyingfang.client.domain.exception.UserException;
import cn.aijiamuyingfang.client.domain.message.UserMessage;
import cn.aijiamuyingfang.client.domain.message.response.GetMessagesListResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
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
      LOGGER.info("onResponse:" + response.message());
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
   * @param userId
   * @param accessToken
   * @return
   * @throws IOException
   */
  public int getUserUnReadMessageCount(String userId, String accessToken) throws IOException {
    Response<ResponseBean> response = userMessageControllerApi.getUserUnReadMessageCount(userId, accessToken).execute();
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
   * @param userId
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public GetMessagesListResponse getUserMessageList(String userId, int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = userMessageControllerApi
        .getUserMessageList(userId, currentPage, pageSize, accessToken).execute();
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
      GetMessagesListResponse getMessagesListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          GetMessagesListResponse.class);
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
   * @param userId
   * @param message
   * @param accessToken
   * @return
   * @throws IOException
   */
  public UserMessage createMessage(String userId, UserMessage message, String accessToken) throws IOException {
    Response<ResponseBean> response = userMessageControllerApi.createMessage(userId, message, accessToken).execute();
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
   * @param userId
   * @param message
   * @param accessToken
   * @param callback
   */
  public void createMessageAsync(String userId, UserMessage message, String accessToken,
      Callback<ResponseBean> callback) {
    userMessageControllerApi.createMessage(userId, message, accessToken).enqueue(callback);
  }

  /**
   * 删除消息
   * 
   * @param userId
   * @param messageId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteMessage(String userId, String messageId, String accessToken, boolean async) throws IOException {
    if (async) {
      userMessageControllerApi.deleteMessage(userId, messageId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = userMessageControllerApi.deleteMessage(userId, messageId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }
}
