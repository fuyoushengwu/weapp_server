package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.domain.user.response.GetUserPhoneResponse;
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
public interface UserControllerApi {

  /**
   * 获取用户
   * 
   * @param username
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}")
  public Observable<ResponseBean<User>> getUser(@Path("username") String username,
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
   * @param username
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}/phone")
  public Observable<ResponseBean<GetUserPhoneResponse>> getUserPhone(@Path("username") String username,
      @Query("access_token") String accessToken);

  /**
   * 更新用户信息
   * 
   * @param username
   * @param user
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{username}")
  public Observable<ResponseBean<User>> updateUser(@Path("username") String username, @Body User user,
      @Query("access_token") String accessToken);

  /**
   * 获取用户收件地址
   * 
   * @param username
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}/recieveaddress")
  public Observable<ResponseBean<List<RecieveAddress>>> getUserRecieveAddressList(@Path("username") String username,
      @Query("access_token") String accessToken);

  /**
   * 给用户添加收件地址
   * 
   * @param username
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{username}/recieveaddress")
  public Observable<ResponseBean<RecieveAddress>> addUserRecieveAddress(@Path("username") String username,
      @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 获取收件地址
   * 
   * @param username
   * @param addressId
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{username}/recieveaddress/{address_id}")
  public Observable<ResponseBean<RecieveAddress>> getRecieveAddress(@Path("username") String username,
      @Path("address_id") String addressId, @Query("access_token") String accessToken);

  /**
   * 更新收件地址信息
   * 
   * @param username
   * @param addressId
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{username}/recieveaddress/{address_id}")
  public Observable<ResponseBean<RecieveAddress>> updateRecieveAddress(@Path("username") String username,
      @Path("address_id") String addressId, @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 废弃收件地址
   * 
   * @param username
   * @param addressId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{username}/recieveaddress/{address_id}")
  public Observable<ResponseBean<Void>> deprecateRecieveAddress(@Path("username") String username,
      @Path("address_id") String addressId, @Query("access_token") String accessToken);
}
