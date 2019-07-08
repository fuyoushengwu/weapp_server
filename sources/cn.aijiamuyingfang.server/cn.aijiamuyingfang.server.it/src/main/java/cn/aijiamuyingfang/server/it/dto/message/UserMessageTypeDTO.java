package cn.aijiamuyingfang.server.it.dto.message;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.server.it.dto.BaseEnum;

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
public enum UserMessageTypeDTO implements BaseEnum {

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

  private UserMessageTypeDTO(int value) {
    this.value = value;
  }

  @Override
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
    public Integer convertToDatabaseColumn(UserMessageTypeDTO attribute) {
      return attribute.getValue();
    }

    @Override
    public UserMessageTypeDTO convertToEntityAttribute(Integer dbData) {
      return UserMessageTypeDTO.fromValue(dbData);
    }
  }
}