package cn.aijiamuyingfang.server.it.dto.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.server.it.dto.BaseEnum;

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
public enum GenderDTO implements BaseEnum {
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

  private GenderDTO(int value) {
    this.value = value;
  }

  public static GenderDTO fromValue(int value) {
    for (GenderDTO type : GenderDTO.values()) {
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

  class GenderConverter implements AttributeConverter<GenderDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GenderDTO attribute) {
      return attribute.getValue();
    }

    @Override
    public GenderDTO convertToEntityAttribute(Integer dbData) {
      return GenderDTO.fromValue(dbData);
    }
  }
}