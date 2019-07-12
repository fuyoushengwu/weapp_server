package cn.aijiamuyingfang.server.user.dto;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

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
@Convert(converter = UserMessageTypeDTO.UserMessageTypeConverter.class)
public enum UserMessageTypeDTO {

  /**
   * 未知类型
   */
  UNKNOW(0),
  /**
   * 通知
   */
  NOTICE(1),

  /**
   * 链接
   */
  LINK(2);

  private int value;

  private UserMessageTypeDTO(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static UserMessageTypeDTO fromValue(int value) {
    for (UserMessageTypeDTO type : UserMessageTypeDTO.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOW;
  }

  class UserMessageTypeConverter implements AttributeConverter<UserMessageTypeDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserMessageTypeDTO type) {
      return type.value;
    }

    @Override
    public UserMessageTypeDTO convertToEntityAttribute(Integer value) {
      return UserMessageTypeDTO.fromValue(value);
    }
  }
}