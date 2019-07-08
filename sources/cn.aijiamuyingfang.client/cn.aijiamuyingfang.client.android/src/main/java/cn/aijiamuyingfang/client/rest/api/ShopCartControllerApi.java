package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.vo.ResponseBean;
import cn.aijiamuyingfang.vo.shopcart.CreateShopCartRequest;
import cn.aijiamuyingfang.vo.shopcart.PagableShopCartList;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
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
public interface ShopCartControllerApi {
  /**
   * 往用户购物车添加商品
   * 
   * @param username
   * @param requestBean
   * @param accessToken
   * @return
   */
  @POST(value = "/shoporder-service/user/{username}/shop_cart")
  public Observable<ResponseBean<ShopCart>> addShopCart(@Path("username") String username,
      @Body CreateShopCartRequest requestBean, @Query("access_token") String accessToken);

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{username}/shop_cart")
  public Observable<ResponseBean<PagableShopCartList>> getShopCartList(@Path("username") String username,
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize,
      @Query("access_token") String accessToken);

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param username
   * @param checked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{username}/shop_cart/allcheck/{checked}")
  public Observable<ResponseBean<Void>> checkAllShopCart(@Path("username") String username,
      @Path("checked") boolean checked, @Query("access_token") String accessToken);

  /**
   * 选中用户购物车下的某一项
   * 
   * @param username
   * @param shopCartId
   * @param checked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{username}/shop_cart/{shop_cart_id}/check/{checked}")
  public Observable<ResponseBean<Void>> checkShopCart(@Path("username") String username,
      @Path("shop_cart_id") String shopCartId, @Path("checked") boolean checked,
      @Query("access_token") String accessToken);

  /**
   * 删除用户购物车中的某项
   * 
   * @param username
   * @param shopCartId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{username}/shop_cart/{shop_cart_id}")
  public Observable<ResponseBean<Void>> deleteShopCart(@Path("username") String username,
      @Path("shop_cart_id") String shopCartId, @Query("access_token") String accessToken);

  /**
   * 修改用户购物车中商品数量
   * 
   * @param username
   * @param shopCartId
   * @param count
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{username}/shop_cart/{shop_cart_id}/count/{count}")
  public Observable<ResponseBean<ShopCart>> updateShopCartCount(@Path("username") String username,
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
