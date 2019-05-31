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
   * @param classifyid
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/goods-service/classify/{classifyid}/good")
  public Call<ResponseBean> getClassifyGoodList(@Path("classifyid") String classifyid,
      @Query(value = "packFilter") List<String> packFilter, @Query(value = "levelFilter") List<String> levelFilter,
      @Query(value = "orderType") String orderType, @Query(value = "orderValue") String orderValue,
      @Query(value = "currentpage") int currentpage, @Query(value = "pagesize") int pagesize);

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
   * @param goodid
   * @return
   */
  @GET(value = "/goods-service/good/{goodid}")
  public Call<ResponseBean> getGood(@Path("goodid") String goodid);

  /**
   * 废弃商品
   * 
   * @param goodid
   * @param accessToken
   * @return
   */
  @DELETE(value = "/goods-service/good/{goodid}")
  public Call<ResponseBean> deprecateGood(@Path("goodid") String goodid, @Query("access_token") String accessToken);

  /**
   * 获取商品详细信息
   * 
   * @param goodid
   * @return
   */
  @GET(value = "/goods-service/good/{goodid}/detail")
  public Call<ResponseBean> getGoodDetail(@Path("goodid") String goodid);

  /**
   * 更新Good信息
   * 
   * @param goodid
   * @param request
   * @param accessToken
   * @return
   */
  @PUT(value = "/goods-service/good/{goodid}")
  public Call<ResponseBean> updateGood(@Path(value = "goodid") String goodid, @Body Good request,
      @Query("access_token") String accessToken);
}
