package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * PreviewOrderController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-10 00:00:47
 */
@HttpApi(baseurl = "weapp.host_name")
public interface PreviewOrderControllerApi {
  /**
   * 更新预览的商品项
   * 
   * @param token
   * @param userid
   * @param itemid
   * @param request
   * @return
   */
  @PUT(value = "/user/{userid}/previeworder/item/{itemid}")
  public Observable<ResponseBean<PreviewOrderItem>> updatePreviewOrderItem(
      @Header(AuthConstants.HEADER_STRING) String token, @Path("userid") String userid, @Path("itemid") String itemid,
      @Body PreviewOrderItem request);

  /**
   * 删除预览的商品项
   * 
   * @param token
   * @param userid
   * @param itemid
   * @return
   */
  @DELETE(value = "/user/{userid}/previeworder/item/{itemid}")
  public Observable<ResponseBean<Void>> deletePreviewOrderItem(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Path("itemid") String itemid);

  /**
   * 生成用户的预览订单
   * 
   * @param token
   * @param userid
   * @param goodids
   * @return
   */
  @GET(value = "/user/{userid}/previeworder")
  public Observable<ResponseBean<PreviewOrder>> generatePreviewOrder(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("userid") String userid, @Query("goodids") List<String> goodids);
}
