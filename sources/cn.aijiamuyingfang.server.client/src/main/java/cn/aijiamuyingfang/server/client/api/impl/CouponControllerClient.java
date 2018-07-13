package cn.aijiamuyingfang.server.client.api.impl;

import cn.aijiamuyingfang.server.client.annotation.HttpService;
import cn.aijiamuyingfang.server.client.api.CouponControllerApi;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.domain.coupon.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GetVoucherItemListResponse;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.server.domain.coupon.GoodVoucherRequest;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItemRequest;
import cn.aijiamuyingfang.server.domain.exception.CouponException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */

  public GetUserVoucherListResponse getUserVoucherList(String token, String userid, int currentpage, int pagesize)
      throws IOException {
    Response<
        ResponseBean> response = couponControllerApi.getUserVoucherList(token, userid, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetUserVoucherListResponse uservoucherListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetUserVoucherListResponse.class);
      if (null == uservoucherListResponse) {
        throw new RuntimeException("get user voucher list  return code is '200',.but return data is null");
      }
      return uservoucherListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param token
   * @param userid
   * @param goodids
   * @return
   * @throws IOException
   */
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(String token, String userid, List<String> goodids)
      throws IOException {
    Response<ResponseBean> response = couponControllerApi.getUserShopOrderVoucherList(token, userid, goodids).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetShopOrderVoucherListResponse shoporderVoucherListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetShopOrderVoucherListResponse.class);
      if (null == shoporderVoucherListResponse) {
        throw new RuntimeException("get shoporder voucher list return code is '200',.but return data is null");
      }
      return shoporderVoucherListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 分页获取商品兑换券
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetGoodVoucherListResponse getGoodVoucherList(String token, int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getGoodVoucherList(token, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetGoodVoucherListResponse goodvoucherListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetGoodVoucherListResponse.class);
      if (null == goodvoucherListResponse) {
        throw new RuntimeException("get good voucher list return code is '200',.but return data is null");
      }
      return goodvoucherListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 创建商品兑换券
   * 
   * @param token
   * @param request
   * @return
   * @throws IOException
   */
  public GoodVoucher createGoodVoucher(String token, GoodVoucherRequest request) throws IOException {
    Response<ResponseBean> response = couponControllerApi.createGoodVoucher(token, request).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GoodVoucher goodvoucher = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GoodVoucher.class);
      if (null == goodvoucher) {
        throw new RuntimeException("create good voucher return code is '200',.but return data is null");
      }
      return goodvoucher;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步创建商品兑换券
   * 
   * @param token
   * @param request
   * @param callback
   */
  public void createGoodVoucherAsync(String token, GoodVoucherRequest request, Callback<ResponseBean> callback) {
    couponControllerApi.createGoodVoucher(token, request).enqueue(callback);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param token
   * @param voucherid
   * @param async
   * @throws IOException
   */
  public void deprecateGoodVoucher(String token, String voucherid, boolean async) throws IOException {
    if (async) {
      couponControllerApi.deprecateGoodVoucher(token, voucherid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateGoodVoucher(token, voucherid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
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
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetVoucherItemListResponse getVoucherItemList(String token, int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = couponControllerApi.getVoucherItemList(token, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetVoucherItemListResponse voucheritemListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetVoucherItemListResponse.class);
      if (null == voucheritemListResponse) {
        throw new RuntimeException("get voucher item list  return code is '200',.but return data is null");
      }
      return voucheritemListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());

  }

  /**
   * 创建兑换方式
   * 
   * @param token
   * @param request
   * @return
   * @throws IOException
   */
  public VoucherItem createVoucherItem(String token, VoucherItemRequest request) throws IOException {
    Response<ResponseBean> response = couponControllerApi.createVoucherItem(token, request).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      VoucherItem voucheritem = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), VoucherItem.class);
      if (null == voucheritem) {
        throw new RuntimeException("create voucher item return code is '200',.but return data is null");
      }
      return voucheritem;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步创建兑换方式
   * 
   * @param token
   * @param request
   * @param callback
   */
  public void createVoucherItemAsync(String token, VoucherItemRequest request, Callback<ResponseBean> callback) {
    couponControllerApi.createVoucherItem(token, request).enqueue(callback);
  }

  /**
   * 废弃兑换方式
   * 
   * @param token
   * @param itemid
   * @param async
   * @throws IOException
   */
  public void deprecateVoucherItem(String token, String itemid, boolean async) throws IOException {
    if (async) {
      couponControllerApi.deprecateVoucherItem(token, itemid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = couponControllerApi.deprecateVoucherItem(token, itemid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());

  }

}
