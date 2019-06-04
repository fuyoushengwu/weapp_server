package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import okhttp3.MultipartBody;
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
 * GoodController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 23:09:59
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface GoodControllerApi {
  /**
   * 分页查询条目下的商品
   * 
   * @param classifyId
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentPage
   * @param pageSize
   * @return
   */
  @GET(value = "/goods-service/classify/{classify_id}/good")
  public Call<ResponseBean> getClassifyGoodList(@Path("classify_id") String classifyId,
      @Query(value = "packFilter") List<String> packFilter, @Query(value = "levelFilter") List<String> levelFilter,
      @Query(value = "orderType") String orderType, @Query(value = "orderValue") String orderValue,
      @Query(value = "current_page") int currentPage, @Query(value = "page_size") int pageSize);

  /**
   * 添加商品
   * 
   * @param goodRequest
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/good")
  public Call<ResponseBean> createGood(@Body MultipartBody goodRequest, @Query("access_token") String accessToken);

  /**
   * 获取商品信息
   * 
   * @param goodId
   * @return
   */
  @GET(value = "/goods-service/good/{good_id}")
  public Call<ResponseBean> getGood(@Path("good_id") String goodId);

  /**
   * 废弃商品
   * 
   * @param goodId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/good/{good_id}")
  public Call<ResponseBean> deprecateGood(@Path("good_id") String goodId, @Query("access_token") String accessToken);

  /**
   * 获取商品详细信息
   * 
   * @param goodId
   * @return
   */
  @GET(value = "/goods-service/good/{good_id}/detail")
  public Call<ResponseBean> getGoodDetail(@Path("good_id") String goodId);

  /**
   * 更新Good信息
   * 
   * @param goodId
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/good/{good_id}")
  public Call<ResponseBean> updateGood(@Path(value = "good_id") String goodId, @Body Good request,
      @Query("access_token") String accessToken);
}
