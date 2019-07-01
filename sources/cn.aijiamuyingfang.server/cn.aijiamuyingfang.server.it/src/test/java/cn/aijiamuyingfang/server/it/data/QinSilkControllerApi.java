package cn.aijiamuyingfang.server.it.data;

import java.util.Map;

import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.server.it.data.qinsilk.ResponseBean;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
@HttpApi(baseurl = "https://web.syt.qinsilk.com")
@SuppressWarnings("rawtypes")
public interface QinSilkControllerApi {
  @FormUrlEncoded
  @POST(value = "/gis/admin/inner/goods/goodsListJSON.ac")
  Call<ResponseBean> getGoodList(@FieldMap Map<String, String> request, @Header("cookie") String cookie);

  @POST(value = "gis/admin/inner/goods/album/goodsImgs.ac")
  Call<ResponseBean> getGoodImages(@Query("goodsId") long goodId, @Header("cookie") String cookie);
}
