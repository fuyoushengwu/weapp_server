package cn.aijiamuyingfang.client.domain.exception;

import cn.aijiamuyingfang.client.commons.domain.ResponseCode;

/**
 * [描述]:
 * <p>
 * 购物车服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:36:47
 */
public class CouponException extends RuntimeException {

  private static final long serialVersionUID = 1827722967403861591L;

  /**
   * 异常码
   */
  private final String code;

  public CouponException(String code) {
    super();
    this.code = code;
  }

  public CouponException(String code, String message) {
    super(message);
    this.code = code;
  }

  public CouponException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public CouponException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public CouponException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
