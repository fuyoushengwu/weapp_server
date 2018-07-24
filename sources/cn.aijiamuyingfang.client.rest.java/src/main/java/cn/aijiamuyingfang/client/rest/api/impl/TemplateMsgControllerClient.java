package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.TemplateMsgControllerApi;
import cn.aijiamuyingfang.commons.domain.exception.WXServiceException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderItem;
import cn.aijiamuyingfang.commons.domain.wxservice.TemplateMsg;
import cn.aijiamuyingfang.commons.domain.wxservice.TemplateMsgKeyValue;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用TemplateMsgController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 02:30:00
 */
@Service
@SuppressWarnings("rawtypes")
public class TemplateMsgControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(TemplateMsgControllerClient.class);

  /**
   * 时间的表达式
   */
  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

  private static final Callback<ResponseBean> Empty_Callback = new Callback<ResponseBean>() {

    @Override
    public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
      LOGGER.info("onResponse:" + response.message());
    }

    @Override
    public void onFailure(Call<ResponseBean> call, Throwable t) {
      LOGGER.error(t.getMessage(), t);
    }
  };

  @HttpService
  private TemplateMsgControllerApi templatemsgControllerApi;

  /**
   * 发送预定单所有商品已到货的消息(需要用户确认预订单是否发货)
   * 
   * @param token
   * @param openid
   * @param order
   * @param updatedGood
   * @param async
   * @throws IOException
   */
  public void sendPreOrderMsg(String token, String openid, ShopOrder order, Good updatedGood, boolean async)
      throws IOException {
    if (null == order || null == updatedGood) {
      return;
    }
    if (StringUtils.isEmpty(openid)) {
      return;
    }

    String keyword1Value = updatedGood.getName();
    String keyword2Value = updatedGood.getPrice() + "";
    String keyword3Value = "您预订单中的商品已全部到货,点击该消息确认是否发货";
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword4Value = dateFormat.format(order.getCreateTime());

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value);
    sendPreOrderMsg(token, openid, message, async);
  }

  /**
   * 发送预定单所有商品已到货的消息(需要用户确认预订单是否发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @param async
   * @throws IOException
   */
  public void sendPreOrderMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendPreOrderMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendPreOrderMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  /**
   * 发送用户自取订单消息(订单已经准备好,可以自取)
   * 
   * @param token
   * @param openid
   * @param order
   * @param async
   * @throws IOException
   */
  public void sendPickupMsg(String token, String openid, ShopOrder order, boolean async) throws IOException {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(openid)) {
      return;
    }

    String keyword1Value = order.getOrderNo();

    StringBuilder sb = new StringBuilder();
    List<ShopOrderItem> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItem item : shoporderitemList) {
        sb.append(item.getGood().getName() + ",");
      }
    }

    String goodsNameStr = sb.toString();
    String keyword2Value = goodsNameStr.substring(0, goodsNameStr.length() - 1);

    String keyword3Value = "爱家母婴坊";
    String keyword4Value = order.getRecieveAddress().getDetail();

    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword5Value = dateFormat.format(order.getPickupTime());
    String keyword6Value = "亲您的商品已经准备完毕,请准时取货";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value);
    sendPickupMsg(token, openid, message, async);

  }

  /**
   * 发送用户自取订单消息(订单已经准备好,可以自取)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @param async
   * @throws IOException
   */
  public void sendPickupMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendPickupMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendPickupMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  /**
   * 发送快递订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param order
   * @param async
   * @throws IOException
   */
  public void sendThirdSendMsg(String token, String openid, ShopOrder order, boolean async) throws IOException {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(openid)) {
      return;
    }
    String keyword1Value = order.getOrderNo();
    String keyword2Value = order.getThirdsendCompany();
    String keyword3Value = order.getThirdsendNo();

    StringBuilder goodsNameSB = new StringBuilder();
    List<ShopOrderItem> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItem item : shoporderitemList) {
        goodsNameSB.append(item.getGood().getName() + ",");
      }
    }
    String goodsNameStr = goodsNameSB.toString();
    String keyword4Value = goodsNameStr.substring(0, goodsNameStr.length() - 1);
    String keyword5Value = order.getRecieveAddress().getDetail();

    StringBuilder contentSB = new StringBuilder();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItem item : shoporderitemList) {
        contentSB.append(item.getGood().getName()).append(" ");
        contentSB.append(item.getCount()).append(item.getGood().getPack()).append(",");
      }
    }
    String contentStr = contentSB.toString();
    String keyword6Value = contentStr.substring(0, contentStr.length() - 1);
    String keyword7Value = "侍伟";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value, keyword7Value);
    sendThirdSendMsg(token, openid, message, async);
  }

  /**
   * 发送快递订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @param async
   * @throws IOException
   */
  public void sendThirdSendMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendThirdSendMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendThirdSendMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  /**
   * 发送送货上门订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param order
   * @param async
   * @throws IOException
   */
  public void sendOwnSendMsg(String token, String openid, ShopOrder order, boolean async) throws IOException {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(openid)) {
      return;
    }

    String keyword1Value = order.getOrderNo();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword2Value = dateFormat.format(order.getCreateTime());
    String keyword3Value = order.getRecieveAddress().getDetail();
    String keyword4Value = order.getOrderNo();

    StringBuilder contentSB = new StringBuilder();
    List<ShopOrderItem> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItem item : shoporderitemList) {
        contentSB.append(item.getGood().getName()).append(" ");
        contentSB.append(item.getCount()).append(item.getGood().getPack()).append(",");
      }
    }
    String contentStr = contentSB.toString();
    String keyword5Value = contentStr.substring(0, contentStr.length() - 1);

    String keyword6Value = "您的商品已经准备派送,请保持手机畅通";
    String keyword7Value = order.getTotalPrice() + "";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value, keyword7Value);
    sendOwnSendMsg(token, openid, message, async);
  }

  /**
   * 发送送货上门订单消息(订单已发货)
   * 
   * @param token
   * @param openid
   * @param msgData
   * @param async
   * @throws IOException
   */
  public void sendOwnSendMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendOwnSendMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendOwnSendMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  public void sendOrderOverTimeMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendOrderOverTimeMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendOrderOverTimeMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  public void sendOrderConfirmMsg(String token, String openid, TemplateMsg msgData, boolean async) throws IOException {
    if (async) {
      templatemsgControllerApi.sendOrderConfirmMsg(token, openid, msgData).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = templatemsgControllerApi.sendOrderConfirmMsg(token, openid, msgData).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new WXServiceException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }

  /**
   * 根据订单创建模板消息类
   *
   * @param shoporder
   * @param messagedata
   */
  private TemplateMsg createTemplateMsg(ShopOrder shoporder, String... messagedata) {
    TemplateMsg message = new TemplateMsg();
    message.setPage("/pages/order_detail?shoporderid=" + shoporder.getId());
    message.setFormid(shoporder.getFormid());

    List<TemplateMsgKeyValue> messagedataList = new ArrayList<>();
    int keyworkIndex = 1;
    for (String item : messagedata) {
      TemplateMsgKeyValue value = new TemplateMsgKeyValue("keyword" + keyworkIndex, item);
      messagedataList.add(value);
      keyworkIndex++;
    }

    message.setData(messagedataList);
    return message;
  }
}
