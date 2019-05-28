package cn.aijiamuyingfang.client.domain.message;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.client.domain.BaseEnum;

/**
 * [描述]:
 * <p>
 * 用户消息类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 17:10:30
 */
public enum UserMessageType implements BaseEnum {

  /**
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW(0),
  /**
   * 通知
   */
  @SerializedName("1")
  NOTICE(1),

  /**
   * 链接
   */
  @SerializedName("2")
  LINK(2);

  private int value;

  private UserMessageType(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static UserMessageType fromValue(int value) {
    for (UserMessageType type : UserMessageType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOW;
  }
}