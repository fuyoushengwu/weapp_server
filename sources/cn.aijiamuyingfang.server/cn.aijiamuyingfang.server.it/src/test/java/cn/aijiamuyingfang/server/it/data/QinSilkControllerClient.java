package cn.aijiamuyingfang.server.it.data;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.server.it.data.qinsilk.GetGoodListRequest;
import cn.aijiamuyingfang.server.it.data.qinsilk.Good;
import cn.aijiamuyingfang.server.it.data.qinsilk.GoodImage;
import cn.aijiamuyingfang.server.it.data.qinsilk.ResponseBean;
import cn.aijiamuyingfang.vo.ResponseCode;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 从秦丝导入数据
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 16:38:37
 */
@Service
@SuppressWarnings("rawtypes")
@Slf4j
public class QinSilkControllerClient {

  private static String cookie = "lastAutoLoginCheck=false; lastUser=19852755906; JSESSIONID=B9E4C3BA5EE38422EEC73302B8738F6B; qs_sys=gis; qs_cid=323774; qs_uid=465443; Hm_lvt_bf3296661a119dc2e4c3427b339b6d9e=1561237323,1561864899,1561894792; Hm_lvt_dde6ba2851f3db0ddc415ce0f895822e=1561237442,1561894832; GISSessionSID=\"<SNAID>3950015D0C3FB06763A2980217C0C27B</SNAID>\"; gisLoginId=e4b8879b-b3ef-41b2-9bbc-411d966e39a0; MMSSessionSID=\"<SNAID>CE197D8E3AE9F3BCE886680713F50505</SNAID>\"; Hm_lvt_dde6ba2851f3db0ddc415ce0f895822e=1561237442,1561894832,1562096925; Hm_lpvt_dde6ba2851f3db0ddc415ce0f895822e=1562096925";

  @HttpService
  private QinSilkControllerApi qinSilkControllerApi;

  @SuppressWarnings("unchecked")
  public ResponseBean<Good> getGoodList(long categoryId, int page) throws IOException {
    GetGoodListRequest request = new GetGoodListRequest();
    request.setCategoryIdsStr(categoryId);
    request.setPage(page);
    Response<ResponseBean> response = qinSilkControllerApi
        .getGoodList(JsonUtils.json2Map(JsonUtils.bean2Json(request), String.class, String.class), cookie).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        log.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }

    responseBean.setRows(JsonUtils.json2List(JsonUtils.list2Json(responseBean.getRows()), Good.class));
    return responseBean;
  }

  public List<GoodImage> getGoodImages(long goodId) throws IOException {
    Response<ResponseBean> response = qinSilkControllerApi.getGoodImages(goodId, cookie).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        log.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }

    return JsonUtils.json2List(JsonUtils.list2Json(responseBean.getRows()), GoodImage.class);
  }

}
