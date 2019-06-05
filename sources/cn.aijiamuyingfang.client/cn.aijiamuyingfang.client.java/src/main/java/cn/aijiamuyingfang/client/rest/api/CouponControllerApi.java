package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * CouponController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 13:15:07
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface CouponControllerApi {

  /**
   * 分页获取用户兑换券
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/user/{username}/coupon/uservoucher")
  public Call<ResponseBean> getUserVoucherList(@Path("username") String username, @Query("current_page") int currentPage,
      @Query("page_size") int pageSize, @Query("access_token") String accessToken);

  /**
   * 分页获取商品兑换券
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @GET(value = "/coupon-service/coupon/goodvoucher")
  public Call<ResponseBean> getGoodVoucherList(@Query("current_page") int currentPage,
      @Query("page_size") int pageSize);

  /**
   * 获取商品兑换券
   * 
   * @param voucherId
   * @return
   */
  @GET(value = "/coupon-service/coupon/goodvoucher/{voucher_id}")
  public Call<ResponseBean> getGoodVoucher(@Path("voucher_id") String voucherId);

  /**
   * 创建商品兑换券
   * 
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/coupon-service/coupon/goodvoucher")
  public Call<ResponseBean> createGoodVoucher(@Body GoodVoucher request, @Query("access_token") String accessToken);

  /**
   * 废弃商品兑换券
   * 
   * @param voucherId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/coupon-service/coupon/goodvoucher/{voucher_id}")
  public Call<ResponseBean> deprecateGoodVoucher(@Path("voucher_id") String voucherId,
      @Query("access_token") String accessToken);

  /**
   * 分页获取可用的兑换方式
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  @GET(value = "/coupon-service/coupon/voucher_item")
  public Call<ResponseBean> getVoucherItemList(@Query("current_page") int currentPage,
      @Query("page_size") int pageSize);

  /**
   * 获取兑换方式
   * 
   * @param voucherItemId
   * @return
   */
  @GET(value = "/coupon-service/coupon/voucher_item/{voucher_item_id}")
  public Call<ResponseBean> getVoucherItem(@Path("voucher_item_id") String voucherItemId);

  /**
   * 创建兑换方式
   * 
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/coupon-service/coupon/voucher_item")
  public Call<ResponseBean> createVoucherItem(@Body VoucherItem request, @Query("access_token") String accessToken);

  /**
   * 废弃兑换方式
   * 
   * @param voucherItemId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/coupon-service/coupon/voucher_item/{voucher_item_id}")
  public Call<ResponseBean> deprecateVoucherItem(@Path("voucher_item_id") String voucherItemId,
      @Query("access_token") String accessToken);
}
