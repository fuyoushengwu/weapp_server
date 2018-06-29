package cn.aijiamuyingfang.server.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.aijiamuyingfang.server.rest.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.rest.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.rest.exception.GoodsException;
import cn.aijiamuyingfang.server.rest.exception.WXServiceException;

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
	public ResponseBean<Exception> handleException(Exception exception) {
		LOGGER.error("Exception", exception);
		ResponseBean<Exception> responseBean = new ResponseBean<>();
		responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
		responseBean.setData(exception);
		return responseBean;
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseBean<RuntimeException> handleException(RuntimeException exception) {
		LOGGER.error("RuntimeException", exception);
		ResponseBean<RuntimeException> responseBean = new ResponseBean<>();
		responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
		responseBean.setData(exception);
		return responseBean;
	}

	@ExceptionHandler(GoodsException.class)
	public ResponseBean<GoodsException> handleException(GoodsException exception) {
		LOGGER.error("GoodsException", exception);
		ResponseBean<GoodsException> responseBean = new ResponseBean<>();
		responseBean.setCode(exception.getCode());
		responseBean.setMsg(exception.getMessage());
		return responseBean;
	}

	@ExceptionHandler(WXServiceException.class)
	public ResponseBean<WXServiceException> handleException(WXServiceException exception) {
		LOGGER.error("WXServiceException", exception);
		ResponseBean<WXServiceException> responseBean = new ResponseBean<>();
		responseBean.setResponseCode(ResponseCode.RUNTIME_EXCEPTION, exception.getMessage());
		responseBean.setData(exception);
		return responseBean;
	}
}
