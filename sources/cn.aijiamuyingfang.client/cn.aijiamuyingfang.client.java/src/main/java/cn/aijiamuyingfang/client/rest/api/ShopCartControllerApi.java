package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopCartRequest;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import retrofit2.Call;
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
@SuppressWarnings("rawtypes")
public interface ShopCartControllerApi {
  /**
   * 往用户购物车添加商品
   * 
   * @param userid
   * @param requestBean
   * @param accessToken
   * @return
   */
  @POST(value = "/shoporder-service/user/{userid}/shopcart")
  public Call<ResponseBean> addShopCart(@Path("userid") String userid, @Body CreateShopCartRequest requestBean,
      @Query("access_token") String accessToken);

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{userid}/shopcart")
  public Call<ResponseBean> getShopCartList(@Path("userid") String userid,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize,
      @Query("access_token") String accessToken);

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param userid
   * @param ischecked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{userid}/shopcart/allcheck/{ischecked}")
  public Call<ResponseBean> checkAllShopCart(@Path("userid") String userid, @Path("ischecked") boolean ischecked,
      @Query("access_token") String accessToken);

  /**
   * 选中用户购物车下的某一项
   * 
   * @param userid
   * @param shopcartid
   * @param ischecked
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{userid}/shopcart/{shopcartid}/check/{ischecked}")
  public Call<ResponseBean> checkShopCart(@Path("userid") String userid, @Path("shopcartid") String shopcartid,
      @Path("ischecked") boolean ischecked, @Query("access_token") String accessToken);

  /**
   * 删除用户购物车中的某项
   * 
   * @param userid
   * @param shopcartid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{userid}/shopcart/{shopcartid}")
  public Call<ResponseBean> deleteShopCart(@Path("userid") String userid, @Path("shopcartid") String shopcartid,
      @Query("access_token") String accessToken);

  /**
   * 修改用户购物车中商品数量
   * 
   * @param userid
   * @param shopcartid
   * @param count
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{userid}/shopcart/{shopcartid}/count/{count}")
  public Call<ResponseBean> updateShopCartCount(@Path("userid") String userid, @Path("shopcartid") String shopcartid,
      @Path("count") int count, @Query("access_token") String accessToken);

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/shopcart/good/{goodid}")
  public Call<ResponseBean> deleteGood(@Path("goodid") String goodid, @Query("access_token") String accessToken);
}
