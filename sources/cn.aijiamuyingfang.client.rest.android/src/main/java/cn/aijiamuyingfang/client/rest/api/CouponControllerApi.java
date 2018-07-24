package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/user/{userid}/coupon/uservoucher")
  public Observable<ResponseBean<GetUserVoucherListResponse>> getUserVoucherList(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid,
      @Query("currentpage") int currentpage, @Query("pagesize") int pagesize);

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param token
   * @param userid
   * @param goodids
   * @return
   */
  @GET(value = "/user/{userid}/coupon/shoporder")
  public Observable<ResponseBean<GetShopOrderVoucherListResponse>> getUserShopOrderVoucherList(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid,
      @Query("goodids") List<String> goodids);

  /**
   * 分页获取商品兑换券
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/coupon/goodvoucher")
  public Observable<ResponseBean<GetGoodVoucherListResponse>> getGoodVoucherList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query("currentpage") int currentpage,
      @Query("pagesize") int pagesize);

  /**
   * 创建商品兑换券
   * 
   * @param token
   * @param request
   * @return
   */
  @POST(value = "/coupon/goodvoucher")
  public Observable<ResponseBean<GoodVoucher>> createGoodVoucher(@Header(AuthConstants.HEADER_STRING) String token,
      @Body GoodVoucher request);

  /**
   * 废弃商品兑换券
   * 
   * @param token
   * @param voucherid
   * @return
   */
  @DELETE(value = "/coupon/goodvoucher/{voucherid}")
  public Observable<ResponseBean<Void>> deprecateGoodVoucher(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("voucherid") String voucherid);

  /**
   * 分页获取可用的兑换方式
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/coupon/voucheritem")
  public Observable<ResponseBean<GetVoucherItemListResponse>> getVoucherItemList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query("currentpage") int currentpage,
      @Query("pagesize") int pagesize);

  /**
   * 创建兑换方式
   * 
   * @param token
   * @param request
   * @return
   */
  @POST(value = "/coupon/voucheritem")
  public Observable<ResponseBean<VoucherItem>> createVoucherItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Body VoucherItem request);

  /**
   * 废弃兑换方式
   * 
   * @param token
   * @param itemid
   * @return
   */
  @DELETE(value = "/coupon/voucheritem/{itemid}")
  public Observable<ResponseBean<Void>> deprecateVoucherItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("itemid") String itemid);
}
