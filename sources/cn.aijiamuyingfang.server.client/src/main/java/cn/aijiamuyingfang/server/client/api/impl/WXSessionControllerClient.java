package cn.aijiamuyingfang.server.client.api.impl;

import cn.aijiamuyingfang.server.client.annotation.HttpService;
import cn.aijiamuyingfang.server.client.api.WXSessionControllerApi;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.WXSession;
import cn.aijiamuyingfang.server.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.domain.exception.WXServiceException;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.http.Query;

/**
 * [描述]:
 * <p>
 * 客户端调用WXSessionController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 02:30:17
 */
@Service
@SuppressWarnings("rawtypes")
public class WXSessionControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(WXSessionControllerClient.class);

  @HttpService
  private WXSessionControllerApi wxsessionControllerApi;

  public WXSession jscode2Session(@Query("jscode") String jscode) throws IOException {
    Response<ResponseBean> response = wxsessionControllerApi.jscode2Session(jscode).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new RuntimeException("response body is null");
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      WXSession wxsession = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), WXSession.class);
      if (null == wxsession) {
        throw new RuntimeException("jscode to wxsession  is '200',.but return data is null");
      }
      return wxsession;
    }
    LOGGER.error(responseBean.getMsg());
    throw new WXServiceException(returnCode, responseBean.getMsg());
  }
}
