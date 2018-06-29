package cn.aijiamuyingfang.server.client.api;

import cn.aijiamuyingfang.server.client.annotation.HttpApi;
import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.UserSession;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * 用户会话服务的API
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 20:15:38
 */
@HttpApi(baseurl = "weapp.host_name")
public interface UserSessionApi {
	@GET(value = "/user/wxservice/wxsession")
	Call<UserSession> jscode2Session(@Query("jscode") String jscode);
}
