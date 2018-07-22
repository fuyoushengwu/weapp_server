package cn.aijiamuyingfang.server.rest;

import cn.aijiamuyingfang.server.domain.EnumConvertFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * [描述]:
 * <p>
 * domain层相关的配置
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-07 01:16:13
 */
@EnableWebMvc
@Configuration
public class DomainConfig extends WebMvcConfigurerAdapter {
  @Override
  public void addFormatters(FormatterRegistry registry) {
    super.addFormatters(registry);
    registry.addConverterFactory(new EnumConvertFactory());
  }
}