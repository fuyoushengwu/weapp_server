package cn.aijiamuyingfang.client.rest.api;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.commons.domain.goods.ClassifyRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
   * @param token
   * @param storeid
   * @return
   */
  @GET(value = "/store/{storeid}/classify")
  public Call<ResponseBean> getStoreTopClassifyList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "storeid") String storeid);

  /**
   * 获取某个条目
   * 
   * @param token
   * @param classifyid
   * @return
   */
  @GET(value = "/classify/{classifyid}")
  public Call<ResponseBean> getClassify(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "classifyid") String classifyid);

  /**
   * 废弃条目
   * 
   * @param token
   * @param classifyid
   * @return
   */
  @DELETE(value = "/classify/{classifyid}")
  public Call<ResponseBean> deleteClassify(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "classifyid") String classifyid);

  /**
   * 创建顶层条目
   * 
   * @param token
   * @param request
   * @return
   */
  @POST(value = "/classify")
  public Call<ResponseBean> createTopClassify(@Header(AuthConstants.HEADER_STRING) String token,
      @Body ClassifyRequest request);

  /**
   * 获得条目下的所有子条目
   * 
   * @param token
   * @param classifyid
   * @return
   */
  @GET(value = "/classify/{classifyid}/subclassify")
  public Call<ResponseBean> getSubClassifyList(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "classifyid") String classifyid);

  /**
   * 创建子条目
   * 
   * @param token
   * @param classifyid
   * @param coverImage
   * @param classifyRequest
   * @return
   */
  @POST(value = "/classify/{classifyid}/subclassify")
  public Call<ResponseBean> createSubClassify(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "classifyid") String classifyid, @Body MultipartBody classifyRequest);

  /**
   * 条目下添加商品
   * 
   * @param token
   * @param classifyid
   * @param goodid
   * @return
   */
  @PUT(value = "/classify/{classifyid}/good/{goodid}")
  public Call<ResponseBean> addClassifyGood(@Header(AuthConstants.HEADER_STRING) String token,
      @Path(value = "classifyid") String classifyid, @Path(value = "goodid") String goodid);
}
