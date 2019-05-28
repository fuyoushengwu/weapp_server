package cn.aijiamuyingfang.server.it.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import org.springframework.security.core.GrantedAuthority;

import com.google.gson.annotations.SerializedName;

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
@Convert(converter = UserAuthority.UserAuthorityConverter.class)
public enum UserAuthority implements GrantedAuthority, BaseEnum {

  /**
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW("permission:anymouse", 0),
  /**
   * 管理员的所有权限
   */
  @SerializedName("1")
  MANAGER_PERMISSION("permission:manager:*", 1),
  /**
   * 送货员的所有权限
   */
  @SerializedName("2")
  SENDER_PERMISSION("permission:sender:*", 2),
  /**
   * 顾客的所有权限
   */
  @SerializedName("3")
  BUYER_PERMISSION("permission:buyer:*", 3);
  /**
   * 权限名
   */
  private String authority;

  /**
   * 权限值
   */
  private int value;

  UserAuthority(String authority, int value) {
    this.authority = authority;
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }

  public static UserAuthority fromValue(int value) {
    for (UserAuthority type : UserAuthority.values()) {
      if (type.getValue() == value) {
        return type;
      }
    }
    return UNKNOW;
  }

  class UserAuthorityConverter implements AttributeConverter<UserAuthority, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserAuthority attribute) {
      return attribute.getValue();
    }

    @Override
    public UserAuthority convertToEntityAttribute(Integer dbData) {
      return UserAuthority.fromValue(dbData);
    }
  }
}
