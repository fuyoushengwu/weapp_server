package cn.aijiamuyingfang.server.client.api;

import cn.aijiamuyingfang.server.client.annotation.HttpApi;
import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.TemplateMsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
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
@SuppressWarnings("rawtypes")
public interface TemplateMsgControllerApi {
  /**
   * 发送预定单所有商品已到货的消息(需要用户确认预订单是否发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/preorder")
  Call<ResponseBean> sendPreOrderMsg(@Header(AuthConstants.HEADER_STRING) String token, @Path("openid") String openid,
      @Body TemplateMsg msgData);

  /**
   * 发送用户自取订单消息(订单已经准备好,可以自取)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/pickup")
  Call<ResponseBean> sendPickupMsg(@Header(AuthConstants.HEADER_STRING) String token, @Path("openid") String openid,
      @Body TemplateMsg msgData);

  /**
   * 发送快递订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/thirdsend")
  Call<ResponseBean> sendThirdSendMsg(@Header(AuthConstants.HEADER_STRING) String token, @Path("openid") String openid,
      @Body TemplateMsg msgData);

  /**
   * 发送送货上门订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/ownsend")
  Call<ResponseBean> sendOwnSendMsg(@Header(AuthConstants.HEADER_STRING) String token, @Path("openid") String openid,
      @Body TemplateMsg msgData);

  /**
   * 发送订单超时消息(自取订单超时未取,则发送超时消息)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/overtime")
  Call<ResponseBean> sendOrderOverTimeMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);

  /**
   * 发送订单确认消息
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/orderconfirm")
  Call<ResponseBean> sendOrderConfirmMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);
}
