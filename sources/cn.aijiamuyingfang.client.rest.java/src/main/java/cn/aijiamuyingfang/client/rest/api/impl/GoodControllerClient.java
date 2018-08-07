package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodDetail;
import cn.aijiamuyingfang.commons.domain.goods.ShelfLife;
import cn.aijiamuyingfang.commons.domain.goods.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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

  /**
   * 时间的表达式
   */
  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

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
  private GoodControllerApi goodControllerApi;

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
   * @throws IOException
   */
  public GetClassifyGoodListResponse getClassifyGoodList(String token, String classifyid, List<String> packFilter,
      List<String> levelFilter, String orderType, String orderValue, int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = goodControllerApi
        .getClassifyGoodList(token, classifyid, packFilter, levelFilter, orderType, orderValue, currentpage, pagesize)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetClassifyGoodListResponse getClassifyGoodListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetClassifyGoodListResponse.class);
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
   * @param token
   * @param coverImageFile
   * @param detailImageFiles
   * @param goodRequest
   * @return
   * @throws IOException
   */
  public Good createGood(String token, File coverImageFile, List<File> detailImageFiles, Good goodRequest)
      throws IOException {
    Response<ResponseBean> response;
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      response = goodControllerApi.createGood(token, buildMultipartBody(null, null, goodRequest)).execute();
    } else {
      response = goodControllerApi.createGood(token, buildMultipartBody(coverImageFile, detailImageFiles, goodRequest))
          .execute();
    }
    return getGoodFromResponse(response, "create good return code is '200',but return data is null");
  }

  /**
   * 异步添加商品
   * 
   * @param token
   * @param coverImageFile
   * @param detailImageFiles
   * @param goodRequest
   * @param callback
   */
  public void createGoodAsync(String token, File coverImageFile, List<File> detailImageFiles, Good goodRequest,
      Callback<ResponseBean> callback) {
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      goodControllerApi.createGood(token, buildMultipartBody(null, null, goodRequest)).enqueue(callback);
    } else {
      goodControllerApi.createGood(token, buildMultipartBody(coverImageFile, detailImageFiles, goodRequest))
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
      RequestBody requestCoverImg = RequestBody.create(MediaType.parse("multipart/form-data"), coverImageFile);
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
        RequestBody requestdetailImg = RequestBody.create(MediaType.parse("multipart/form-data"), detailImageFile);
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
    if (StringUtils.hasContent(goodRequest.getVoucherId())) {
      requestBodyBuilder.addFormDataPart("voucherId", goodRequest.getVoucherId());
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
    if (goodRequest.getMarketprice() > 0) {
      requestBodyBuilder.addFormDataPart("marketprice", goodRequest.getMarketprice() + "");
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
      SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
      Date startTime = lifetime.getStart();
      if (startTime != null) {
        requestBodyBuilder.addFormDataPart("lifetime.start", dateFormat.format(startTime));
      }
      Date endTime = lifetime.getEnd();
      if (endTime != null) {
        requestBodyBuilder.addFormDataPart("lifetime.end", dateFormat.format(endTime));
      }
    }
  }

  /**
   * 获取商品信息
   * 
   * @param token
   * @param goodid
   * @return
   * @throws IOException
   */
  public Good getGood(String token, String goodid) throws IOException {
    Response<ResponseBean> response = goodControllerApi.getGood(token, goodid).execute();
    return getGoodFromResponse(response, "get good  return code is '200',but return data is null");
  }

  /**
   * 废弃商品
   * 
   * @param token
   * @param goodid
   * @param async
   * @throws IOException
   */
  public void deprecateGood(String token, String goodid, boolean async) throws IOException {
    if (async) {
      goodControllerApi.deprecateGood(token, goodid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = goodControllerApi.deprecateGood(token, goodid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取商品详细信息
   * 
   * @param token
   * @param goodid
   * @return
   * @throws IOException
   */
  public GoodDetail getGoodDetail(String token, String goodid) throws IOException {
    Response<ResponseBean> response = goodControllerApi.getGoodDetail(token, goodid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param goodid
   * @param request
   * @return
   * @throws IOException
   */
  public Good updateGood(String token, String goodid, Good request) throws IOException {
    Response<ResponseBean> response = goodControllerApi.updateGood(token, goodid, request).execute();
    return getGoodFromResponse(response, "update good  return code is '200',but return data is null");
  }

  /**
   * 从response中获取Good
   * 
   * @param response
   * @param exceptionMsg
   * @return
   */
  private Good getGoodFromResponse(Response<ResponseBean> response, String exceptionMsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
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
   * @param token
   * @param goodid
   * @param request
   * @param callback
   */
  public void updateGoodAsync(String token, String goodid, Good request, Callback<ResponseBean> callback) {
    goodControllerApi.updateGood(token, goodid, request).enqueue(callback);
  }
}