package cn.aijiamuyingfang.client.rest.utils;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import cn.aijiamuyingfang.vo.exception.CouponException;
import cn.aijiamuyingfang.vo.exception.FileCenterException;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.exception.ShopCartException;
import cn.aijiamuyingfang.vo.exception.ShopOrderException;
import cn.aijiamuyingfang.vo.exception.UserException;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import lombok.experimental.UtilityClass;
import retrofit2.Response;

@UtilityClass
public class ResponseUtils {
  /**
   * 处理shopcart-service没有返回数据的response
   * 
   * @param response
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleShopCartVOIDResponse(Response<ResponseBean> response, Logger logger) throws IOException {
    ResponseBean<?> responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 处理goods-service没有返回数据的response
   * 
   * @param response
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleGoodsVOIDResponse(Response<ResponseBean> response, Logger logger) throws IOException {
    ResponseBean<?> responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new GoodsException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new GoodsException(returnCode, responseBean.getMsg());
  }

  /**
   * 处理user-service没有返回数据的response
   * 
   * @param response
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleUserVOIDResponse(Response<ResponseBean> response, Logger logger) throws IOException {
    ResponseBean<?> responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new UserException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new UserException(returnCode, responseBean.getMsg());
  }

  /**
   * 处理shoporder-service没有返回数据的response
   * 
   * @param response
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleShopOrderVOIDResponse(Response<ResponseBean> response, Logger logger) throws IOException {
    ResponseBean<?> responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new ShopOrderException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new ShopOrderException(returnCode, responseBean.getMsg());
  }

  /**
   * 处理coupon-service没有返回数据的response
   * 
   * @param response
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleCouponVOIDResponse(Response<ResponseBean> response, Logger logger) throws IOException {
    ResponseBean<?> responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new CouponException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new CouponException(returnCode, responseBean.getMsg());
  }

  /**
   * 处理filecenter-service没有返回数据的response
   * 
   * @param response
   * @param fileId
   * @param logger
   * @throws IOException
   */
  @SuppressWarnings("rawtypes")
  public static void handleFileCenterVOIDResponse(Response<ResponseBean> response, String fileId, Logger logger)
      throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        logger.error(new String(response.errorBody().bytes()));
      }
      throw new FileCenterException(ResponseCode.FILECENTER_DELETE_FAILED, fileId);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    logger.error(responseBean.getMsg());
    throw new FileCenterException(ResponseCode.FILECENTER_DELETE_FAILED, fileId);
  }

}
