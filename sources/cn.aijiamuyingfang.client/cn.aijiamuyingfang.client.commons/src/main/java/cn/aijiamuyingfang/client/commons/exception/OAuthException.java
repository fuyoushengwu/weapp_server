package cn.aijiamuyingfang.client.commons.exception;

import cn.aijiamuyingfang.client.commons.domain.ResponseCode;

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
public class OAuthException extends RuntimeException {

  private static final long serialVersionUID = 7441822734883574232L;

  /**
   * 异常码
   */
  private final String code;

  public OAuthException(String code) {
    super();
    this.code = code;
  }

  public OAuthException(String code, String message) {
    super(message);
    this.code = code;
  }

  public OAuthException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public OAuthException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public OAuthException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
