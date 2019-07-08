package cn.aijiamuyingfang.server.it.dto.shoporder;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.server.it.dto.BaseEnum;

/**
 * [描述]:
 * <p>
 * 订单配送类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:51:30
 */
@Convert(converter = SendTypeDTO.SendTypeConverter.class)
public enum SendTypeDTO implements BaseEnum {
  /**
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW(0),
  /**
   * 自取
   */
  @SerializedName("1")
  PICKUP(1),

  /**
   * 送货
   */
  @SerializedName("2")
  OWNSEND(2),

  /**
   * 快递
   */
  @SerializedName("3")
  THIRDSEND(3);

  private int value;

  private SendTypeDTO(int value) {
    this.value = value;
  }

  public static SendTypeDTO fromValue(int value) {
    for (SendTypeDTO type : SendTypeDTO.values()) {
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

  class SendTypeConverter implements AttributeConverter<SendTypeDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SendTypeDTO attribute) {
      return attribute.getValue();
    }

    @Override
    public SendTypeDTO convertToEntityAttribute(Integer dbData) {
      return SendTypeDTO.fromValue(dbData);
    }
  }
}