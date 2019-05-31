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
   * @param userid
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public GetUserVoucherListResponse getUserVoucherList(String userid, int currentpage, int pagesize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = couponControllerApi.getUserVoucherList(userid, currentpage, pagesize, accessToken)
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
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetGoodVoucherListResponse getGoodVoucherList(int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getGoodVoucherList(currentpage, pagesize).execute();
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
   * @param voucherid
   * @return
   * @throws IOException
   */
  public GoodVoucher getGoodVoucher(String voucherid) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getGoodVoucher(voucherid).execute();
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
   * @param voucherid
   * @param async
   * @param accessToken
   * @throws IOException
   */
  public void deprecateGoodVoucher(String voucherid, boolean async, String accessToken) throws IOException {
    if (async) {
      couponControllerApi.deprecateGoodVoucher(voucherid, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateGoodVoucher(voucherid, accessToken).execute();
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
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetVoucherItemListResponse getVoucherItemList(int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getVoucherItemList(currentpage, pagesize).execute();
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
      GetVoucherItemListResponse voucheritemListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetVoucherItemListResponse.class);
      if (null == voucheritemListResponse) {
        throw new CouponException("500", "get voucher item list  return code is '200',but return data is null");
      }
      return voucheritemListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());

  }

  /**
   * 获取兑换方式
   * 
   * @param voucheritemId
   * @return
   * @throws IOException
   */
  public VoucherItem getVoucherItem(String voucheritemId) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getVoucherItem(voucheritemId).execute();
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
      VoucherItem voucheritem = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), VoucherItem.class);
      if (null == voucheritem) {
        throw new CouponException("500", errormsg);
      }
      return voucheritem;
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
   * @param voucheritemId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deprecateVoucherItem(String voucheritemId, String accessToken, boolean async) throws IOException {
    if (async) {
      couponControllerApi.deprecateVoucherItem(voucheritemId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateVoucherItem(voucheritemId, accessToken).execute();
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
