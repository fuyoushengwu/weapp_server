package cn.aijiamuyingfang.vo.response;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 返回给服务调用者的类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 05:00:43
 */
@Data
public class ResponseBean<T> {
  /**
   * 返回码
   */
  private String code;

  /**
   * 返回的消息
   */
  private String msg;

  /**
   * 返回的数据
   */
  private T data;

  public void setResponseCode(ResponseCode responseCode, Object... args) {
    if (responseCode != null) {
      this.code = responseCode.getCode();
      this.msg = responseCode.getMessage(args);
    }
  }
}
