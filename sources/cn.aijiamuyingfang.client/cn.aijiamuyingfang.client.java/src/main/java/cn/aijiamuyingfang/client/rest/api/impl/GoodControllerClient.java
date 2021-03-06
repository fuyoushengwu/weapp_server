package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.aijiamuyingfang.client.commons.constant.ClientRestConstants;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.client.rest.utils.ResponseUtils;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.goods.GoodDetail;
import cn.aijiamuyingfang.vo.goods.PagableGoodList;
import cn.aijiamuyingfang.vo.goods.ShelfLife;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.JsonUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用GoodController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 23:25:21
 */
@Service
@SuppressWarnings("rawtypes")
public class GoodControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(GoodControllerClient.class);

  private static final Callback<ResponseBean> Empty_Callback = new Callback<ResponseBean>() {

    @Override
    public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
      LOGGER.info("onResponse:{}", response.message());
    }

    @Override
    public void onFailure(Call<ResponseBean> call, Throwable t) {
      LOGGER.error(t.getMessage(), t);
    }
  };

  @HttpService
  private GoodControllerApi goodControllerApi;

  /**
   * 分页查询条目下的商品
   * 
   * @param classifyId
   * @param packFilter
   * @param levelFilter
   * @param orderType
   * @param orderValue
   * @param currentPage
   * @param pageSize
   * @return
   * @throws IOException
   */
  public PagableGoodList getClassifyGoodList(String classifyId, List<String> packFilter, List<String> levelFilter,
      String orderType, String orderValue, int currentPage, int pageSize) throws IOException {
    Response<ResponseBean> response = goodControllerApi
        .getClassifyGoodList(classifyId, packFilter, levelFilter, orderType, orderValue, currentPage, pageSize)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableGoodList getClassifyGoodListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableGoodList.class);
      if (null == getClassifyGoodListResponse) {
        throw new GoodsException("500", "get classify good list  return code is '200',but return data is null");
      }
      return getClassifyGoodListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 添加商品
   * 
   * @param coverImageFile
   * @param detailImageFiles
   * @param goodRequest
   * @param accessToken
   * @return
   * @throws IOException
   */
  public Good createGood(File coverImageFile, List<File> detailImageFiles, Good goodRequest, String accessToken)
      throws IOException {
    Response<ResponseBean> response;
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      response = goodControllerApi.createGood(buildMultipartBody(null, null, goodRequest), accessToken).execute();
    } else {
      response = goodControllerApi
          .createGood(buildMultipartBody(coverImageFile, detailImageFiles, goodRequest), accessToken).execute();
    }
    return getGoodFromResponse(response, "create good return code is '200',but return data is null");
  }

  /**
   * 异步添加商品
   * 
   * @param coverImageFile
   * @param detailImageFiles
   * @param goodRequest
   * @param accessToken
   * @param callback
   */
  public void createGoodAsync(File coverImageFile, List<File> detailImageFiles, Good goodRequest, String accessToken,
      Callback<ResponseBean> callback) {
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      goodControllerApi.createGood(buildMultipartBody(null, null, goodRequest), accessToken).enqueue(callback);
    } else {
      goodControllerApi.createGood(buildMultipartBody(coverImageFile, detailImageFiles, goodRequest), accessToken)
          .enqueue(callback);
    }
  }

  /**
   * 根据coverImageFile、detailImageFiles和goodRequest构建MultipartBody
   * 
   * @param coverImageFile
   * @param detailImageFiles
   * @param goodRequest
   * @return
   */
  private MultipartBody buildMultipartBody(File coverImageFile, List<File> detailImageFiles, Good goodRequest) {
    MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    buildCoverImage(requestBodyBuilder, coverImageFile);
    buildDetailImage(requestBodyBuilder, detailImageFiles);
    buildGoodRequest(requestBodyBuilder, goodRequest);
    return requestBodyBuilder.build();

  }

  /**
   * 为MultipartBody添加coverImage段
   * 
   * @param requestBodyBuilder
   * @param coverImageFile
   */
  private void buildCoverImage(MultipartBody.Builder requestBodyBuilder, File coverImageFile) {
    if (coverImageFile != null) {
      RequestBody requestCoverImg = RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, coverImageFile);
      requestBodyBuilder.addFormDataPart("coverImage", coverImageFile.getName(), requestCoverImg);
    }
  }

  /**
   * 为MultipartBody添加detailImages段
   * 
   * @param requestBodyBuilder
   * @param detailImageFiles
   */
  private void buildDetailImage(Builder requestBodyBuilder, List<File> detailImageFiles) {
    if (!CollectionUtils.isEmpty(detailImageFiles)) {
      for (File detailImageFile : detailImageFiles) {
        RequestBody requestdetailImg = RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, detailImageFile);
        requestBodyBuilder.addFormDataPart("detailImages", detailImageFile.getName(), requestdetailImg);
      }
    }
  }

  /**
   * 将GoodRequest的字段添加到MultipartBody中
   * 
   * @param requestBodyBuilder
   * @param goodRequest
   */
  private void buildGoodRequest(Builder requestBodyBuilder, Good goodRequest) {
    if (null == goodRequest) {
      return;
    }
    buildGoodRequestString(requestBodyBuilder, goodRequest);
    buildGoodRequestNumber(requestBodyBuilder, goodRequest);
    buildLifeTime(requestBodyBuilder, goodRequest.getLifetime());
  }

  /**
   * 将GoodRequest中String类型的字段添加到MultipartBody中
   * 
   * @param requestBodyBuilder
   * @param goodRequest
   */
  private void buildGoodRequestString(Builder requestBodyBuilder, Good goodRequest) {
    if (StringUtils.hasContent(goodRequest.getBarcode())) {
      requestBodyBuilder.addFormDataPart("barcode", goodRequest.getBarcode());
    }
    if (StringUtils.hasContent(goodRequest.getLevel())) {
      requestBodyBuilder.addFormDataPart("level", goodRequest.getLevel());
    }
    if (StringUtils.hasContent(goodRequest.getName())) {
      requestBodyBuilder.addFormDataPart("name", goodRequest.getName());
    }
    if (StringUtils.hasContent(goodRequest.getPack())) {
      requestBodyBuilder.addFormDataPart("pack", goodRequest.getPack());
    }
    if (goodRequest.getGoodVoucher() != null) {
      requestBodyBuilder.addFormDataPart("goodVoucher.voucherId", goodRequest.getGoodVoucher().getId());
    }
  }

  /**
   * 将GoodRequest中Number类型的字段添加到MultipartBody中
   * 
   * @param requestBodyBuilder
   * @param goodRequest
   */
  private void buildGoodRequestNumber(Builder requestBodyBuilder, Good goodRequest) {
    if (goodRequest.getCount() > 0) {
      requestBodyBuilder.addFormDataPart("count", goodRequest.getCount() + "");
    }
    if (goodRequest.getMarketPrice() > 0) {
      requestBodyBuilder.addFormDataPart("marketPrice", goodRequest.getMarketPrice() + "");
    }
    if (goodRequest.getPrice() > 0) {
      requestBodyBuilder.addFormDataPart("price", goodRequest.getPrice() + "");
    }
    if (goodRequest.getSalecount() > 0) {
      requestBodyBuilder.addFormDataPart("salecount", goodRequest.getSalecount() + "");
    }
    if (goodRequest.getScore() > 0) {
      requestBodyBuilder.addFormDataPart("score", goodRequest.getScore() + "");
    }
  }

  /**
   * 为MultipartBody添加lifetime段
   * 
   * @param requestBodyBuilder
   * @param lifetime
   */
  private void buildLifeTime(Builder requestBodyBuilder, ShelfLife lifetime) {
    if (lifetime != null) {
      if (StringUtils.hasContent(lifetime.getStart())) {
        requestBodyBuilder.addFormDataPart("lifetime.start", lifetime.getStart());
      }
      if (StringUtils.hasContent(lifetime.getEnd())) {
        requestBodyBuilder.addFormDataPart("lifetime.end", lifetime.getEnd());
      }
    }
  }

  /**
   * 获取商品信息
   * 
   * @param goodId
   * @return
   * @throws IOException
   */
  public Good getGood(String goodId) throws IOException {
    Response<ResponseBean> response = goodControllerApi.getGood(goodId).execute();
    return getGoodFromResponse(response, "get good  return code is '200',but return data is null");
  }

  /**
   * 废弃商品
   * 
   * @param goodId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deprecateGood(String goodId, String accessToken, boolean async) throws IOException {
    if (async) {
      goodControllerApi.deprecateGood(goodId, accessToken).enqueue(Empty_Callback);
      return;
    }
    ResponseUtils.handleGoodsVOIDResponse(goodControllerApi.deprecateGood(goodId, accessToken).execute(), LOGGER);
  }

  /**
   * 获取商品详细信息
   * 
   * @param goodId
   * @return
   * @throws IOException
   */
  public GoodDetail getGoodDetail(String goodId) throws IOException {
    Response<ResponseBean> response = goodControllerApi.getGoodDetail(goodId).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GoodDetail goodDetail = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GoodDetail.class);
      if (null == goodDetail) {
        throw new GoodsException("500", "get good detail  return code is '200',but return data is null");
      }
      return goodDetail;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 更新Good信息
   * 
   * @param goodId
   * @param request
   * @param accessToken
   * @return
   * @throws IOException
   */
  public Good updateGood(String goodId, Good request, String accessToken) throws IOException {
    Response<ResponseBean> response = goodControllerApi.updateGood(goodId, request, accessToken).execute();
    return getGoodFromResponse(response, "update good  return code is '200',but return data is null");
  }

  /**
   * 从response中获取Good
   * 
   * @param response
   * @param exceptionMsg
   * @return
   * @throws IOException
   */
  private Good getGoodFromResponse(Response<ResponseBean> response, String exceptionMsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      Good good = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), Good.class);
      if (null == good) {
        throw new GoodsException("500", exceptionMsg);
      }
      return good;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步更新Good信息
   * 
   * @param goodId
   * @param request
   * @param accessToken
   * @param callback
   */
  public void updateGoodAsync(String goodId, Good request, String accessToken, Callback<ResponseBean> callback) {
    goodControllerApi.updateGood(goodId, request, accessToken).enqueue(callback);
  }
}
