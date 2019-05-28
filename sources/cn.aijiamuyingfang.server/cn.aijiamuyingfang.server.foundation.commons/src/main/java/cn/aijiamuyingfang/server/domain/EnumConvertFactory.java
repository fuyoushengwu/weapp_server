package cn.aijiamuyingfang.server.domain;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.security.core.GrantedAuthority;

import cn.aijiamuyingfang.commons.utils.NumberUtils;

public class EnumConvertFactory implements ConverterFactory<String, BaseEnum> {
  private static final Map<Class<?>, Converter<String, ? extends BaseEnum>> converterMap = new WeakHashMap<>();

  static {
    converterMap.put(Gender.class, new GenderConverter());
    converterMap.put(UserMessageType.class, new UserMessageTypeConverter());
    converterMap.put(UserAuthority.class, new UserAuthorityTypeConverter());
    converterMap.put(GrantedAuthority.class, new UserAuthorityTypeConverter());
    converterMap.put(SendType.class, new SendTypeConverter());
    converterMap.put(ShopOrderStatus.class, new ShopOrderStatusConverter());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
    return (Converter<String, T>) converterMap.get(targetType);
  }

  /**
   * [描述]:
   * <p>
   * Gender枚举类型转换器
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2018-06-27 17:12:16
   */
  private static class GenderConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(String source) {
      return Gender.fromValue(NumberUtils.toInt(source, 0));
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
      return UserMessageType.fromValue(NumberUtils.toInt(source, 0));
    }
  }

  /**
   * [描述]:
   * <p>
   * UserAuthority枚举类型转换器
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2019-05-13 03:35:00
   */
  private static class UserAuthorityTypeConverter implements Converter<String, UserAuthority> {

    @Override
    public UserAuthority convert(String source) {
      return UserAuthority.fromValue(NumberUtils.toInt(source, 0));
    }
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
      return SendType.fromValue(NumberUtils.toInt(source, 0));
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
      return ShopOrderStatus.fromValue(NumberUtils.toInt(source, 0));
    }
  }
}