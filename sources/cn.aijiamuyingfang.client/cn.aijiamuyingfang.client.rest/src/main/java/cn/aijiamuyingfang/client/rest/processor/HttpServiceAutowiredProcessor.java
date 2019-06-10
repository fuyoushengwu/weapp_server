package cn.aijiamuyingfang.client.rest.processor;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import cn.aijiamuyingfang.client.commons.utils.StringUtils;
import cn.aijiamuyingfang.client.rest.annotation.HttpApi;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.bean.HttpServiceBeanFactory;

/**
 * [描述]:
 * <p>
 * HttpService注解的处理器
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 20:58:40
 */
@Component
public class HttpServiceAutowiredProcessor extends InstantiationAwareBeanPostProcessorAdapter {
  @Autowired
  private Environment env;

  @Override
  public boolean postProcessAfterInstantiation(Object bean, String beanName) {
    ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {

      @Override
      public void doWith(Field field) throws IllegalAccessException {
        if (null == field) {
          return;
        }
        HttpService httpService = field.getAnnotation(HttpService.class);
        if (null == httpService) {
          return;
        }
        createHttpServiceInstance(bean, field, field.getType());
      }
    });
    return super.postProcessAfterInstantiation(bean, beanName);
  }

  /**
   * 创建服务API实例
   * 
   * @param bean
   * @param field
   * @param clazz
   * @throws IllegalAccessException
   */
  protected void createHttpServiceInstance(Object bean, Field field, Class<?> clazz) throws IllegalAccessException {
    HttpApi httpApi = clazz.getAnnotation(HttpApi.class);
    String baseurl = httpApi.baseurl();
    if (StringUtils.isEmpty(baseurl)) {
      return;
    }
    String value = env.getProperty(baseurl);
    if (StringUtils.isEmpty(value)) {
      value = baseurl;
    }
    Object object = HttpServiceBeanFactory.putBean(value, clazz, httpApi.interceptor());
    if (null == object) {
      return;
    }
    field.setAccessible(true);
    field.set(bean, object);
  }

}
