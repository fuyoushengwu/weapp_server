package cn.aijiamuyingfang.server.it.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

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
@Convert(converter = SendType.SendTypeConverter.class)
public enum SendType implements BaseEnum {
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

  private SendType(int value) {
    this.value = value;
  }

  public static SendType fromValue(int value) {
    for (SendType type : SendType.values()) {
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

  class SendTypeConverter implements AttributeConverter<SendType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SendType attribute) {
      return attribute.getValue();
    }

    @Override
    public SendType convertToEntityAttribute(Integer dbData) {
      return SendType.fromValue(dbData);
    }
  }
}