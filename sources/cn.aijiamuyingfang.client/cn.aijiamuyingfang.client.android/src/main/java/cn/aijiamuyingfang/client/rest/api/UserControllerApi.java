package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.domain.user.response.GetUserPhoneResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import io.reactivex.Observable;
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
public interface UserControllerApi {

  /**
   * 获取用户
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}")
  public Observable<ResponseBean<User>> getUser(@Path("user_id") String userId,
      @Query("access_token") String accessToken);

  /**
   * 注册用户
   * 
   * @param user
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/register")
  public Observable<ResponseBean<User>> registerUser(@Body User user, @Query("access_token") String accessToken);

  /**
   * 获取用户手机号
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/phone")
  public Observable<ResponseBean<GetUserPhoneResponse>> getUserPhone(@Path("user_id") String userId,
      @Query("access_token") String accessToken);

  /**
   * 更新用户信息
   * 
   * @param userId
   * @param user
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{user_id}")
  public Observable<ResponseBean<User>> updateUser(@Path("user_id") String userId, @Body User user,
      @Query("access_token") String accessToken);

  /**
   * 获取用户收件地址
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/recieveaddress")
  public Observable<ResponseBean<List<RecieveAddress>>> getUserRecieveAddressList(@Path("user_id") String userId,
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
  public Observable<ResponseBean<RecieveAddress>> addUserRecieveAddress(@Path("user_id") String userId,
      @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 获取收件地址
   * 
   * @param userId
   * @param addressId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{user_id}/recieveaddress/{address_id}")
  public Observable<ResponseBean<RecieveAddress>> getRecieveAddress(@Path("user_id") String userId,
      @Path("address_id") String addressId, @Query("access_token") String accessToken);

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
  public Observable<ResponseBean<RecieveAddress>> updateRecieveAddress(@Path("user_id") String userId,
      @Path("address_id") String addressId, @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 废弃收件地址
   * 
   * @param userId
   * @param addressId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{user_id}/recieveaddress/{address_id}")
  public Observable<ResponseBean<Void>> deprecateRecieveAddress(@Path("user_id") String userId,
      @Path("address_id") String addressId, @Query("access_token") String accessToken);
}
