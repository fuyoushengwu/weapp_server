package cn.aijiamuyingfang.server.it.dto.shoporder;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

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
public enum SendTypeDTO {
  /**
   * 未知类型
   */
  UNKNOW(0),
  /**
   * 自取
   */
  PICKUP(1),

  /**
   * 送货
   */
  OWNSEND(2),

  /**
   * 快递
   */
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

  class SendTypeConverter implements AttributeConverter<SendTypeDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SendTypeDTO type) {
      return type.value;
    }

    @Override
    public SendTypeDTO convertToEntityAttribute(Integer value) {
      return SendTypeDTO.fromValue(value);
    }
  }
}