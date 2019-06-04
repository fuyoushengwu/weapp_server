package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shopcart.response.GetShopCartListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopCartRequest;
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
 * ShopCartController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-10 00:00:47
 */
@HttpApi(baseurl = "weapp.host_name")
public interface ShopCartControllerApi {
  /**
   * 往用户购物车添加商品
   * 
   * @param userId
   * @param requestBean
   * @param accessToken
   * @return
   */
  @POST(value = "/shoporder-service/user/{user_id}/shop_cart")
  public Observable<ResponseBean<ShopCart>> addShopCart(@Path("user_id") String userId,
      @Body CreateShopCartRequest requestBean, @Query("access_token") String accessToken);

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userId
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{user_id}/shop_cart")
  public Observable<ResponseBean<GetShopCartListResponse>> getShopCartList(@Path("user_id") String userId,
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param userId
   * @param isChecked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{user_id}/shop_cart/allcheck/{is_checked}")
  public Observable<ResponseBean<Void>> checkAllShopCart(@Path("user_id") String userId,
      @Path("is_checked") boolean isChecked, @Query("access_token") String accessToken);

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userId
   * @param shopCartId
   * @param isChecked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{user_id}/shop_cart/{shop_cart_id}/check/{is_checked}")
  public Observable<ResponseBean<Void>> checkShopCart(@Path("user_id") String userId,
      @Path("shop_cart_id") String shopCartId, @Path("is_checked") boolean isChecked,
      @Query("access_token") String accessToken);

  /**
   * 删除用户购物车中的某项
   * 
   * @param userId
   * @param shopCartId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{user_id}/shop_cart/{shop_cart_id}")
  public Observable<ResponseBean<Void>> deleteShopCart(@Path("user_id") String userId,
      @Path("shop_cart_id") String shopCartId, @Query("access_token") String accessToken);

  /**
   * 修改用户购物车中商品数量
   * 
   * @param userId
   * @param shopCartId
   * @param count
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{user_id}/shop_cart/{shop_cart_id}/count/{count}")
  public Observable<ResponseBean<ShopCart>> updateShopCartCount(@Path("user_id") String userId,
      @Path("shop_cart_id") String shopCartId, @Path("count") int count, @Query("access_token") String accessToken);

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/shop_cart/good/{good_id}")
  public Observable<ResponseBean<Void>> deleteGood(@Path("good_id") String goodId,
      @Query("access_token") String accessToken);
}
