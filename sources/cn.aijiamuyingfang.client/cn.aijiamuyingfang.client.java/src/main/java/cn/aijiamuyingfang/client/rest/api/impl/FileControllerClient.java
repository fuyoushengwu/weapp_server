package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.commons.utils.StringUtils;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.FileControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.vo.ResponseBean;
import cn.aijiamuyingfang.vo.ResponseCode;
import cn.aijiamuyingfang.vo.exception.FileCenterException;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.filecenter.FileInfo;
import cn.aijiamuyingfang.vo.filecenter.PagableFileInfoList;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用FileController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-15 11:16:18
 */
@Service
@SuppressWarnings("rawtypes")
public class FileControllerClient {
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
  private FileControllerApi fileControllerApi;

  public FileInfo upload(File file, String source, String accessToken) throws IOException {
    if (null == file || !file.exists()) {
      throw new IllegalArgumentException("file must be provided and exists");
    }
    Part filePart = buildMultiPart(file);
    Response<ResponseBean> response = fileControllerApi.upload(filePart, source, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new FileCenterException(ResponseCode.FILECENTER_UPLOAD_FAILED, file.getAbsolutePath());
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      FileInfo fileInfo = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), FileInfo.class);
      if (null == fileInfo) {
        throw new GoodsException("500", "upload file success,but return data is null");
      }
      return fileInfo;
    }
    LOGGER.error(responseBean.getMsg());
    throw new FileCenterException(returnCode, responseBean.getMsg());
  }

  private Part buildMultiPart(File file) {
    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    return Part.createFormData("file", file.getName(), requestBody);
  }

  public void delete(String fileId, String accessToken, boolean async) throws IOException {
    if (StringUtils.isEmpty(fileId)) {
      return;
    }
    if (async) {
      fileControllerApi.delete(fileId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = fileControllerApi.delete(fileId, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new FileCenterException(ResponseCode.FILECENTER_DELETE_FAILED, fileId);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new FileCenterException(ResponseCode.FILECENTER_DELETE_FAILED, fileId);
  }

  public PagableFileInfoList getFileInfoList(Map<String, String> params, String accessToken) throws IOException {
    if (null == params) {
      params = new HashMap<>();
    }
    Response<ResponseBean> response = fileControllerApi.getFileInfoList(params, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new FileCenterException(ResponseCode.FILECENTER_GETFILEINFO_FAILED, params);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      PagableFileInfoList getFileInfoListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableFileInfoList.class);
      if (null == getFileInfoListResponse) {
        throw new GoodsException("500", "get fileinfo list  return code is '200',but return data is null");
      }
      return getFileInfoListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new FileCenterException(returnCode, responseBean.getMsg());
  }
}
