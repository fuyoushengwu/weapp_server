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
   * @param userid
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}")
  public Observable<ResponseBean<User>> getUser(@Path("userid") String userid,
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
   * @param userid
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}/phone")
  public Observable<ResponseBean<GetUserPhoneResponse>> getUserPhone(@Path("userid") String userid,
      @Query("access_token") String accessToken);

  /**
   * 更新用户信息
   * 
   * @param userid
   * @param user
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{userid}")
  public Observable<ResponseBean<User>> updateUser(@Path("userid") String userid, @Body User user,
      @Query("access_token") String accessToken);

  /**
   * 获取用户收件地址
   * 
   * @param userid
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}/recieveaddress")
  public Observable<ResponseBean<List<RecieveAddress>>> getUserRecieveAddressList(@Path("userid") String userid,
      @Query("access_token") String accessToken);

  /**
   * 给用户添加收件地址
   * 
   * @param userid
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/user-service/user/{userid}/recieveaddress")
  public Observable<ResponseBean<RecieveAddress>> addUserRecieveAddress(@Path("userid") String userid,
      @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 获取收件地址
   * 
   * @param userid
   * @param addressid
   * @param accessToken
   * @return
   */
  @GET(value = "/user-service/user/{userid}/recieveaddress/{addressid}")
  public Observable<ResponseBean<RecieveAddress>> getRecieveAddress(@Path("userid") String userid,
      @Path("addressid") String addressid, @Query("access_token") String accessToken);

  /**
   * 更新收件地址信息
   * 
   * @param userid
   * @param addressid
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/user-service/user/{userid}/recieveaddress/{addressid}")
  public Observable<ResponseBean<RecieveAddress>> updateRecieveAddress(@Path("userid") String userid,
      @Path("addressid") String addressid, @Body RecieveAddress request, @Query("access_token") String accessToken);

  /**
   * 废弃收件地址
   * 
   * @param userid
   * @param addressid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/user-service/user/{userid}/recieveaddress/{addressid}")
  public Observable<ResponseBean<Void>> deprecateRecieveAddress(@Path("userid") String userid,
      @Path("addressid") String addressid, @Query("access_token") String accessToken);
}
