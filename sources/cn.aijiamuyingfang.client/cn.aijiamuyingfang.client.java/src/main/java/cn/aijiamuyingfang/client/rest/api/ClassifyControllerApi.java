package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.vo.classify.Classify;
import cn.aijiamuyingfang.vo.response.ResponseBean;
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
 * ClassifyController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 05:59:07
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface ClassifyControllerApi {

  /**
   * 获取所有顶层条目
   * 
   * @return
   */
  @GET(value = "/goods-service/classify")
  public Call<ResponseBean> getTopClassifyList();

  /**
   * 获取某个条目
   * 
   * @param classifyId
   * @return
   */
  @GET(value = "/goods-service/classify/{classify_id}")
  public Call<ResponseBean> getClassify(@Path(value = "classify_id") String classifyId);

  /**
   * 废弃条目
   * 
   * @param classifyId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/classify/{classify_id}")
  public Call<ResponseBean> deleteClassify(@Path(value = "classify_id") String classifyId,
      @Query("access_token") String accessToken);

  /**
   * 创建顶层条目
   * 
   * @param request
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/classify")
  public Call<ResponseBean> createTopClassify(@Body Classify request, @Query("access_token") String accessToken);

  /**
   * 获得条目下的所有子条目
   * 
   * @param classifyId
   * @return
   */
  @GET(value = "/goods-service/classify/{classify_id}/subclassify")
  public Call<ResponseBean> getSubClassifyList(@Path(value = "classify_id") String classifyId);

  /**
   * 创建子条目
   * 
   * @param classifyId
   * @param classifyRequest
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/classify/{classify_id}/subclassify")
  public Call<ResponseBean> createSubClassify(@Path(value = "classify_id") String classifyId,
      @Body MultipartBody classifyRequest, @Query("access_token") String accessToken);

  /**
   * 条目下添加商品
   * 
   * @param classifyId
   * @param goodId
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/classify/{classify_id}/good/{good_id}")
  public Call<ResponseBean> addClassifyGood(@Path(value = "classify_id") String classifyId,
      @Path(value = "good_id") String goodId, @Query("access_token") String accessToken);
}
