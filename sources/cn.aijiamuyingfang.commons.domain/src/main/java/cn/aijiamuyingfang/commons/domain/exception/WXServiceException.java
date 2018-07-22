package cn.aijiamuyingfang.commons.domain.exception;

import cn.aijiamuyingfang.commons.controller.bean.ResponseCode;

/**
 * [描述]:
 * <p>
 * WXService服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 18:15:42
 */
public class WXServiceException extends RuntimeException {

  private static final long serialVersionUID = 7441822734883574232L;

  /**
   * 异常码
   */
  private final String code;

  public WXServiceException(String code) {
    super();
    this.code = code;
  }

  public WXServiceException(String code, String message) {
    super(message);
    this.code = code;
  }

  public WXServiceException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public WXServiceException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public WXServiceException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
