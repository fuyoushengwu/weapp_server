package cn.aijiamuyingfang.client.rest.api;

import java.util.List;
import java.util.Map;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.previeworder.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.client.domain.previeworder.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.SendType;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopOrderRequest;
import cn.aijiamuyingfang.client.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.client.domain.shoporder.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.client.domain.shoporder.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * ShopOrderController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 23:59:40
 */
@HttpApi(baseurl = "weapp.host_name")
public interface ShopOrderControllerApi {
  /**
   * 分页获取用户的订单信息
   * 
   * @param userId
   * @param status
   * @param sendType
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{user_id}/shoporder")
  public Observable<ResponseBean<GetShopOrderListResponse>> getUserShopOrderList(@Path("user_id") String userId,
      @Query(value = "status") List<ShopOrderStatus> status, @Query(value = "send_type") List<SendType> sendType,
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 获取用户购买商品时可以使用的兑换券
   * 
   * @param userId
   * @param goodIdList
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{user_id}/coupon/shoporder")
  public Observable<ResponseBean<GetShopOrderVoucherListResponse>> getUserShopOrderVoucherList(
      @Path("user_id") String userId, @Query("good_id") List<String> goodIdList,
      @Query("access_token") String accessToken);

  /**
   * 分页获取所有的订单信息
   * 
   * @param status
   * @param sendType
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/shoporder")
  public Observable<ResponseBean<GetShopOrderListResponse>> getShopOrderList(
      @Query(value = "status") List<ShopOrderStatus> status, @Query(value = "send_type") List<SendType> sendType,
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 更新订单
   * 
   * @param shopOrderId
   * @param requestBean
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/shoporder/{shop_order_id}/status")
  public Observable<ResponseBean<Void>> updateShopOrderStatus(@Path("shop_order_id") String shopOrderId,
      @Body UpdateShopOrderStatusRequest requestBean, @Query("access_token") String accessToken);

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param shopOrderId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/shoporder/{shop_order_id}")
  public Observable<ResponseBean<Void>> delete100DaysFinishedShopOrder(@Path("shop_order_id") String shopOrderId,
      @Query("access_token") String accessToken);

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{user_id}/shoporder/{shop_order_id}")
  public Observable<ResponseBean<Void>> deleteUserShopOrder(@Path("user_id") String userId,
      @Path("shop_order_id") String shopOrderId, @Query("access_token") String accessToken);

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{user_id}/shoporder/{shop_order_id}/finisheorder")
  public Observable<ResponseBean<ConfirmShopOrderFinishedResponse>> confirmUserShopOrderFinished(
      @Path("user_id") String userId, @Path("shop_order_id") String shopOrderId,
      @Query("access_token") String accessToken);

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @param addressId
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{user_id}/shoporder/{shop_order_id}/recieveaddress/{address_id}")
  public Observable<ResponseBean<Void>> updateUserShopOrderRecieveAddress(@Path("user_id") String userId,
      @Path("shop_order_id") String shopOrderId, @Path("address_id") String addressId,
      @Query("access_token") String accessToken);

  /**
   * 分页获取已完成预约单
   * 
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/shoporder/preorder/finished")
  public Observable<ResponseBean<GetFinishedPreOrderListResponse>> getFinishedPreOrderList(
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param userId
   * @param shopOrderId
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{user_id}/shoporder/{shop_order_id}")
  public Observable<ResponseBean<ShopOrder>> getUserShopOrder(@Path("user_id") String userId,
      @Path(value = "shop_order_id") String shopOrderId, @Query("access_token") String accessToken);

  /**
   * 创建用户订单
   * 
   * @param userId
   * @param requestBean
   * @param accessToken
   * @return
   */
  @POST(value = "/shoporder-service/user/{user_id}/shoporder")
  public Observable<ResponseBean<ShopOrder>> createUserShopOrder(@Path("user_id") String userId,
      @Body CreateShopOrderRequest requestBean, @Query("access_token") String accessToken);

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * @param userId
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{user_id}/shoporder/statuscount")
  public Observable<ResponseBean<Map<String, Double>>> getUserShopOrderStatusCount(@Path("user_id") String userId,
      @Query("access_token") String accessToken);

  /**
   * 分页获取预定的商品信息
   * 
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/shoporder/preordergoods")
  public Observable<ResponseBean<GetPreOrderGoodListResponse>> getPreOrderGoodList(
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 预定的商品到货,更新预约单
   * 
   * @param goodId
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/shoporder/preorder/good/{good_id}")
  public Observable<ResponseBean<Void>> updatePreOrder(@Path("good_id") String goodId,
      @Query("access_token") String accessToken);
}
