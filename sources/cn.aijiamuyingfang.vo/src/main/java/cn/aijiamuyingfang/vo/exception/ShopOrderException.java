package cn.aijiamuyingfang.vo.exception;

import cn.aijiamuyingfang.vo.response.ResponseCode;

/**
 * [描述]:
 * <p>
 * 订单服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:36:47
 */
public class ShopOrderException extends RuntimeException {

  private static final long serialVersionUID = -2742738667468243148L;

  /**
   * 异常码
   */
  private final String code;

  public ShopOrderException(String code) {
    super();
    this.code = code;
  }

  public ShopOrderException(String code, String message) {
    super(message);
    this.code = code;
  }

  public ShopOrderException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public ShopOrderException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public ShopOrderException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
