package cn.aijiamuyingfang.server.filecenter;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.server" })
public class FileCenterApplication implements InitializingBean {
  public static void main(String[] args) {
    SpringApplication.run(FileCenterApplication.class, args);
  }

  @Autowired
  private ServletContext servletContext;

  /**
   * 上传文件存储在本地的根路径
   */
  @Value("${weapp.file-repo.localfs.path}")
  private String localFilePath;

  @Bean
  public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {

    return new WebMvcConfigurerAdapter() {

      /**
       * 外部文件访问<br>
       */
      @Override
      public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**")
            .addResourceLocations(ResourceUtils.FILE_URL_PREFIX + localFilePath + File.separator);
      }

    };
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    File tmp = new File(localFilePath);
    if (!tmp.isAbsolute()) {
      File location = (File) servletContext.getAttribute(ServletContext.TEMPDIR);
      localFilePath = location.getAbsolutePath() + localFilePath;
    }

  }
}
