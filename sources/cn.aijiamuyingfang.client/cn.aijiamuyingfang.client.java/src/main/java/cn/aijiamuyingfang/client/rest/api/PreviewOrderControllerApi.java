package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.review.PreviewOrderItem;
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
   * @param username
   * @param previewItemId
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/shoporder-service/user/{username}/previeworder/item/{preview_item_id}")
  public Call<ResponseBean> updatePreviewOrderItem(@Path("username") String username,
      @Path("preview_item_id") String previewItemId, @Body PreviewOrderItem request,
      @Query("access_token") String accessToken);

  /**
   * 删除预览的商品项
   * 
   * @param username
   * @param previewItemId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/shoporder-service/user/{username}/previeworder/item/{previewItemId}")
  public Call<ResponseBean> deletePreviewOrderItem(@Path("username") String username,
      @Path("previewItemId") String previewItemId, @Query("access_token") String accessToken);

  /**
   * 生成用户的预览订单
   * 
   * @param username
   * @param goodIdList
   * @param accessToken
   * @return
   */
  @GET(value = "/shoporder-service/user/{username}/previeworder")
  public Call<ResponseBean> generatePreviewOrder(@Path("username") String username,
      @Query("good_id") List<String> goodIdList, @Query("access_token") String accessToken);
}
