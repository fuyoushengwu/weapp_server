package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shopcart.AddShopCartItemRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
   * @param token
   * @param userid
   * @param requestBean
   * @return
   */
  @POST(value = "/user/{userid}/shopcart")
  public Call<ResponseBean> addShopCartItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Body AddShopCartItemRequest requestBean);

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/user/{userid}/shopcart")
  public Call<ResponseBean> getShopCartItemList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param token
   * @param userid
   * @param ischecked
   * @return
   */
  @PUT(value = "/user/{userid}/shopcart/allcheck/{ischecked}")
  public Call<ResponseBean> checkAllShopCartItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("ischecked") boolean ischecked);

  /**
   * 选中用户购物车下的某一项
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param ischecked
   * @return
   */
  @PUT(value = "/user/{userid}/shopcart/{shopcartid}/check/{ischecked}")
  public Call<ResponseBean> checkShopCartItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shopcartid") String shopcartid, @Path("ischecked") boolean ischecked);

  /**
   * 删除用户购物车中的某项
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @return
   */
  @DELETE(value = "/user/{userid}/shopcart/{shopcartid}")
  public Call<ResponseBean> deleteShopCartItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shopcartid") String shopcartid);

  /**
   * 修改用户购物车中商品数量
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param count
   * @return
   */
  @PUT(value = "/user/{userid}/shopcart/{shopcartid}/count/{count}")
  public Call<ResponseBean> updateShopCartItemCount(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shopcartid") String shopcartid, @Path("count") int count);

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param token
   * @param goodid
   * @return
   */
  @DELETE(value = "/shopcart/good/{goodid}")
  public Call<ResponseBean> deleteGood(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("goodid") String goodid);
}
