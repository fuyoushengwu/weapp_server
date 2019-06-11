package cn.aijiamuyingfang.client.domain.exception;

import cn.aijiamuyingfang.client.commons.domain.ResponseCode;

/**
 * [描述]:
 * <p>
 * 文件服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-20 09:51:04
 */
public class FileCenterException extends RuntimeException {

  private static final long serialVersionUID = -6895913520168909204L;

  /**
   * 异常码
   */
  private final String code;

  public FileCenterException(String code) {
    super();
    this.code = code;
  }

  public FileCenterException(String code, String message) {
    super(message);
    this.code = code;
  }

  public FileCenterException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public FileCenterException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public FileCenterException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
