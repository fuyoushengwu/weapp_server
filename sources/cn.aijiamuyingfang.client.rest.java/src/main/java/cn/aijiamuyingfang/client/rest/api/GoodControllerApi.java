package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
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
   * @param token
   * @param classifyid
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentpage
   * @param pagesize
   * @return
   */
  @GET(value = "/classify/{classifyid}/good")
  public Call<ResponseBean> getClassifyGoodList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("classifyid") String classifyid, @Query(value = "packFilter") List<String> packFilter,
      @Query(value = "levelFilter") List<String> levelFilter, @Query(value = "orderType") String orderType,
      @Query(value = "orderValue") String orderValue, @Query(value = "currentpage") int currentpage,
      @Query(value = "pagesize") int pagesize);

  /**
   * 添加商品
   * 
   * @param token
   * @param goodRequest
   * @return
   */
  @POST(value = "/good")
  public Call<ResponseBean> createGood(@Header(AuthConstants.HEADER_STRING) String token,
      @Body MultipartBody goodRequest);

  /**
   * 获取商品信息
   * 
   * @param token
   * @param goodid
   * @return
   */
  @GET(value = "/good/{goodid}")
  public Call<ResponseBean> getGood(@Header(AuthConstants.HEADER_STRING) String token, @Path("goodid") String goodid);

  /**
   * 废弃商品
   * 
   * @param token
   * @param goodid
   * @return
   */
  @DELETE(value = "/good/{goodid}")
  public Call<ResponseBean> deprecateGood(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("goodid") String goodid);

  /**
   * 获取商品详细信息
   * 
   * @param token
   * @param goodid
   * @return
   */
  @GET(value = "/good/{goodid}/detail")
  public Call<ResponseBean> getGoodDetail(@Header(AuthConstants.HEADER_STRING) String token,
      @Path("goodid") String goodid);

  /**
   * 更新Good信息
   * 
   * @param token
   * @param goodid
   * @param request
   * @return
   */
  @PUT(value = "/good/{goodid}")
  public Call<ResponseBean> updateGood(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "goodid") String goodid, @Body Good request);
}
