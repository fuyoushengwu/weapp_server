package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.client.domain.exception.CouponException;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用CouponController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 16:38:37
 */
@Service
@SuppressWarnings("rawtypes")
public class CouponControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(CouponControllerClient.class);

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
  private CouponControllerApi couponControllerApi;

  /**
   * 分页获取用户兑换券
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public GetUserVoucherListResponse getUserVoucherList(String username, int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = couponControllerApi.getUserVoucherList(username, currentPage, pageSize, accessToken)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetUserVoucherListResponse uservoucherListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetUserVoucherListResponse.class);
      if (null == uservoucherListResponse) {
        throw new CouponException("500", "get user voucher list  return code is '200',but return data is null");
      }
      return uservoucherListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取商品兑换券
   * 
   * @param currentPage
   * @param pageSize
   * @return
   * @throws IOException
   */
  public GetGoodVoucherListResponse getGoodVoucherList(int currentPage, int pageSize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getGoodVoucherList(currentPage, pageSize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetGoodVoucherListResponse goodvoucherListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetGoodVoucherListResponse.class);
      if (null == goodvoucherListResponse) {
        throw new CouponException("500", "get good voucher list return code is '200',but return data is null");
      }
      return goodvoucherListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取商品兑换券
   * 
   * @param voucherId
   * @return
   * @throws IOException
   */
  public GoodVoucher getGoodVoucher(String voucherId) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getGoodVoucher(voucherId).execute();
    return getGoodVoucherFromResponse(response, "get good voucher  return code is '200',but return data is null");
  }

  /**
   * 从response中获取GoodVoucher
   * 
   * @param response
   * @param errormsg
   * @return
   * @throws IOException
   */
  private GoodVoucher getGoodVoucherFromResponse(Response<ResponseBean> response, String errormsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GoodVoucher goodvoucher = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GoodVoucher.class);
      if (null == goodvoucher) {
        throw new CouponException("500", errormsg);
      }
      return goodvoucher;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 创建商品兑换券
   * 
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public GoodVoucher createGoodVoucher(GoodVoucher request, String accessToken) throws IOException {
    Response<ResponseBean> response = couponControllerApi.createGoodVoucher(request, accessToken).execute();
    return getGoodVoucherFromResponse(response, "create good voucher return code is '200',but return data is null");
  }

  /**
   * 异步创建商品兑换券
   * 
   * @param request
   * @param accessToken
   * @param callback
   */
  public void createGoodVoucherAsync(GoodVoucher request, String accessToken, Callback<ResponseBean> callback) {
    couponControllerApi.createGoodVoucher(request, accessToken).enqueue(callback);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherId
   * @param async
   * @param accessToken
   * @throws IOException
   */
  public void deprecateGoodVoucher(String voucherId, boolean async, String accessToken) throws IOException {
    if (async) {
      couponControllerApi.deprecateGoodVoucher(voucherId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateGoodVoucher(voucherId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取可用的兑换方式
   * 
   * @param currentPage
   * @param pageSize
   * @return
   * @throws IOException
   */
  public GetVoucherItemListResponse getVoucherItemList(int currentPage, int pageSize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getVoucherItemList(currentPage, pageSize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetVoucherItemListResponse voucherItemListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetVoucherItemListResponse.class);
      if (null == voucherItemListResponse) {
        throw new CouponException("500", "get voucher item list  return code is '200',but return data is null");
      }
      return voucherItemListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());

  }

  /**
   * 获取兑换方式
   * 
   * @param voucherItemId
   * @return
   * @throws IOException
   */
  public VoucherItem getVoucherItem(String voucherItemId) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getVoucherItem(voucherItemId).execute();
    return getVoucherItemFromResponse(response, "get voucher item list  return code is '200',but return data is null");
  }

  /**
   * 从response中获取VoucherItem
   * 
   * @param response
   * @param errormsg
   * @return
   * @throws IOException
   */
  private VoucherItem getVoucherItemFromResponse(Response<ResponseBean> response, String errormsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      VoucherItem voucherItem = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), VoucherItem.class);
      if (null == voucherItem) {
        throw new CouponException("500", errormsg);
      }
      return voucherItem;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 创建兑换方式
   * 
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public VoucherItem createVoucherItem(VoucherItem request, String accessToken) throws IOException {
    Response<ResponseBean> response = couponControllerApi.createVoucherItem(request, accessToken).execute();
    return getVoucherItemFromResponse(response, "create voucher item return code is '200',but return data is null");
  }

  /**
   * 异步创建兑换方式
   * 
   * @param request
   * @param accessToken
   * @param callback
   */
  public void createVoucherItemAsync(VoucherItem request, String accessToken, Callback<ResponseBean> callback) {
    couponControllerApi.createVoucherItem(request, accessToken).enqueue(callback);
  }

  /**
   * 废弃兑换方式
   * 
   * @param voucherItemId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deprecateVoucherItem(String voucherItemId, String accessToken, boolean async) throws IOException {
    if (async) {
      couponControllerApi.deprecateVoucherItem(voucherItemId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateVoucherItem(voucherItemId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());

  }

}
