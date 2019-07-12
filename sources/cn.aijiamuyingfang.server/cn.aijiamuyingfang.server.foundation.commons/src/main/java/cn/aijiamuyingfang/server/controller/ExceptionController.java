package cn.aijiamuyingfang.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.aijiamuyingfang.vo.exception.CouponException;
import cn.aijiamuyingfang.vo.exception.FileCenterException;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.exception.OAuthException;
import cn.aijiamuyingfang.vo.exception.ShopCartException;
import cn.aijiamuyingfang.vo.exception.ShopOrderException;
import cn.aijiamuyingfang.vo.exception.UserException;
import cn.aijiamuyingfang.vo.exception.WeChatServiceException;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ExceptionController {
  @ExceptionHandler(WeChatServiceException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<WeChatServiceException> handleException(WeChatServiceException exception) {
    log.error("WeChatServiceException", exception);
    ResponseBean<WeChatServiceException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(OAuthException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<OAuthException> handleException(OAuthException exception) {
    log.error("OAuthException", exception);
    ResponseBean<OAuthException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(CouponException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<CouponException> handleException(CouponException exception) {
    log.error("CouponException", exception);
    ResponseBean<CouponException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(GoodsException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<GoodsException> handleException(GoodsException exception) {
    log.error("GoodsException", exception);
    ResponseBean<GoodsException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(ShopCartException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<ShopCartException> handleException(ShopCartException exception) {
    log.error("ShopCartException", exception);
    ResponseBean<ShopCartException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(ShopOrderException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<ShopOrderException> handleException(ShopOrderException exception) {
    log.error("ShopOrderException", exception);
    ResponseBean<ShopOrderException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(FileCenterException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<FileCenterException> handleException(FileCenterException exception) {
    log.error("FileCenterException", exception);
    ResponseBean<FileCenterException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<UserException> handleException(UserException exception) {
    log.error("UserException", exception);
    ResponseBean<UserException> responseBean = new ResponseBean<>();
    responseBean.setCode(exception.getCode());
    responseBean.setMsg(exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<AccessDeniedException> handleException(AccessDeniedException exception) {
    log.error("AccessDeniedException", exception);
    ResponseBean<AccessDeniedException> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.ACCESS_DENIED, exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<Exception> handleException(IllegalArgumentException exception) {
    log.error("IllegalArgumentException", exception);
    ResponseBean<Exception> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.BAD_REQUEST, exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<RuntimeException> handleException(RuntimeException exception) {
    log.error("RuntimeException", exception);
    ResponseBean<RuntimeException> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
    return responseBean;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseBean<Exception> handleException(Exception exception) {
    log.error("Exception", exception);
    ResponseBean<Exception> responseBean = new ResponseBean<>();
    responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
    return responseBean;
  }
}
