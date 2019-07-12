package cn.aijiamuyingfang.server.user.dto;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * [描述]:
 * <p>
 * 用户权限
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-07 00:54:17
 */
@Convert(converter = UserAuthorityDTO.UserAuthorityConverter.class)
public enum UserAuthorityDTO {

  /**
   * 未知类型
   */
  UNKNOW(0),
  /**
   * 管理员的所有权限
   */
  MANAGER_PERMISSION(1),
  /**
   * 送货员的所有权限
   */
  SENDER_PERMISSION(2),
  /**
   * 顾客的所有权限
   */
  BUYER_PERMISSION(3);

  /**
   * 权限值
   */
  private int value;

  UserAuthorityDTO(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static UserAuthorityDTO fromValue(int value) {
    for (UserAuthorityDTO authority : UserAuthorityDTO.values()) {
      if (authority.value == value) {
        return authority;
      }
    }
    return UNKNOW;
  }

  class UserAuthorityConverter implements AttributeConverter<UserAuthorityDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserAuthorityDTO authority) {
      return authority.value;
    }

    @Override
    public UserAuthorityDTO convertToEntityAttribute(Integer value) {
      return UserAuthorityDTO.fromValue(value);
    }
  }
}
