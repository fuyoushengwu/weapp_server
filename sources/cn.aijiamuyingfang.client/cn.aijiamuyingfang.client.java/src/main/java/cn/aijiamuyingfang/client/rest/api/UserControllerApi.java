package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * UserController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-10 23:01:18
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface UserControllerApi {

  /**
   * 获取用户
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}")
  public Call<ResponseBean> getUser(@Path("user_id") String userId, @Query("access_token") String accessToken);

  /**
   * 注册用户
   * 
   * @param user
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/register")
  public Call<ResponseBean> registerUser(@Body User user, @Query("access_token") String accessToken);

  /**
   * 获取用户手机号
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/phone")
  public Call<ResponseBean> getUserPhone(@Path("user_id") String userId, @Query("access_token") String accessToken);

  /**
   * 更新用户信息
   * 
   * @param userId
   * @param user
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{user_id}")
  public Call<ResponseBean> updateUser(@Path("user_id") String userId, @Body User user,
      @Query("access_token") String accessToken);

  /**
   * 获取用户收件地址
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/recieveaddress")
  public Call<ResponseBean> getUserRecieveAddressList(@Path("user_id") String userId,
      @Query("access_token") String accessToken);

  /**
   * 给用户添加收件地址
   * 
   * @param userId
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{user_id}/recieveaddress")
  public Call<ResponseBean> addUserRecieveAddress(@Path("user_id") String userId, @Body RecieveAddress request,
      @Query("access_token") String accessToken);

  /**
   * 获取收件地址
   * 
   * @param userId
   * @param addressId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/recieveaddress/{address_id}")
  public Call<ResponseBean> getRecieveAddress(@Path("user_id") String userId, @Path("address_id") String addressId,
      @Query("access_token") String accessToken);

  /**
   * 更新收件地址信息
   * 
   * @param userId
   * @param addressId
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{user_id}/recieveaddress/{address_id}")
  public Call<ResponseBean> updateRecieveAddress(@Path("user_id") String userId, @Path("address_id") String addressId,
      @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 废弃收件地址
   * 
   * @param userId
   * @param addressId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{user_id}/recieveaddress/{address_id}")
  public Call<ResponseBean> deprecateRecieveAddress(@Path("user_id") String userId,
      @Path("address_id") String addressId, @Query("access_token") String accessToken);
}
