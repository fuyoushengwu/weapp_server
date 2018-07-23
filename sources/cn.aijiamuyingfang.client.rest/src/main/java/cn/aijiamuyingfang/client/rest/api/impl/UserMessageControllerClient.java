package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.exception.UserException;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.response.GetMessagesListResponse;
import java.io.IOException;
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
  private UserMessageControllerApi usermessageControllerApi;

  /**
   * 获得用户未读消息数量
   * 
   * @param token
   * @param userid
   * @return
   * @throws IOException
   */
  public int getUserUnReadMessageCount(String token, String userid) throws IOException {
    Response<ResponseBean> response = usermessageControllerApi.getUserUnReadMessageCount(token, userid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetMessagesListResponse getUserMessageList(String token, String userid, int currentpage, int pagesize)
      throws IOException {
    Response<ResponseBean> response = usermessageControllerApi.getUserMessageList(token, userid, currentpage, pagesize)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param userid
   * @param message
   * @return
   * @throws IOException
   */
  public UserMessage createMessage(String token, String userid, UserMessage message) throws IOException {
    Response<ResponseBean> response = usermessageControllerApi.createMessage(token, userid, message).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      UserMessage usermessage = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), UserMessage.class);
      if (null == usermessage) {
        throw new UserException("500", "create message  return code is '200',but return data is null");
      }
      return usermessage;
    }
    LOGGER.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步为用户创建消息
   * 
   * @param token
   * @param userid
   * @param message
   * @param callback
   */
  public void createMessageAsync(String token, String userid, UserMessage message, Callback<ResponseBean> callback) {
    usermessageControllerApi.createMessage(token, userid, message).enqueue(callback);
  }

  /**
   * 删除消息
   * 
   * @param token
   * @param userid
   * @param messageid
   * @param async
   * @throws IOException
   */
  public void deleteMessage(String token, String userid, String messageid, boolean async) throws IOException {
    if (async) {
      usermessageControllerApi.deleteMessage(token, userid, messageid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = usermessageControllerApi.deleteMessage(token, userid, messageid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
