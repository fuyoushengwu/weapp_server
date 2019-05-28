package cn.aijiamuyingfang.client.domain.user;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.client.domain.BaseEnum;

/**
 * [描述]:
 * <p>
 * 用户性别
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 16:38:23
 */
public enum Gender implements BaseEnum {
  /**
   * 未知
   */
  @SerializedName("0")
  UNKNOW(0),
  /**
   * 男性
   */
  @SerializedName("1")
  MALE(1),

  /**
   * 女性
   */
  @SerializedName("2")
  FEMALE(2);

  private int value;

  private Gender(int value) {
    this.value = value;
  }

  public static Gender fromValue(int value) {
    for (Gender type : Gender.values()) {
      if (type.value == value) {
        return type;
      }
    }
    return UNKNOW;
  }

  @Override
  public int getValue() {
    return value;
  }
}