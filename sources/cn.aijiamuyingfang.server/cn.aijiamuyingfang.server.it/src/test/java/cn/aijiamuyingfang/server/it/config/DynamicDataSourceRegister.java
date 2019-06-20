package cn.aijiamuyingfang.server.it.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import cn.aijiamuyingfang.server.it.context.DynamicDataSourceContextHolder;
import cn.aijiamuyingfang.server.it.db.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
  // 如配置文件中未指定数据源类型，使用该默认值
  private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";

  private ConversionService conversionService = new DefaultConversionService();

  private Map<String, DataSource> customDataSources = new HashMap<>();

  // 数据源
  private DataSource defaultDataSource;

  private PropertyValues dataSourcePropertyValues;

  private Environment env;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

    // 将主数据源添加到更多数据源中
    targetDataSources.put("dataSource", defaultDataSource);
    DynamicDataSourceContextHolder.addDataSourceKey("dataSource");

    // 添加更多数据源
    targetDataSources.putAll(customDataSources);
    for (String key : customDataSources.keySet()) {
      DynamicDataSourceContextHolder.addDataSourceKey(key);
    }

    // 创建DynamicDataSource
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(DynamicDataSource.class);
    beanDefinition.setSynthetic(true);
    MutablePropertyValues mpv = beanDefinition.getPropertyValues();
    mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
    mpv.addPropertyValue("targetDataSources", targetDataSources);

    registry.registerBeanDefinition("dataSource", beanDefinition);

    log.info("Dynamic DataSource Registry");
  }

  /**
   * 创建DataSource
   *
   * @param type
   * @param driverClassName
   * @param url
   * @param username
   * @param password
   * @return
   */
  @SuppressWarnings("unchecked")
  public DataSource buildDataSource(Map<String, Object> dsMap) {
    try {
      Object type = dsMap.get("type");
      if (null == type) {
        // 默认DataSource
        type = DATASOURCE_TYPE_DEFAULT;
      }

      Class<? extends DataSource> dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

      String driverClassName = env.resolvePlaceholders(dsMap.get("driver-class-name").toString());
      String url = env.resolvePlaceholders(dsMap.get("url").toString());
      String username = env.resolvePlaceholders(dsMap.get("username").toString());
      String password = env.resolvePlaceholders(dsMap.get("password").toString());
      
      DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
          .username(username).password(password).type(dataSourceType);
      return factory.build();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 加载多数据源配置
   */
  @Override
  public void setEnvironment(Environment env) {
    this.env = env;
    initDefaultDataSource(env);
    initCustomDataSources(env);
  }

  /**
   * 初始化主数据源
   *
   */
  private void initDefaultDataSource(Environment env) {
    // 读取主数据源
    RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "datasource.default.");
    if (propertyResolver.getSubProperties("").size() == 0) {
      return;
    }
    Map<String, Object> dsMap = new HashMap<>();
    dsMap.put("type", propertyResolver.getProperty("type"));
    dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
    dsMap.put("url", propertyResolver.getProperty("url"));
    dsMap.put("username", propertyResolver.getProperty("username"));
    dsMap.put("password", propertyResolver.getProperty("password"));

    defaultDataSource = buildDataSource(dsMap);

    dataBinder(defaultDataSource, env);
  }

  /**
   * 为DataSource绑定更多数据
   *
   * @param dataSource
   * @param env
   */
  private void dataBinder(DataSource dataSource, Environment env) {
    RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
    dataBinder.setConversionService(conversionService);
    dataBinder.setIgnoreNestedProperties(false);// false
    dataBinder.setIgnoreInvalidFields(false);// false
    dataBinder.setIgnoreUnknownFields(true);// true
    if (dataSourcePropertyValues == null) {
      Map<String, Object> rpr = new RelaxedPropertyResolver(env, "datasource.default").getSubProperties(".");
      Map<String, Object> values = new HashMap<>(rpr);
      // 排除已经设置的属性
      values.remove("type");
      values.remove("driver-class-name");
      values.remove("url");
      values.remove("username");
      values.remove("password");
      dataSourcePropertyValues = new MutablePropertyValues(values);
    }
    dataBinder.bind(dataSourcePropertyValues);
  }

  /**
   * 初始化更多数据源
   *
   */
  private void initCustomDataSources(Environment env) {
    // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
    RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "datasource.");
    Map<String, Object> rpr = propertyResolver.getSubProperties("");
    for (String key : rpr.keySet()) {
      if (key.endsWith(".name")) {
        String datasourceName = key.replace("datasource.", "").replace(".name", "");
        Map<String, Object> dsMap = propertyResolver.getSubProperties(datasourceName + ".");
        DataSource ds = buildDataSource(dsMap);
        customDataSources.put(datasourceName, ds);
        dataBinder(ds, env);
      }
    }
  }
}
