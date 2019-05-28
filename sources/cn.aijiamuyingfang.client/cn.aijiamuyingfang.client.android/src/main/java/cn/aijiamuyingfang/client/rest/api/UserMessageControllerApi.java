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
   * @param userid
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}/message/unread/count")
  public Observable<ResponseBean<Integer>> getUserUnReadMessageCount(@Path("userid") String userid,
      @Query("access_token") String accessToken);

  /**
   * 分页获取用户消息
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}/message")
  public Observable<ResponseBean<GetMessagesListResponse>> getUserMessageList(@Path("userid") String userid,
      @Query("currentpage") int currentpage, @Query("pagesize") int pagesize,
      @Query("access_token") String accessToken);

  /**
   * 为用户创建消息
   * 
   * @param userid
   * @param message
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{userid}/message")
  public Observable<ResponseBean<UserMessage>> createMessage(@Path("userid") String userid, @Body UserMessage message,
      @Query("access_token") String accessToken);

  /**
   * 删除消息
   * 
   * @param userid
   * @param messageid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{userid}/message/{messageid}")
  public Observable<ResponseBean<Void>> deleteMessage(@Path("userid") String userid,
      @Path("messageid") String messageid, @Query("access_token") String accessToken);
}
