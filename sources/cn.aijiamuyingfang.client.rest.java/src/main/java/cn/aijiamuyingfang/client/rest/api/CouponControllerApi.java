package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import java.util.List;
import retrofit2.Call;
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
@SuppressWarnings("rawtypes")
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
  public Call<ResponseBean> getUserVoucherList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Query("currentpage") int currentpage, @Query("pagesize") int pagesize);

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param token
   * @param userid
   * @param goodids
   * @return
   */
  @GET(value = "/user/{userid}/coupon/shoporder")
  public Call<ResponseBean> getUserShopOrderVoucherList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Query("goodids") List<String> goodids);

  /**
   * 分页获取商品兑换券
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/coupon/goodvoucher")
  public Call<ResponseBean> getGoodVoucherList(@Header(AuthConstants.HEADER_STRING) String token,
      @Query("currentpage") int currentpage, @Query("pagesize") int pagesize);

  /**
   * 创建商品兑换券
   * 
   * @param token
   * @param request
   * @return
   */
  @POST(value = "/coupon/goodvoucher")
  public Call<ResponseBean> createGoodVoucher(@Header(AuthConstants.HEADER_STRING) String token,
      @Body GoodVoucher request);

  /**
   * 废弃商品兑换券
   * 
   * @param token
   * @param voucherid
   * @return
   */
  @DELETE(value = "/coupon/goodvoucher/{voucherid}")
  public Call<ResponseBean> deprecateGoodVoucher(@Header(AuthConstants.HEADER_STRING) String token,
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
  public Call<ResponseBean> getVoucherItemList(@Header(AuthConstants.HEADER_STRING) String token,
      @Query("currentpage") int currentpage, @Query("pagesize") int pagesize);

  /**
   * 创建兑换方式
   * 
   * @param token
   * @param request
   * @return
   */
  @POST(value = "/coupon/voucheritem")
  public Call<ResponseBean> createVoucherItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Body VoucherItem request);

  /**
   * 废弃兑换方式
   * 
   * @param token
   * @param itemid
   * @return
   */
  @DELETE(value = "/coupon/voucheritem/{itemid}")
  public Call<ResponseBean> deprecateVoucherItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("itemid") String itemid);
}
