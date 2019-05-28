package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.classify.Classify;
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
   * 获取门店下所有的顶层条目
   * 
   * @param storeid
   * @param accessToken
   * @return
   */
  @GET(value = "/store/{storeid}/classify")
  public Call<ResponseBean> getStoreTopClassifyList(@Path(value = "storeid") String storeid,
      @Query("access_token") String accessToken);

  /**
   * 获取所有顶层条目
   * 
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/classify")
  public Call<ResponseBean> getTopClassifyList(@Query("access_token") String accessToken);

  /**
   * 获取某个条目
   * 
   * @param classifyid
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/classify/{classifyid}")
  public Call<ResponseBean> getClassify(@Path(value = "classifyid") String classifyid,
      @Query("access_token") String accessToken);

  /**
   * 废弃条目
   * 
   * @param classifyid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/classify/{classifyid}")
  public Call<ResponseBean> deleteClassify(@Path(value = "classifyid") String classifyid,
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
   * @param classifyid
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/classify/{classifyid}/subclassify")
  public Call<ResponseBean> getSubClassifyList(@Path(value = "classifyid") String classifyid,
      @Query("access_token") String accessToken);

  /**
   * 创建子条目
   * 
   * @param classifyid
   * @param classifyRequest
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/classify/{classifyid}/subclassify")
  public Call<ResponseBean> createSubClassify(@Path(value = "classifyid") String classifyid,
      @Body MultipartBody classifyRequest, @Query("access_token") String accessToken);

  /**
   * 条目下添加商品
   * 
   * @param classifyid
   * @param goodid
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/classify/{classifyid}/good/{goodid}")
  public Call<ResponseBean> addClassifyGood(@Path(value = "classifyid") String classifyid,
      @Path(value = "goodid") String goodid, @Query("access_token") String accessToken);
}
