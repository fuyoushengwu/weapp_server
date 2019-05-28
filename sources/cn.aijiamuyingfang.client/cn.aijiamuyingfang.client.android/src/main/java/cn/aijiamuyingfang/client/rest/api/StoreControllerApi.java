package cn.aijiamuyingfang.client.rest.api;

import java.util.List;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.domain.store.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.client.domain.store.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
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
public interface StoreControllerApi {

  /**
   * 分页获取在使用中的Store
   * 
   * @param currentpage
   *          当前页 默认值:1 (currentpage必须&ge;1,否则重置为1)
   * @param pagesize
   *          每页大小 默认值:10(pagesize必须&gt;0,否则重置为1)
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/store")
  public Observable<ResponseBean<GetInUseStoreListResponse>> getInUseStoreList(
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize,
      @Query("access_token") String accessToken);

  /**
   * 创建门店
   * 
   * @param storeRequest
   * @param accessToken
   * @return
   */
  @POST(value = "/goods-service/store")
  public Observable<ResponseBean<Store>> createStore(@Body MultipartBody storeRequest,
      @Query("access_token") String accessToken);

  /**
   * 获取门店信息
   * 
   * @param storeid
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/store/{storeid}")
  public Observable<ResponseBean<Store>> getStore(@Path("storeid") String storeid,
      @Query("access_token") String accessToken);

  /**
   * 更新门店信息
   * 
   * @param storeid
   * @param storeRequest
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/store/{storeid}")
  public Observable<ResponseBean<Store>> updateStore(@Path("storeid") String storeid, @Body Store storeRequest,
      @Query("access_token") String accessToken);

  /**
   * 废弃门店
   * 
   * @param storeid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/store/{storeid}")
  public Observable<ResponseBean<Void>> deprecateStore(@Path("storeid") String storeid,
      @Query("access_token") String accessToken);

  /**
   * 获取默认门店Id
   * 
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/store/defaultid")
  public Observable<ResponseBean<GetDefaultStoreIdResponse>> getDefaultStoreId(
      @Query("access_token") String accessToken);

  /**
   * 获取在哪些城市有门店分布
   * 
   * @param accessToken
   * @return
   */
  @GET(value = "/goods-service/store/city")
  public Observable<ResponseBean<List<String>>> getStoresCity(@Query("access_token") String accessToken);
}
