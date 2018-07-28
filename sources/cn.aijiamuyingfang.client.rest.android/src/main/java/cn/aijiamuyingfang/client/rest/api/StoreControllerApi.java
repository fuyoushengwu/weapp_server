package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.domain.goods.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.commons.domain.goods.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import io.reactivex.Observable;
import java.util.List;
import okhttp3.MultipartBody;
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
   * @param token
   * @param currentpage
   *          当前页 默认值:1 (currentpage必须&ge;1,否则重置为1)
   * @param pagesize
   *          每页大小 默认值:10(pagesize必须&gt;0,否则重置为1)
   * @return
   */
  @GET(value = "/store")
  public Observable<ResponseBean<GetInUseStoreListResponse>> getInUseStoreList(
      @Header(AuthConstants.HEADER_STRING) String token, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 创建门店
   * 
   * @param token
   * @param storeRequest
   * @return
   */
  @POST(value = "/store")
  public Observable<ResponseBean<Store>> createStore(@Header(AuthConstants.HEADER_STRING) String token,
      @Body MultipartBody storeRequest);

  /**
   * 获取门店信息
   * 
   * @param token
   * @param storeid
   * @return
   */
  @GET(value = "/store/{storeid}")
  public Observable<ResponseBean<Store>> getStore(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("storeid") String storeid);

  /**
   * 更新门店信息
   * 
   * @param token
   * @param storeid
   * @param storeRequest
   * @return
   */
  @PUT(value = "/store/{storeid}")
  public Observable<ResponseBean<Store>> updateStore(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("storeid") String storeid, @Body Store storeRequest);

  /**
   * 废弃门店
   * 
   * @param token
   * @param storeid
   * @return
   */
  @DELETE(value = "/store/{storeid}")
  public Observable<ResponseBean<Void>> deprecateStore(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("storeid") String storeid);

  /**
   * 获取默认门店Id
   * 
   * @param token
   * @return
   */
  @GET(value = "/store/defaultid")
  public Observable<ResponseBean<GetDefaultStoreIdResponse>> getDefaultStoreId(
      @Header(AuthConstants.HEADER_STRING) String token);

  /**
   * 获取在哪些城市有门店分布
   * 
   * @param token
   * @return
   */
  @GET(value = "/store/city")
  public Observable<ResponseBean<List<String>>> getStoresCity(@Header(AuthConstants.HEADER_STRING) String token);

  /**
   * 门店下添加条目
   * 
   * @param token
   * @param storeid
   * @param classifyid
   * @return
   */
  @PUT(value = "/store/{storeid}/classify/{classifyid}")
  public Observable<ResponseBean<Void>> addStoreClassify(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("storeid") String storeid, @Path("classifyid") String classifyid);
}
