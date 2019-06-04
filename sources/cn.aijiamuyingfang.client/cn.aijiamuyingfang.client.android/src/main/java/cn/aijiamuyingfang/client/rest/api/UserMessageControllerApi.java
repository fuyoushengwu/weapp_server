package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.message.UserMessage;
import cn.aijiamuyingfang.client.domain.message.response.GetMessagesListResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * UserMessageController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 16:13:34
 */
@HttpApi(baseurl = "weapp.host_name")
public interface UserMessageControllerApi {
  /**
   * 获得用户未读消息数量
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/message/unread/count")
  public Observable<ResponseBean<Integer>> getUserUnReadMessageCount(@Path("user_id") String userId,
      @Query("access_token") String accessToken);

  /**
   * 分页获取用户消息
   * 
   * @param userId
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/message")
  public Observable<ResponseBean<GetMessagesListResponse>> getUserMessageList(@Path("user_id") String userId,
      @Query("current_page") int currentPage, @Query("page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 为用户创建消息
   * 
   * @param userId
   * @param message
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{user_id}/message")
  public Observable<ResponseBean<UserMessage>> createMessage(@Path("user_id") String userId, @Body UserMessage message,
      @Query("access_token") String accessToken);

  /**
   * 删除消息
   * 
   * @param userId
   * @param messageId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{user_id}/message/{message_id}")
  public Observable<ResponseBean<Void>> deleteMessage(@Path("user_id") String userId,
      @Path("message_id") String messageId, @Query("access_token") String accessToken);
}
