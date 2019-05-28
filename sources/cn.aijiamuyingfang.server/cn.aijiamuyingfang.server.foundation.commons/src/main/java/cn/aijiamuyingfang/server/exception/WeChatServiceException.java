package cn.aijiamuyingfang.server.exception;

import cn.aijiamuyingfang.server.domain.response.ResponseCode;

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
public class WeChatServiceException extends RuntimeException {

  private static final long serialVersionUID = 7441822734883574232L;

  /**
   * 异常码
   */
  private final String code;

  public WeChatServiceException(String code) {
    super();
    this.code = code;
  }

  public WeChatServiceException(String code, String message) {
    super(message);
    this.code = code;
  }

  public WeChatServiceException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public WeChatServiceException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public WeChatServiceException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
