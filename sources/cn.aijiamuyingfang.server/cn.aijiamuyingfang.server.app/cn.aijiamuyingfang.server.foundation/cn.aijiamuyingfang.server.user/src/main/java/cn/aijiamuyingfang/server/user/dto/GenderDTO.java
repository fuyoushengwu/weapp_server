package cn.aijiamuyingfang.server.user.dto;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

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
@Convert(converter = GenderDTO.GenderConverter.class)
public enum GenderDTO {
  /**
   * 未知
   */
  UNKNOW(0),
  /**
   * 男性
   */
  MALE(1),

  /**
   * 女性
   */
  FEMALE(2);

  private int value;

  private GenderDTO(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static GenderDTO fromValue(int value) {
    for (GenderDTO gender : GenderDTO.values()) {
      if (gender.value == value) {
        return gender;
      }
    }
    return UNKNOW;
  }

  class GenderConverter implements AttributeConverter<GenderDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GenderDTO gender) {
      return gender.value;
    }

    @Override
    public GenderDTO convertToEntityAttribute(Integer value) {
      return GenderDTO.fromValue(value);
    }
  }
}