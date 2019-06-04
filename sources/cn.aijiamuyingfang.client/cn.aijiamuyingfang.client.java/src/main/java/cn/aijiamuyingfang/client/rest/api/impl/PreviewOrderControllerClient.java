package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrder;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrderItem;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.PreviewOrderControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用 PreviewOrderController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 17:17:01
 */
@Service
@SuppressWarnings("rawtypes")
public class PreviewOrderControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(PreviewOrderControllerClient.class);

  private static final Callback<ResponseBean> Empty_Callback = new Callback<ResponseBean>() {

    @Override
    public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
      LOGGER.info("onResponse:" + response.message());
    }

    @Override
    public void onFailure(Call<ResponseBean> call, Throwable t) {
      LOGGER.error(t.getMessage(), t);
    }
  };

  @HttpService
  private PreviewOrderControllerApi previeworderControllerApi;

  /**
   * 更新预览的商品项
   * 
   * @param userId
   * @param previewItemId
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PreviewOrderItem updatePreviewOrderItem(String userId, String previewItemId, PreviewOrderItem request,
      String accessToken) throws IOException {
    Response<ResponseBean> response = previeworderControllerApi
        .updatePreviewOrderItem(userId, previewItemId, request, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PreviewOrderItem previeworderItem = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PreviewOrderItem.class);
      if (null == previeworderItem) {
        throw new ShopOrderException("500", "update preview order item return code is '200',but return data is null");
      }
      return previeworderItem;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步更新预览的商品项
   * 
   * @param userId
   * @param previewItemId
   * @param request
   * @param accessToken
   * @param callback
   */
  public void updatePreviewOrderItem(String userId, String previewItemId, PreviewOrderItem request, String accessToken,
      Callback<ResponseBean> callback) {
    previeworderControllerApi.updatePreviewOrderItem(userId, previewItemId, request, accessToken).enqueue(callback);
  }

  /**
   * 删除预览的商品项
   * 
   * @param userId
   * @param previewItemId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deletePreviewOrderItem(String userId, String previewItemId, String accessToken, boolean async)
      throws IOException {
    if (async) {
      previeworderControllerApi.deletePreviewOrderItem(userId, previewItemId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = previeworderControllerApi
        .deletePreviewOrderItem(userId, previewItemId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 生成用户的预览订单
   * 
   * @param userId
   * @param goodIdList
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PreviewOrder generatePreviewOrder(String userId, List<String> goodIdList, String accessToken) throws IOException {
    Response<
        ResponseBean> response = previeworderControllerApi.generatePreviewOrder(userId, goodIdList, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PreviewOrder previeworder = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), PreviewOrder.class);
      if (null == previeworder) {
        throw new ShopOrderException("500", "generate preview order return code is '200',but return data is null");
      }
      return previeworder;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }
}
