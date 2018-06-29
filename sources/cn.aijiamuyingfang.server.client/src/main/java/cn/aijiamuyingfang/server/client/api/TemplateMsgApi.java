package cn.aijiamuyingfang.server.client.api;

import cn.aijiamuyingfang.server.client.annotation.HttpApi;
import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.TemplateMsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * [描述]:
 * <p>
 * 模板消息服务的API
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 20:15:09
 */
@HttpApi(baseurl = "weapp.host_name")
public interface TemplateMsgApi {
	@POST(value = "/user/{openid}/wxservice/templatemsg/preorder")
	Call<Object> sendPreOrderMsg(@Path("openid") long openid, @Body TemplateMsg msgData);

	@POST(value = "/user/{openid}/wxservice/templatemsg/pickup")
	Call<Object> sendPickupMsg(@Path("openid") long openid, @Body TemplateMsg msgData);

	@POST(value = "/user/{openid}/wxservice/templatemsg/thirdsend")
	Call<Object> sendThirdSendMsg(@Path("openid") long openid, @Body TemplateMsg msgData);

	@POST(value = "/user/{openid}/wxservice/templatemsg/ownsend")
	Call<Object> sendOwnSendMsg(@Path("openid") long openid, @Body TemplateMsg msgData);

	@POST(value = "/user/{openid}/wxservice/templatemsg/overtime")
	Call<Object> sendOrderOverTimeMsg(@Path("openid") long openid, @Body TemplateMsg msgData);

	@POST(value = "/user/{openid}/wxservice/templatemsg/orderconfirm")
	Call<Object> sendOrderConfirmMsg(@Path("openid") long openid, @Body TemplateMsg msgData);
}
