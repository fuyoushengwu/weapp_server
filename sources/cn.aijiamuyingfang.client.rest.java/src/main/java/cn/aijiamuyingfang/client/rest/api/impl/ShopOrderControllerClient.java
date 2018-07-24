package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.ShopOrderControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.commons.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.shoporder.SendType;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.commons.domain.shoporder.request.CreateUserShoprderRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.response.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetUserShopOrderListResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

/**
 * [描述]:
 * <p>
 * 客户端调用 ShopOrderController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 17:17:01
 */
@Service
@SuppressWarnings("rawtypes")
public class ShopOrderControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(ShopOrderControllerClient.class);

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
  private ShopOrderControllerApi shoporderControllerApi;

  /**
   * 分页获取用户的订单信息
   * 
   * @param token
   * @param userid
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetUserShopOrderListResponse getUserShopOrderList(String token, String userid, List<ShopOrderStatus> status,
      List<SendType> sendtype, int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .getUserShopOrderList(token, userid, status, sendtype, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetUserShopOrderListResponse getUserShoporderListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetUserShopOrderListResponse.class);
      if (null == getUserShoporderListResponse) {
        throw new ShopOrderException("500", "get user shoporder list return code is '200',but return data is null");
      }
      return getUserShoporderListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取所有的订单信息
   * 
   * @param token
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetShopOrderListResponse getShopOrderList(String token, List<ShopOrderStatus> status, List<SendType> sendtype,
      int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .getShopOrderList(token, status, sendtype, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetShopOrderListResponse getShoporderListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetShopOrderListResponse.class);
      if (null == getShoporderListResponse) {
        throw new ShopOrderException("500", "get  shoporder list return code is '200',but return data is null");
      }
      return getShoporderListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 更新订单
   * 
   * @param token
   * @param shoporderid
   * @param requestBean
   * @param async
   * @return
   * @throws IOException
   */
  public void updateShopOrderStatus(String token, String shoporderid, UpdateShopOrderStatusRequest requestBean,
      boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updateShopOrderStatus(token, shoporderid, requestBean).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shoporderControllerApi.updateShopOrderStatus(token, shoporderid, requestBean)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param token
   * @param shoporderid
   * @param async
   * @return
   * @throws IOException
   */
  public void delete100DaysFinishedShopOrder(String token, String shoporderid, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.delete100DaysFinishedShopOrder(token, shoporderid).enqueue(Empty_Callback);
      return;
    }
    Response<
        ResponseBean> response = shoporderControllerApi.delete100DaysFinishedShopOrder(token, shoporderid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @param async
   * @return
   * @throws IOException
   */
  public void deleteUserShopOrder(String token, String userid, String shoporderid, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.deleteUserShopOrder(token, userid, shoporderid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shoporderControllerApi.deleteUserShopOrder(token, userid, shoporderid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   * @throws IOException
   */
  public ConfirmUserShopOrderFinishedResponse confirmUserShopOrderFinished(String token, String userid,
      String shoporderid) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.confirmUserShopOrderFinished(token, userid, shoporderid)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      ConfirmUserShopOrderFinishedResponse confirmResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), ConfirmUserShopOrderFinishedResponse.class);
      if (null == confirmResponse) {
        throw new ShopOrderException("500",
            "confirm user shoporder finish return code is '200',but return data is null");
      }
      return confirmResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步确认订单结束,先要判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @param callback
   * @return
   */
  public void confirmUserShopOrderFinishedAsync(String token, String userid, String shoporderid,
      Callback<ResponseBean> callback) {
    shoporderControllerApi.confirmUserShopOrderFinished(token, userid, shoporderid).enqueue(callback);
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @param addressid
   * @param async
   * @return
   * @throws IOException
   */
  @PUT(value = "/user/{userid}/shoporder/{shoporderid}/recieveaddress/{addressid}")
  public void updateUserShopOrderRecieveAddress(String token, String userid, String shoporderid, String addressid,
      boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updateUserShopOrderRecieveAddress(token, userid, shoporderid, addressid)
          .enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shoporderControllerApi
        .updateUserShopOrderRecieveAddress(token, userid, shoporderid, addressid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取已完成预约单
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetFinishedPreOrderListResponse getFinishedPreOrderList(String token, int currentpage, int pagesize)
      throws IOException {
    Response<
        ResponseBean> response = shoporderControllerApi.getFinishedPreOrderList(token, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetFinishedPreOrderListResponse getFinishedPreOrderListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetFinishedPreOrderListResponse.class);
      if (null == getFinishedPreOrderListResponse) {
        throw new ShopOrderException("500", "get finished preorder list  return code is '200',but return data is null");
      }
      return getFinishedPreOrderListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   * @throws IOException
   */
  public ShopOrder getUserShopOrder(String token, String userid, String shoporderid) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.getUserShopOrder(token, userid, shoporderid).execute();
    return getShopOrderFromResponse(response, "get user shoporder return code is '200',but return data is null");
  }

  /**
   * 创建用户订单
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @return
   * @throws IOException
   */
  public ShopOrder createUserShopOrder(String token, String userid, CreateUserShoprderRequest requestBean)
      throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.createUserShopOrder(token, userid, requestBean).execute();
    return getShopOrderFromResponse(response, "create user shoporder return code is '200',but return data is null");
  }

  /**
   * 从response中获取ShopOrder
   * 
   * @param response
   * @param exceptionmsg
   * @return
   */
  private ShopOrder getShopOrderFromResponse(Response<ResponseBean> response, String exceptionmsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      ShopOrder shoporder = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), ShopOrder.class);
      if (null == shoporder) {
        throw new ShopOrderException("500", exceptionmsg);
      }
      return shoporder;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步创建用户订单
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @param callback
   */
  public void createUserShopOrderAsync(String token, String userid, CreateUserShoprderRequest requestBean,
      Callback<ResponseBean> callback) {
    shoporderControllerApi.createUserShopOrder(token, userid, requestBean).enqueue(callback);
  }

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * 
   * @param token
   * @param userid
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public Map<String, Double> getUserShopOrderStatusCount(String token, String userid) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.getUserShopOrderStatusCount(token, userid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      if (null == returnData) {
        throw new ShopOrderException("500",
            "get user shoporder status count return code is '200',but return data is null");
      }
      return (Map<String, Double>) returnData;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取预定的商品信息
   * 
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetPreOrderGoodListResponse getPreOrderGoodList(String token, int currentpage, int pagesize)
      throws IOException {
    Response<
        ResponseBean> response = shoporderControllerApi.getPreOrderGoodList(token, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetPreOrderGoodListResponse getPreOrderGoodListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetPreOrderGoodListResponse.class);
      if (null == getPreOrderGoodListResponse) {
        throw new ShopOrderException("500", "get preorder good list return code is '200',but return data is null");
      }
      return getPreOrderGoodListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 预定的商品到货,更新预约单
   * 
   * 
   * @param token
   * @param goodid
   * @param request
   * @param async
   * @throws IOException
   */
  public void updatePreOrder(String token, String goodid, Good request, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updatePreOrder(token, goodid, request).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shoporderControllerApi.updatePreOrder(token, goodid, request).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }
}
