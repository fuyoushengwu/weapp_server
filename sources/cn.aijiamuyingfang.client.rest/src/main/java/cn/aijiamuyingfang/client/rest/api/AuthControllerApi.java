package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * AuthController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 17:07:55
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface AuthControllerApi {
  /**
   * 获取JWT
   * 
   * @param jscode
   * @param nickname
   * @param avatar
   * @return
   */

  @GET(value = AuthConstants.GET_TOKEN_URL)
  public Call<ResponseBean> getToken(@Query("jscode") String jscode, @Query("nickname") String nickname,
      @Query("avatar") String avatar);

  /**
   * 注册用户
   * 
   * @param token
   * @param user
   * @return
   */
  @POST(value = "/user/register")
  public Call<ResponseBean> registerUser(@Header(AuthConstants.HEADER_STRING) String token, @Body User user);

  /**
   * 刷新用户token
   * 
   * @param token
   * @return
   */
  @PUT(value = AuthConstants.GET_TOKEN_URL)
  public Call<ResponseBean> refreshToken(@Header(AuthConstants.HEADER_STRING) String token);
}
