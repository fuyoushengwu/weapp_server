package cn.aijiamuyingfang.client.rest.api;

import java.util.Map;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * [描述]:
 * <p>
 * FileController客户端调用接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-15 11:10:51
 */
@HttpApi(baseurl = "weapp.host_name")
@SuppressWarnings("rawtypes")
public interface FileControllerApi {
  /**
   * 上传文件
   * 
   * @param filePart
   * @param source
   * @param accessToken
   * @return
   */
  @Multipart
  @POST(value = "/filecenter-service/files")
  public Call<ResponseBean> upload(@Part MultipartBody.Part filePart, @Part("source") String source,
      @Query("access_token") String accessToken);

  /**
   * 删除文件
   * 
   * @param id
   * @param accessToken
   * @return
   */
  @DELETE(value = "/filecenter-service/files/{id}")
  public Call<ResponseBean> delete(@Path("id") String id, @Query("access_token") String accessToken);

  /**
   * 分页查询文件信息
   * 
   * @param params
   * @param accessToken
   * @return
   */
  @GET(value = "/filecenter-service/files")
  public Call<ResponseBean> getFileInfoList(@QueryMap Map<String, String> params,
      @Query("access_token") String accessToken);
}
