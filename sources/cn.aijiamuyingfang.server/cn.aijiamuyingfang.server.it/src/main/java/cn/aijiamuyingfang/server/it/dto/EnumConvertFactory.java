package cn.aijiamuyingfang.server.it.dto;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.security.core.GrantedAuthority;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.server.it.dto.message.UserMessageTypeDTO;
import cn.aijiamuyingfang.server.it.dto.shoporder.SendTypeDTO;
import cn.aijiamuyingfang.server.it.dto.shoporder.ShopOrderStatusDTO;
import cn.aijiamuyingfang.server.it.dto.user.GenderDTO;
import cn.aijiamuyingfang.server.it.dto.user.UserAuthorityDTO;

public class EnumConvertFactory implements ConverterFactory<String, BaseEnum> {
  private static final Map<Class<?>, Converter<String, ? extends BaseEnum>> converterMap = new WeakHashMap<>();

  static {
    converterMap.put(GenderDTO.class, new GenderConverter());
    converterMap.put(UserMessageTypeDTO.class, new UserMessageTypeConverter());
    converterMap.put(UserAuthorityDTO.class, new UserAuthorityTypeConverter());
    converterMap.put(GrantedAuthority.class, new UserAuthorityTypeConverter());
    converterMap.put(SendTypeDTO.class, new SendTypeConverter());
    converterMap.put(ShopOrderStatusDTO.class, new ShopOrderStatusConverter());
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
  private static class GenderConverter implements Converter<String, GenderDTO> {

    @Override
    public GenderDTO convert(String source) {
      return GenderDTO.fromValue(NumberUtils.toInt(source, 0));
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
  private static class UserMessageTypeConverter implements Converter<String, UserMessageTypeDTO> {

    @Override
    public UserMessageTypeDTO convert(String source) {
      return UserMessageTypeDTO.fromValue(NumberUtils.toInt(source, 0));
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
  private static class UserAuthorityTypeConverter implements Converter<String, UserAuthorityDTO> {

    @Override
    public UserAuthorityDTO convert(String source) {
      return UserAuthorityDTO.fromValue(NumberUtils.toInt(source, 0));
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
  private static class SendTypeConverter implements Converter<String, SendTypeDTO> {

    @Override
    public SendTypeDTO convert(String source) {
      return SendTypeDTO.fromValue(NumberUtils.toInt(source, 0));
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
  private static class ShopOrderStatusConverter implements Converter<String, ShopOrderStatusDTO> {
    @Override
    public ShopOrderStatusDTO convert(String source) {
      return ShopOrderStatusDTO.fromValue(NumberUtils.toInt(source, 0));
    }
  }
}