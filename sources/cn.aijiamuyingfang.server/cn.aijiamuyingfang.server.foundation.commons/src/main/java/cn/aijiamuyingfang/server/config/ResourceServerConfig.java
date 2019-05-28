package cn.aijiamuyingfang.server.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import cn.aijiamuyingfang.commons.utils.PermitAllUrl;

/**
 * [描述]:
 * <p>
 * 资源服务器配置
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 20:35:14
 */
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnBean(name = "resourceServerConfig")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return bCryptPasswordEncoder;
  }
  
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().exceptionHandling()
        .authenticationEntryPoint(
            (request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and().authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl()).permitAll().anyRequest().authenticated()
        .and().httpBasic();
    http.headers().frameOptions().sameOrigin();
  }
}
