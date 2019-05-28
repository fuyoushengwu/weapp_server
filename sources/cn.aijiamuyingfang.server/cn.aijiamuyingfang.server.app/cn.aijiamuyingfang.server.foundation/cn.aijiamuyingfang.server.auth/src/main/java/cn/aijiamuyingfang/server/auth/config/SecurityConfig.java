package cn.aijiamuyingfang.server.auth.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.aijiamuyingfang.commons.utils.PermitAllUrl;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.requestMatcher(new OAuth2RequestedMatcher()).authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl())
        .permitAll().anyRequest().authenticated().and().authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl())
        .permitAll().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
  }

  /**
   * 判断来源请求是否包含oauth2授权信息
   */
  private static class OAuth2RequestedMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
      // 请求参数中包含access_token参数
      if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
        return true;
      }
      // 头部的Authorization值以Bearer开头
      String auth = request.getHeader("Authorization");
      if (auth != null && auth.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
        return true;
      }
      return false;
    }

  }
}
