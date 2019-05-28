package cn.aijiamuyingfang.server.user.controller;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;

/**
 * [描述]:
 * <p>
 * RestControll服务的拦截器
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 02:29:16
 */
@RestControllerAdvice
public class ResponseController implements ResponseBodyAdvice<Object> {
  /**
   * 返回结果中保存status值的key
   */
  private static final String STATUS_KEY = "status";

  /**
   * 返回结果中保存message值得key
   */
  private static final String MESSAGE_KEY = "message";

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return !ResponseBean.class.isAssignableFrom(returnType.getParameterType());
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof ResponseBean) {
      return body;
    }
    if (body instanceof Resource) {
      return body;
    }

    ResponseBean<Object> responseBean = new ResponseBean<>();
    if (body instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> bodyMap = (Map<String, Object>) body;
      if (bodyMap.containsKey(STATUS_KEY) && bodyMap.get(STATUS_KEY) instanceof Integer) {
        responseBean.setCode(bodyMap.get(STATUS_KEY).toString());
        Object msg = bodyMap.get(MESSAGE_KEY);
        responseBean.setMsg(msg != null ? msg.toString() : "");
        responseBean.setData(body);
        return responseBean;
      }
    }
    responseBean.setResponseCode(ResponseCode.OK);
    responseBean.setData(body);
    return responseBean;
  }
}