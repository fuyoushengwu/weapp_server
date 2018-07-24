package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.wxservice.WXSession;
import io.reactivex.Observable;
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
public interface WXSessionControllerApi {
  @GET(value = AuthConstants.WXSESSION_URL)
  Observable<ResponseBean<WXSession>> jscode2Session(@Query("jscode") String jscode);
}
