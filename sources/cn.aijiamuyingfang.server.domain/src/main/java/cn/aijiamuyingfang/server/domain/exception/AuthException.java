package cn.aijiamuyingfang.server.domain.exception;

import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;

/**
 * [描述]:
 * <p>
 * 鉴权失败的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 18:15:42
 */
public class AuthException extends RuntimeException {

  private static final long serialVersionUID = 7441822734883574232L;

  /**
   * 异常码
   */
  private final String code;

  public AuthException(String code) {
    super();
    this.code = code;
  }

  public AuthException(String code, String message) {
    super(message);
    this.code = code;
  }

  public AuthException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public AuthException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public AuthException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
