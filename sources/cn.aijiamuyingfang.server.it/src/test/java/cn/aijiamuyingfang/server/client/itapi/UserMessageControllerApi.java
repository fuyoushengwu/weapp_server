package cn.aijiamuyingfang.server.client.itapi;

import cn.aijiamuyingfang.server.client.annotation.HttpApi;
import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.domain.user.UserMessageRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
   * @param token
   * @param userid
   * @return
   */
  @GET(value = "/user/{userid}/message/unread/count")
  public Call<ResponseBean> getUserUnReadMessageCount(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid);

  /**
   * 分页获取用户消息
   * 
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/user/{userid}/message")
  public Call<ResponseBean> getUserMessageList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Query("currentpage") int currentpage, @Query("pagesize") int pagesize);

  /**
   * 为用户创建消息
   * 
   * @param token
   * @param userid
   * @param request
   * @return
   */
  @POST(value = "/user/{userid}/message")
  public Call<ResponseBean> createMessage(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Body UserMessageRequest request);

  /**
   * 删除消息
   * 
   * @param token
   * @param userid
   * @param messageid
   * @return
   */
  @DELETE(value = "/user/{userid}/message/{messageid}")
  public Call<ResponseBean> deleteMessage(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("messageid") String messageid);
}
