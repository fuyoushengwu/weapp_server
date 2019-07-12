package cn.aijiamuyingfang.server.it.dto.shoporder;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * [描述]:
 * <p>
 * 订单状态
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:03:34
 */
@Convert(converter = ShopOrderStatusDTO.ShopOrderStatusConverter.class)
public enum ShopOrderStatusDTO {
  /**
   * 未知类型
   */
  UNKNOW(0),
  /**
   * 预订
   */
  PREORDER(1),
  /**
   * 未开始
   */
  UNSTART(2),

  /**
   * 进行中
   */
  DOING(3),

  /**
   * 已完成
   */
  FINISHED(4),

  /**
   * 超时(例如:订单超时未取货就会变为超时的状态)
   */
  OVERTIME(5);

  private int value;

  private ShopOrderStatusDTO(int value) {
    this.value = value;
  }

  public static ShopOrderStatusDTO fromValue(int value) {
    for (ShopOrderStatusDTO status : ShopOrderStatusDTO.values()) {
      if (status.value == value) {
        return status;
      }
    }
    return UNKNOW;
  }

  class ShopOrderStatusConverter implements AttributeConverter<ShopOrderStatusDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ShopOrderStatusDTO status) {
      return status.value;
    }

    @Override
    public ShopOrderStatusDTO convertToEntityAttribute(Integer value) {
      return ShopOrderStatusDTO.fromValue(value);
    }
  }
}