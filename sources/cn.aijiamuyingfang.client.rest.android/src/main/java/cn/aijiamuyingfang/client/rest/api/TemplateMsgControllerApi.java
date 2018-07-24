package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.wxservice.TemplateMsg;
import io.reactivex.Observable;
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
  Observable<ResponseBean<Void>> sendPreOrderMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);

  /**
   * 发送用户自取订单消息(订单已经准备好,可以自取)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/pickup")
  Observable<ResponseBean<Void>> sendPickupMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);

  /**
   * 发送快递订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/thirdsend")
  Observable<ResponseBean<Void>> sendThirdSendMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);

  /**
   * 发送送货上门订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/ownsend")
  Observable<ResponseBean<Void>> sendOwnSendMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);

  /**
   * 发送订单超时消息(自取订单超时未取,则发送超时消息)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @return
   */
  @POST(value = "/user/{openid}/wxservice/templatemsg/overtime")
  Observable<ResponseBean<Void>> sendOrderOverTimeMsg(@Header(AuthConstants.HEADER_STRING) String token,
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
  Observable<ResponseBean<Void>> sendOrderConfirmMsg(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("openid") String openid, @Body TemplateMsg msgData);
}
