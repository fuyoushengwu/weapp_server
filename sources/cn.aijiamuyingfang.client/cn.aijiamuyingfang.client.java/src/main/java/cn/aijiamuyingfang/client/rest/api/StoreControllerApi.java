package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.store.Store;
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
 * StoreController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 16:13:34
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface StoreControllerApi {

  /**
   * 分页获取在使用中的Store
   * 
   * @param currentPage
   *          当前页 默认值:1 (currentPage必须&ge;1,否则重置为1)
   * @param pageSize
   *          每页大小 默认值:10(pageSize必须&gt;0,否则重置为1)
   * @return
   */
  @GET(value = "/goods-service/store")
  public Call<ResponseBean> getInUseStoreList(@Query(value = "current_page") int currentPage,
      @Query(value = "page_size") int pageSize);

  /**
   * 创建门店
   * 
   * @param storeRequest
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/store")
  public Call<ResponseBean> createStore(@Body MultipartBody storeRequest, @Query("access_token") String accessToken);

  /**
   * 获取门店信息
   * 
   * @param storeId
   * @return
   */
  @GET(value = "/goods-service/store/{store_id}")
  public Call<ResponseBean> getStore(@Path("store_id") String storeId);

  /**
   * 更新门店信息
   * 
   * @param storeId
   * @param storeRequest
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/store/{store_id}")
  public Call<ResponseBean> updateStore(@Path("store_id") String storeId, @Body Store storeRequest,
      @Query("access_token") String accessToken);

  /**
   * 废弃门店
   * 
   * @param storeId
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/store/{store_id}")
  public Call<ResponseBean> deprecateStore(@Path("store_id") String storeId, @Query("access_token") String accessToken);

  /**
   * 获取默认门店Id
   * 
   * @return
   */
  @GET(value = "/goods-service/store/defaultid")
  public Call<ResponseBean> getDefaultStoreId();

  /**
   * 获取在哪些城市有门店分布
   * 
   * @return
   */
  @GET(value = "/goods-service/store/city")
  public Call<ResponseBean> getStoresCity();

  /**
   * 门店下添加条目
   * 
   * @param storeId
   * @param classifyId
   * @param accessToken
   * @return
   */
  @PUT(value = "/store/{store_id}/classify/{classify_id}")
  public Call<ResponseBean> addStoreClassify(@Path("store_id") String storeId, @Path("classify_id") String classifyId,
      @Query("access_token") String accessToken);

}
