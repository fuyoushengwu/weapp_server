package cn.aijiamuyingfang.client.rest.api.impl;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.StoreControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.commons.domain.address.City;
import cn.aijiamuyingfang.commons.domain.address.Coordinate;
import cn.aijiamuyingfang.commons.domain.address.County;
import cn.aijiamuyingfang.commons.domain.address.Province;
import cn.aijiamuyingfang.commons.domain.address.StoreAddress;
import cn.aijiamuyingfang.commons.domain.address.Town;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.domain.goods.WorkTime;
import cn.aijiamuyingfang.commons.domain.goods.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.commons.domain.goods.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.io.File;
import java.io.IOException;
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
 * 客户端调用StoreController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 16:19:31
 */
@Service
@SuppressWarnings("rawtypes")
public class StoreControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(StoreControllerClient.class);

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
  private StoreControllerApi storeControllerApi;

  /**
   * 分页获取在使用中的Store
   * 
   * @param token
   * @param currentpage
   *          当前页 默认值:1 (currentpage必须&ge;1,否则重置为1)
   * @param pagesize
   *          每页大小 默认值:10(pagesize必须&gt;0,否则重置为1)
   * @return
   * @throws IOException
   */
  public GetInUseStoreListResponse getInUseStoreList(String token, int currentpage, int pagesize) throws IOException {
    Response<ResponseBean> response = storeControllerApi.getInUseStoreList(token, currentpage, pagesize).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetInUseStoreListResponse getInUseStoreListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetInUseStoreListResponse.class);
      if (null == getInUseStoreListResponse) {
        throw new GoodsException("500", "get inuse store list  return code is '200',but return data is null");
      }
      return getInUseStoreListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 创建门店
   * 
   * @param token
   * @param storeRequest
   * @return
   * @throws IOException
   */
  public Store createStore(String token, File coverImageFile, List<File> detailImageFiles, Store storeRequest)
      throws IOException {
    Response<ResponseBean> response;
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      response = storeControllerApi.createStore(token, buildMultipartBody(null, null, storeRequest)).execute();
    } else {
      response = storeControllerApi
          .createStore(token, buildMultipartBody(coverImageFile, detailImageFiles, storeRequest)).execute();
    }
    return getStoreFromResponse(response, "create store return code is '200',but return data is null");
  }

  private Store getStoreFromResponse(Response<ResponseBean> response, String errormsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      Store store = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), Store.class);
      if (null == store) {
        throw new GoodsException("500", errormsg);
      }
      return store;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步创建门店
   * 
   * @param token
   * @param coverImageFile
   * @param detailImageFiles
   * @param storeRequest
   * @param callback
   */
  public void createStoreAsync(String token, File coverImageFile, List<File> detailImageFiles, Store storeRequest,
      Callback<ResponseBean> callback) {
    if (null == coverImageFile && CollectionUtils.isEmpty(detailImageFiles)) {
      storeControllerApi.createStore(token, buildMultipartBody(null, null, storeRequest)).enqueue(callback);
    } else {
      storeControllerApi.createStore(token, buildMultipartBody(coverImageFile, detailImageFiles, storeRequest))
          .enqueue(callback);
    }
  }

  /**
   * 根据coverImageFile、detailImageFiles和storeRequest构建MultipartBody
   * 
   * @param coverImageFile
   * @param detailImageFiles
   * @param storeRequest
   * @return
   */
  private MultipartBody buildMultipartBody(File coverImageFile, List<File> detailImageFiles, Store storeRequest) {
    MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    buildCoverImage(requestBodyBuilder, coverImageFile);
    buildDetailImage(requestBodyBuilder, detailImageFiles);
    buildStoreRequest(requestBodyBuilder, storeRequest);
    return requestBodyBuilder.build();

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
   * 将Store添加到MultipartBody
   * 
   * @param requestBodyBuilder
   * @param storeRequest
   */
  private void buildStoreRequest(Builder requestBodyBuilder, Store storeRequest) {
    if (null == storeRequest) {
      return;
    }
    if (StringUtils.hasContent(storeRequest.getName())) {
      requestBodyBuilder.addFormDataPart("name", storeRequest.getName());
    }

    buildWorkTime(requestBodyBuilder, storeRequest.getWorkTime());
    buildStoreAddress(requestBodyBuilder, storeRequest.getStoreAddress());

  }

  /**
   * 为MultipartBody添加workTime字段
   * 
   * @param requestBodyBuilder
   * @param workTime
   */
  private void buildWorkTime(Builder requestBodyBuilder, WorkTime workTime) {
    if (workTime != null) {
      if (StringUtils.hasContent(workTime.getStart())) {
        requestBodyBuilder.addFormDataPart("workTime.start", workTime.getStart());
      }
      if (StringUtils.hasContent(workTime.getEnd())) {
        requestBodyBuilder.addFormDataPart("workTime.end", workTime.getEnd());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress字段
   * 
   * @param requestBodyBuilder
   * @param storeaddress
   */
  private void buildStoreAddress(Builder requestBodyBuilder, StoreAddress storeaddress) {
    if (storeaddress != null) {
      buildProvince(requestBodyBuilder, storeaddress.getProvince());
      buildCity(requestBodyBuilder, storeaddress.getCity());
      buildCounty(requestBodyBuilder, storeaddress.getCounty());
      buildTown(requestBodyBuilder, storeaddress.getTown());
      buildCoordinate(requestBodyBuilder, storeaddress.getCoordinate());

      if (StringUtils.hasContent(storeaddress.getContactor())) {
        requestBodyBuilder.addFormDataPart("storeAddress.contactor", storeaddress.getContactor());
      }
      if (StringUtils.hasContent(storeaddress.getDetail())) {
        requestBodyBuilder.addFormDataPart("storeAddress.detail", storeaddress.getDetail());
      }
      if (StringUtils.hasContent(storeaddress.getPhone())) {
        requestBodyBuilder.addFormDataPart("storeAddress.phone", storeaddress.getPhone());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress.province字段
   * 
   * @param requestBodyBuilder
   * @param province
   */
  private void buildProvince(Builder requestBodyBuilder, Province province) {
    if (province != null) {
      if (StringUtils.hasContent(province.getName())) {
        requestBodyBuilder.addFormDataPart("storeAddress.province.name", province.getName());
      }
      if (StringUtils.hasContent(province.getCode())) {
        requestBodyBuilder.addFormDataPart("storeAddress.province.code", province.getCode());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress.city字段
   * 
   * @param requestBodyBuilder
   * @param city
   */
  private void buildCity(Builder requestBodyBuilder, City city) {
    if (city != null) {
      if (StringUtils.hasContent(city.getName())) {
        requestBodyBuilder.addFormDataPart("storeAddress.city.name", city.getName());
      }
      if (StringUtils.hasContent(city.getCode())) {
        requestBodyBuilder.addFormDataPart("storeAddress.city.code", city.getCode());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress.county字段
   * 
   * @param requestBodyBuilder
   * @param county
   */
  private void buildCounty(Builder requestBodyBuilder, County county) {
    if (county != null) {
      if (StringUtils.hasContent(county.getName())) {
        requestBodyBuilder.addFormDataPart("storeAddress.county.name", county.getName());
      }
      if (StringUtils.hasContent(county.getCode())) {
        requestBodyBuilder.addFormDataPart("storeAddress.county.code", county.getCode());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress.town字段
   * 
   * @param requestBodyBuilder
   * @param town
   */
  private void buildTown(Builder requestBodyBuilder, Town town) {
    if (town != null) {
      if (StringUtils.hasContent(town.getName())) {
        requestBodyBuilder.addFormDataPart("storeAddress.town.name", town.getName());
      }
      if (StringUtils.hasContent(town.getCode())) {
        requestBodyBuilder.addFormDataPart("storeAddress.town.code", town.getCode());
      }
    }
  }

  /**
   * 为MultipartBody添加storeAddress.coordinate字段
   * 
   * @param requestBodyBuilder
   * @param coordinate
   */
  private void buildCoordinate(Builder requestBodyBuilder, Coordinate coordinate) {
    if (coordinate != null) {
      requestBodyBuilder.addFormDataPart("storeAddress.coordinate.longitude", coordinate.getLongitude() + "");
      requestBodyBuilder.addFormDataPart("storeAddress.coordinate.latitude", coordinate.getLatitude() + "");
    }
  }

  /**
   * 获取门店信息
   * 
   * @param token
   * @param storeid
   * @return
   * @throws IOException
   */
  public Store getStore(String token, String storeid) throws IOException {
    Response<ResponseBean> response = storeControllerApi.getStore(token, storeid).execute();
    return getStoreFromResponse(response, "get store return code is '200',but return data is null");
  }

  /**
   * 更新门店信息
   * 
   * @param token
   * @param storeid
   * @param storeRequest
   * @return
   * @throws IOException
   */
  public Store updateStore(String token, String storeid, Store storeRequest) throws IOException {
    Response<ResponseBean> response = storeControllerApi.updateStore(token, storeid, storeRequest).execute();
    return getStoreFromResponse(response, "update store return code is '200',but return data is null");
  }

  /**
   * 异步更新门店信息
   * 
   * @param token
   * @param storeid
   * @param storeRequest
   * @param callback
   */
  public void updateStoreAsync(String token, String storeid, Store storeRequest, Callback<ResponseBean> callback) {
    storeControllerApi.updateStore(token, storeid, storeRequest).enqueue(callback);
  }

  /**
   * 废弃门店
   * 
   * @param token
   * @param storeid
   * @param async
   * @throws IOException
   */
  public void deprecateStore(String token, String storeid, boolean async) throws IOException {
    if (async) {
      storeControllerApi.deprecateStore(token, storeid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = storeControllerApi.deprecateStore(token, storeid).execute();
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
   * 获取默认门店Id
   * 
   * @param token
   * @return
   * @throws IOException
   */
  public GetDefaultStoreIdResponse getDefaultStoreId(String token) throws IOException {
    Response<ResponseBean> response = storeControllerApi.getDefaultStoreId(token).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      return JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetDefaultStoreIdResponse.class);
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 获取在哪些城市有门店分布
   * 
   * @param token
   * @return
   * @throws IOException
   */
  public List<String> getStoresCity(String token) throws IOException {
    Response<ResponseBean> response = storeControllerApi.getStoresCity(token).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      List<String> cities = JsonUtils.json2List(JsonUtils.list2Json((List<?>) returnData), String.class);
      if (null == cities) {
        throw new GoodsException("500", "get stores city  return code is '200',but return data is null");
      }
      return cities;
    }
    LOGGER.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 门店下添加条目
   * 
   * 
   * @param token
   * @param storeid
   * @param classifyid
   * @param async
   * @throws IOException
   */
  // public void addStoreClassify(String token, String storeid, String classifyid, boolean async)
  // throws IOException {
  // if (async) {
  // storeControllerApi.addStoreClassify(token, storeid, classifyid).enqueue(Empty_Callback);
  // return;
  // }
  // Response<ResponseBean> response = storeControllerApi.addStoreClassify(token, storeid,
  // classifyid).execute();
  // ResponseBean responseBean = response.body();
  // if (null == responseBean) {
  // throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
  // }
  // String returnCode = responseBean.getCode();
  // if ("200".equals(returnCode)) {
  // return;
  // }
  // LOGGER.error(responseBean.getMsg());
  // throw new GoodsException(returnCode, responseBean.getMsg());
  // }
}
