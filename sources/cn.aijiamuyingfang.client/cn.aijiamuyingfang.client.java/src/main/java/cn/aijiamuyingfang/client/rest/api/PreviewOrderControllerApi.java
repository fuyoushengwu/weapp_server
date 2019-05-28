package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrderItem;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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
@SuppressWarnings("rawtypes")
public interface PreviewOrderControllerApi {
  /**
   * 更新预览的商品项
   * 
   * @param userid
   * @param previewitemId
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{userid}/previeworder/item/{previewitemId}")
  public Call<ResponseBean> updatePreviewOrderItem(@Path("userid") String userid,
      @Path("previewitemId") String previewitemId, @Body PreviewOrderItem request,
      @Query("access_token") String accessToken);

  /**
   * 删除预览的商品项
   * 
   * @param userid
   * @param previewItemId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{userid}/previeworder/item/{previewItemId}")
  public Call<ResponseBean> deletePreviewOrderItem(@Path("userid") String userid,
      @Path("previewItemId") String previewItemId, @Query("access_token") String accessToken);

  /**
   * 生成用户的预览订单
   * 
   * @param userid
   * @param goodids
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{userid}/previeworder")
  public Call<ResponseBean> generatePreviewOrder(@Path("userid") String userid, @Query("goodids") List<String> goodids,
      @Query("access_token") String accessToken);
}
