package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shoporder.SendType;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.commons.domain.shoporder.request.CreateUserShoprderRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.response.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetUserShopOrderListResponse;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
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
   * @param token
   * @param userid
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/user/{userid}/shoporder")
  public Observable<ResponseBean<GetUserShopOrderListResponse>> getUserShopOrderList(
      @Header(AuthConstants.HEADER_STRING) String token, @Path(value = "userid") String userid,
      @Query(value = "status") List<ShopOrderStatus> status, @Query(value = "sendtype") List<SendType> sendtype,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize);

  /**
   * 分页获取所有的订单信息
   * 
   * @param token
   * @param status
   * @param sendtype
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/shoporder")
  public Observable<ResponseBean<GetShopOrderListResponse>> getShopOrderList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query(value = "status") List<ShopOrderStatus> status,
      @Query(value = "sendtype") List<SendType> sendtype, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 更新订单
   * 
   * @param token
   * @param shoporderid
   * @param requestBean
   * @return
   */
  @PUT(value = "/shoporder/{shoporderid}/status")
  public Observable<ResponseBean<Void>> updateShopOrderStatus(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("shoporderid") String shoporderid, @Body UpdateShopOrderStatusRequest requestBean);

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param token
   * @param shoporderid
   * @return
   */
  @DELETE(value = "/shoporder/{shoporderid}")
  public Observable<ResponseBean<Void>> delete100DaysFinishedShopOrder(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("shoporderid") String shoporderid);

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   */
  @DELETE(value = "/user/{userid}/shoporder/{shoporderid}")
  public Observable<ResponseBean<Void>> deleteUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shoporderid") String shoporderid);

  /**
   * 确认订单结束,先要判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   */
  @PUT(value = "/user/{userid}/shoporder/{shoporderid}/finisheorder")
  public Observable<ResponseBean<ConfirmUserShopOrderFinishedResponse>> confirmUserShopOrderFinished(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid,
      @Path("shoporderid") String shoporderid);

  /**
   * 更新订单的收货地址,先要判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @param addressid
   * @return
   */
  @PUT(value = "/user/{userid}/shoporder/{shoporderid}/recieveaddress/{addressid}")
  public Observable<ResponseBean<Void>> updateUserShopOrderRecieveAddress(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid,
      @Path("shoporderid") String shoporderid, @Path("addressid") String addressid);

  /**
   * 分页获取已完成预约单
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/shoporder/preorder/finished")
  public Observable<ResponseBean<GetFinishedPreOrderListResponse>> getFinishedPreOrderList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   */
  @GET(value = "/user/{userid}/shoporder/{shoporderid}")
  public Observable<ResponseBean<ShopOrder>> getUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path(value = "shoporderid") String shoporderid);

  /**
   * 创建用户订单
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @return
   */
  @POST(value = "/user/{userid}/shoporder")
  public Observable<ResponseBean<ShopOrder>> createUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Body CreateUserShoprderRequest requestBean);

  /**
   * 得到用户订单根据状态分类的数目
   * 
   * 
   * @param token
   * @param userid
   * @return
   */
  @GET(value = "/user/{userid}/shoporder/statuscount")
  public Observable<ResponseBean<Map<String, Double>>> getUserShopOrderStatusCount(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid);

  /**
   * 分页获取预定的商品信息
   * 
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/shoporder/preordergoods")
  public Observable<ResponseBean<GetPreOrderGoodListResponse>> getPreOrderGoodList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 预定的商品到货,更新预约单
   * 
   * 
   * @param token
   * @param goodid
   * @param request
   * @return
   */
  @PUT(value = "/shoporder/preorder/good/{goodid}")
  public Observable<ResponseBean<Void>> updatePreOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("goodid") String goodid, @Body Good request);
}
