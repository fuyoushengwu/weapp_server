package cn.aijiamuyingfang.server.it.data;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.commons.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.exception.GoodsException;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.server.it.data.qinsilk.GetGoodListRequest;
import cn.aijiamuyingfang.server.it.data.qinsilk.Good;
import cn.aijiamuyingfang.server.it.data.qinsilk.GoodImage;
import cn.aijiamuyingfang.server.it.data.qinsilk.ResponseBean;
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

  private static String cookie = "lastAutoLoginCheck=false; lastUser=19852755906; JSESSIONID=CF1ED14067F11075D5B7A793C00F1D01; qs_sys=gis; qs_cid=323774; qs_uid=465443; Hm_lvt_bf3296661a119dc2e4c3427b339b6d9e=1561237323,1561864899,1561894792; Hm_lvt_dde6ba2851f3db0ddc415ce0f895822e=1561237442,1561894832; GISSessionSID=\"<SNAID>6D8207577CA18640794E8511529E36DA</SNAID>\"; MMSSessionSID=\"<SNAID>EA5CD18AB130F10706A880E0AAB95800</SNAID>\"; Hm_lvt_dde6ba2851f3db0ddc415ce0f895822e=1561237442,1561894832; Hm_lpvt_dde6ba2851f3db0ddc415ce0f895822e=1562009476; gisLoginId=68d91a40-5dcc-4d0c-9160-bfea5eec9633";

  @HttpService
  private QinSilkControllerApi qinSilkControllerApi;

  @SuppressWarnings("unchecked")
  public ResponseBean<Good> getGoodList(long categoryId) throws IOException {
    GetGoodListRequest request = new GetGoodListRequest();
    request.setCategoryIdsStr(categoryId);
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
