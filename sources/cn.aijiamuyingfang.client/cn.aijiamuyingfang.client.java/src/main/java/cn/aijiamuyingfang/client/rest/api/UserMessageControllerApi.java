package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.message.UserMessage;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import retrofit2.Call;
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
@SuppressWarnings("rawtypes")
public interface UserMessageControllerApi {
  /**
   * 获得用户未读消息数量
   * 
   * @param username
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}/message/unread/count")
  public Call<ResponseBean> getUserUnReadMessageCount(@Path("username") String username,
      @Query("access_token") String accessToken);

  /**
   * 分页获取用户消息
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}/message")
  public Call<ResponseBean> getUserMessageList(@Path("username") String username, @Query("current_page") int currentPage,
      @Query("page_size") int pageSize, @Query("access_token") String accessToken);

  /**
   * 为用户创建消息
   * 
   * @param username
   * @param message
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{username}/message")
  public Call<ResponseBean> createMessage(@Path("username") String username, @Body UserMessage message,
      @Query("access_token") String accessToken);

  /**
   * 删除消息
   * 
   * @param username
   * @param messageId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{username}/message/{message_id}")
  public Call<ResponseBean> deleteMessage(@Path("username") String username, @Path("message_id") String messageId,
      @Query("access_token") String accessToken);
}
