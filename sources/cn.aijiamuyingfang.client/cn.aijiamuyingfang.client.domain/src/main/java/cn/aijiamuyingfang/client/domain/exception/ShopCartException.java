package cn.aijiamuyingfang.client.domain.exception;

import cn.aijiamuyingfang.client.domain.ResponseCode;

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
public class ShopCartException extends RuntimeException {

  private static final long serialVersionUID = -7489281576267169094L;

  /**
   * 异常码
   */
  private final String code;

  public ShopCartException(String code) {
    super();
    this.code = code;
  }

  public ShopCartException(String code, String message) {
    super(message);
    this.code = code;
  }

  public ShopCartException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public ShopCartException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public ShopCartException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
