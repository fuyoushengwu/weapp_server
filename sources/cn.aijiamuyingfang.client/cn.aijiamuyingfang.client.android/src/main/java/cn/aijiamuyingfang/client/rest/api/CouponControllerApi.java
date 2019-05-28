package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import io.reactivex.Observable;
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
public interface CouponControllerApi {

  /**
   * 分页获取用户兑换券
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/user/{userid}/coupon/uservoucher")
  public Observable<ResponseBean<GetUserVoucherListResponse>> getUserVoucherList(@Path("userid") String userid,
      @Query("currentpage") int currentpage, @Query("pagesize") int pagesize,
      @Query("access_token") String accessToken);

  /**
   * 分页获取商品兑换券
   * 
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/coupon/goodvoucher")
  public Observable<ResponseBean<GetGoodVoucherListResponse>> getGoodVoucherList(@Query("currentpage") int currentpage,
      @Query("pagesize") int pagesize, @Query("access_token") String accessToken);

  /**
   * 获取商品兑换券
   * 
   * @param voucherid
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/coupon/goodvoucher/{voucherid}")
  public Observable<ResponseBean<GoodVoucher>> getGoodVoucher(@Path("voucherid") String voucherid,
      @Query("access_token") String accessToken);

  /**
   * 创建商品兑换券
   * 
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/coupon-service/coupon/goodvoucher")
  public Observable<ResponseBean<GoodVoucher>> createGoodVoucher(@Body GoodVoucher request,
      @Query("access_token") String accessToken);

  /**
   * 废弃商品兑换券
   * 
   * @param voucherid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/coupon-service/coupon/goodvoucher/{voucherid}")
  public Observable<ResponseBean<Void>> deprecateGoodVoucher(@Path("voucherid") String voucherid,
      @Query("access_token") String accessToken);

  /**
   * 分页获取可用的兑换方式
   * 
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/coupon/voucheritem")
  public Observable<ResponseBean<GetVoucherItemListResponse>> getVoucherItemList(@Query("currentpage") int currentpage,
      @Query("pagesize") int pagesize, @Query("access_token") String accessToken);

  /**
   * 获取兑换方式
   * 
   * @param voucheritemId
   * @param accessToken
   * @return
   */
  @GET(value = "/coupon-service/coupon/voucheritem/{voucheritemId}")
  public Observable<ResponseBean<VoucherItem>> getVoucherItem(@Path("voucheritemId") String voucheritemId,
      @Query("access_token") String accessToken);

  /**
   * 创建兑换方式
   * 
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/coupon-service/coupon/voucheritem")
  public Observable<ResponseBean<VoucherItem>> createVoucherItem(@Body VoucherItem request,
      @Query("access_token") String accessToken);

  /**
   * 废弃兑换方式
   * 
   * @param voucheritemId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/coupon-service/coupon/voucheritem/{voucheritemId}")
  public Observable<ResponseBean<Void>> deprecateVoucherItem(@Path("voucheritemId") String voucheritemId,
      @Query("access_token") String accessToken);
}
