package cn.aijiamuyingfang.server.rest.controller;

import cn.aijiamuyingfang.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.CouponException;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.exception.ShopCartException;
import cn.aijiamuyingfang.commons.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.commons.domain.exception.UserException;
import cn.aijiamuyingfang.commons.domain.exception.WXServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * [描述]:
 * <p>
 * 异常处理的拦截器
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 13:57:56
 */
@RestControllerAdvice
public class ExceptionController {
  private static final Logger LOGGER = LogManager.getLogger(ExceptionController.class);

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<Exception> handleException(Exception exception) {
    LOGGER.error("Exception", exception);
    ResponseBean<Exception> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<RuntimeException> handleException(RuntimeException exception) {
    LOGGER.error("RuntimeException", exception);
    ResponseBean<RuntimeException> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(GoodsException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<GoodsException> handleException(GoodsException exception) {
    LOGGER.error("GoodsException", exception);
    ResponseBean<GoodsException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<GoodsException> handleException(UserException exception) {
    LOGGER.error("UserException", exception);
    ResponseBean<GoodsException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(ShopCartException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<ShopCartException> handleException(ShopCartException exception) {
    LOGGER.error("ShopCartException", exception);
    ResponseBean<ShopCartException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(ShopOrderException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<ShopOrderException> handleException(ShopOrderException exception) {
    LOGGER.error("ShopOrderException", exception);
    ResponseBean<ShopOrderException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(CouponException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<CouponException> handleException(CouponException exception) {
    LOGGER.error("CouponException", exception);
    ResponseBean<CouponException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(WXServiceException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<WXServiceException> handleException(WXServiceException exception) {
    LOGGER.error("WXServiceException", exception);
    ResponseBean<WXServiceException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(AuthException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<AuthException> handleException(AuthException exception) {
    LOGGER.error("AuthException", exception);
    ResponseBean<AuthException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }
}
