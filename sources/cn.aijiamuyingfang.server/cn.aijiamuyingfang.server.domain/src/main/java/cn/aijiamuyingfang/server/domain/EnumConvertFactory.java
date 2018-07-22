package cn.aijiamuyingfang.server.domain;

import cn.aijiamuyingfang.commons.domain.BaseEnum;
import cn.aijiamuyingfang.commons.domain.SendType;
import cn.aijiamuyingfang.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.commons.domain.UserMessageType;
import java.util.Map;
import java.util.WeakHashMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * [描述]:
 * <p>
 * 枚举类型转换器的工厂类<br/>
 * 枚举类型转换器:用于将枚举类型转换成字符串类型返回给服务调用方,或者将服务调用方传递关联的字符串转化为枚举类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 17:06:54
 */
public class EnumConvertFactory implements ConverterFactory<String, BaseEnum> {

  private static final Map<Class<? extends BaseEnum>,
      Converter<String, ? extends BaseEnum>> converterMap = new WeakHashMap<>();

  static {
    converterMap.put(SendType.class, new SendTypeConverter());
    converterMap.put(ShopOrderStatus.class, new ShopOrderStatusConverter());
    converterMap.put(UserMessageType.class, new UserMessageTypeConverter());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
    return (Converter<String, T>) converterMap.get(targetType);
  }

  /**
   * [描述]:
   * <p>
   * SendType枚举类型转换器
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2018-06-27 17:12:16
   */
  private static class SendTypeConverter implements Converter<String, SendType> {

    @Override
    public SendType convert(String source) {
      for (SendType sendType : SendType.values()) {
        if (String.valueOf(sendType.getValue()).equals(source)) {
          return sendType;
        }
      }
      return SendType.UNKNOW;
    }

  }

  /**
   * [描述]:
   * <p>
   * ShopOrderStatus枚举类型转换器
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2018-06-27 17:12:31
   */
  private static class ShopOrderStatusConverter implements Converter<String, ShopOrderStatus> {
    @Override
    public ShopOrderStatus convert(String source) {
      for (ShopOrderStatus status : ShopOrderStatus.values()) {
        if (String.valueOf(status.getValue()).equals(source)) {
          return status;
        }
      }
      return ShopOrderStatus.UNKNOW;
    }
  }

  /**
   * [描述]:
   * <p>
   * UserMessageType枚举类型转换器
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2018-06-27 17:12:41
   */
  private static class UserMessageTypeConverter implements Converter<String, UserMessageType> {

    @Override
    public UserMessageType convert(String source) {
      for (UserMessageType type : UserMessageType.values()) {
        if (String.valueOf(type.getValue()).equals(source)) {
          return type;
        }
      }
      return UserMessageType.UNKNOW;
    }

  }

}