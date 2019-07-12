package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.ShopOrderControllerApi;
import cn.aijiamuyingfang.client.rest.utils.ResponseUtils;
import cn.aijiamuyingfang.vo.exception.CouponException;
import cn.aijiamuyingfang.vo.exception.ShopOrderException;
import cn.aijiamuyingfang.vo.preorder.PagablePreOrderGoodList;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.shoporder.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.vo.shoporder.CreateShopOrderRequest;
import cn.aijiamuyingfang.vo.shoporder.PagableShopOrderList;
import cn.aijiamuyingfang.vo.shoporder.SendType;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.vo.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.vo.utils.JsonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
      LOGGER.info("onResponse:{}", response.message());
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
   * @param username
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagableShopOrderList getUserShopOrderList(String username, List<ShopOrderStatus> statusList,
      List<SendType> sendTypeList, int currentPage, int pageSize, String accessToken) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .getUserShopOrderList(username, statusList, sendTypeList, currentPage, pageSize, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableShopOrderList getShopOrderListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableShopOrderList.class);
      if (null == getShopOrderListResponse) {
        throw new ShopOrderException("500", "get user shoporder list return code is '200',but return data is null");
      }
      return getShopOrderListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param username
   * @param goodIdList
   * @param accessToken
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<ShopOrderVoucher> getUserShopOrderVoucherList(String username, List<String> goodIdList,
      String accessToken) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .getUserShopOrderVoucherList(username, goodIdList, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return JsonUtils.json2List(JsonUtils.list2Json((List<ShopOrderVoucher>) responseBean.getData()),
          ShopOrderVoucher.class);
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取所有的订单信息
   * 
   * @param statusList
   * @param sendTypeList
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagableShopOrderList getShopOrderList(List<ShopOrderStatus> statusList, List<SendType> sendTypeList,
      int currentPage, int pageSize, String accessToken) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .getShopOrderList(statusList, sendTypeList, currentPage, pageSize, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableShopOrderList getShoporderListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableShopOrderList.class);
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
   * @param shopOrderId
   * @param requestBean
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void updateShopOrderStatus(String shopOrderId, UpdateShopOrderStatusRequest requestBean, String accessToken,
      boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updateShopOrderStatus(shopOrderId, requestBean, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleShopOrderVOIDResponse(
        shoporderControllerApi.updateShopOrderStatus(shopOrderId, requestBean, accessToken).execute(), LOGGER);
  }

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param shopOrderId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void delete100DaysFinishedShopOrder(String shopOrderId, String accessToken, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.delete100DaysFinishedShopOrder(shopOrderId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleShopOrderVOIDResponse(
        shoporderControllerApi.delete100DaysFinishedShopOrder(shopOrderId, accessToken).execute(), LOGGER);
  }

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param username
   * @param shopOrderId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteUserShopOrder(String username, String shopOrderId, String accessToken, boolean async)
      throws IOException {
    if (async) {
      shoporderControllerApi.deleteUserShopOrder(username, shopOrderId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleShopOrderVOIDResponse(
        shoporderControllerApi.deleteUserShopOrder(username, shopOrderId, accessToken).execute(), LOGGER);
  }

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param username
   * @param shopOrderId
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ConfirmShopOrderFinishedResponse confirmUserShopOrderFinished(String username, String shopOrderId,
      String accessToken) throws IOException {
    Response<ResponseBean> response = shoporderControllerApi
        .confirmUserShopOrderFinished(username, shopOrderId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      ConfirmShopOrderFinishedResponse confirmResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          ConfirmShopOrderFinishedResponse.class);
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
   * @param username
   * @param shopOrderId
   * @param accessToken
   * @param callback
   */
  public void confirmUserShopOrderFinishedAsync(String username, String shopOrderId, String accessToken,
      Callback<ResponseBean> callback) {
    shoporderControllerApi.confirmUserShopOrderFinished(username, shopOrderId, accessToken).enqueue(callback);
  }

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param username
   * @param shopOrderId
   * @param addressId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void updateUserShopOrderRecieveAddress(String username, String shopOrderId, String addressId,
      String accessToken, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updateUserShopOrderRecieveAddress(username, shopOrderId, addressId, accessToken)
          .enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleShopOrderVOIDResponse(shoporderControllerApi
        .updateUserShopOrderRecieveAddress(username, shopOrderId, addressId, accessToken).execute(), LOGGER);
  }

  /**
   * 分页获取已完成预约单
   * 
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagableShopOrderList getFinishedPreOrderList(int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.getFinishedPreOrderList(currentPage, pageSize, accessToken)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableShopOrderList getFinishedPreOrderListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), PagableShopOrderList.class);
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
   * @param username
   * @param shopOrderId
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopOrder getUserShopOrder(String username, String shopOrderId, String accessToken) throws IOException {
    Response<
        ResponseBean> response = shoporderControllerApi.getUserShopOrder(username, shopOrderId, accessToken).execute();
    return getShopOrderFromResponse(response, "get user shoporder return code is '200',but return data is null");
  }

  /**
   * 创建用户订单
   * 
   * @param username
   * @param requestBean
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopOrder createUserShopOrder(String username, CreateShopOrderRequest requestBean, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.createUserShopOrder(username, requestBean, accessToken)
        .execute();
    return getShopOrderFromResponse(response, "create user shoporder return code is '200',but return data is null");
  }

  /**
   * 从response中获取ShopOrder
   * 
   * @param response
   * @param exceptionmsg
   * @return
   * @throws IOException
   */
  private ShopOrder getShopOrderFromResponse(Response<ResponseBean> response, String exceptionmsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
   * @param username
   * @param requestBean
   * @param accessToken
   * @param callback
   */
  public void createUserShopOrderAsync(String username, CreateShopOrderRequest requestBean, String accessToken,
      Callback<ResponseBean> callback) {
    shoporderControllerApi.createUserShopOrder(username, requestBean, accessToken).enqueue(callback);
  }

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * @param username
   * @param accessToken
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public Map<String, Double> getUserShopOrderStatusCount(String username, String accessToken) throws IOException {
    Response<
        ResponseBean> response = shoporderControllerApi.getUserShopOrderStatusCount(username, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagablePreOrderGoodList getPreOrderGoodList(int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shoporderControllerApi.getPreOrderGoodList(currentPage, pageSize, accessToken)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagablePreOrderGoodList getPreOrderGoodListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), PagablePreOrderGoodList.class);
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
   * @param goodId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void updatePreOrder(String goodId, String accessToken, boolean async) throws IOException {
    if (async) {
      shoporderControllerApi.updatePreOrder(goodId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleShopOrderVOIDResponse(shoporderControllerApi.updatePreOrder(goodId, accessToken).execute(),
        LOGGER);
  }
}
