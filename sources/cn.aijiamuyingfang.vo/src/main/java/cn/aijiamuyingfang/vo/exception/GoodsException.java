package cn.aijiamuyingfang.vo.exception;

import cn.aijiamuyingfang.vo.response.ResponseCode;

/**
 * [描述]:
 * <p>
 * Goods服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:36:47
 */
public class GoodsException extends RuntimeException {
  private static final long serialVersionUID = 3095661455169479600L;

  /**
   * 异常码
   */
  private final String code;

  public GoodsException(String code) {
    super();
    this.code = code;
  }

  public GoodsException(String code, String message) {
    super(message);
    this.code = code;
  }

  public GoodsException(ResponseCode exceptionCode, Object... args) {
    super(exceptionCode.getMessage(args));
    this.code = exceptionCode.getCode();
  }

  public GoodsException(String code, Throwable cause) {
    super(cause);
    this.code = code;
  }

  public GoodsException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
