package cn.aijiamuyingfang.server.client.api;

import cn.aijiamuyingfang.server.client.annotation.HttpApi;
import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.domain.SendType;
import cn.aijiamuyingfang.server.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.shoporder.CreateUserShoprderRequest;
import cn.aijiamuyingfang.server.domain.shoporder.UpdateShopOrderStatusRequest;
import java.util.List;
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
 * ShopOrderController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 23:59:40
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
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
  public Call<ResponseBean> getUserShopOrderList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "userid") String userid, @Query(value = "status") List<ShopOrderStatus> status,
      @Query(value = "sendtype") List<SendType> sendtype, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

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
  public Call<ResponseBean> getShopOrderList(@Header(AuthConstants.HEADER_STRING) String token,
      @Query(value = "status") List<ShopOrderStatus> status, @Query(value = "sendtype") List<SendType> sendtype,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize);

  /**
   * 更新订单
   * 
   * @param token
   * @param shoporderid
   * @param requestBean
   * @return
   */
  @PUT(value = "/shoporder/{shoporderid}/status")
  public Call<ResponseBean> updateShopOrderStatus(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("shoporderid") String shoporderid, @Body UpdateShopOrderStatusRequest requestBean);

  /**
   * 如果订单已经完成100天,可以删除(Admin和Sender都可以调用该方法)
   * 
   * @param token
   * @param shoporderid
   * @return
   */
  @DELETE(value = "/shoporder/{shoporderid}")
  public Call<ResponseBean> delete100DaysFinishedShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("shoporderid") String shoporderid);

  /**
   * 删除用户下的订单,该操作需要先判断该订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   */
  @DELETE(value = "/user/{userid}/shoporder/{shoporderid}")
  public Call<ResponseBean> deleteUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
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
  public Call<ResponseBean> confirmUserShopOrderFinished(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shoporderid") String shoporderid);

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
  public Call<ResponseBean> updateUserShopOrderRecieveAddress(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("shoporderid") String shoporderid, @Path("addressid") String addressid);

  /**
   * 分页获取已完成预约单
   * 
   * @param token
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/shoporder/preorder/finished")
  public Call<ResponseBean> getFinishedPreOrderList(@Header(AuthConstants.HEADER_STRING) String token,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize);

  /**
   * 获取用户订单,先要判断订单是否属于用户
   * 
   * @param token
   * @param userid
   * @param shoporderid
   * @return
   */
  @GET(value = "/user/{userid}/shoporder/{shoporderid}")
  public Call<ResponseBean> getUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
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
  public Call<ResponseBean> createUserShopOrder(@Header(AuthConstants.HEADER_STRING) String token,
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
  public Call<ResponseBean> getUserShopOrderStatusCount(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid);

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
  public Call<ResponseBean> getPreOrderGoodList(@Header(AuthConstants.HEADER_STRING) String token,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize);

  /**
   * 预定的商品到货,更新预约单
   * 
   * 
   * @param token
   * @param request
   * @return
   */
  @PUT(value = "/shoporder/preorder")
  public Call<ResponseBean> updatePreOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Body GoodRequest request);
}
