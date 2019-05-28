package cn.aijiamuyingfang.server.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import feign.RequestInterceptor;

/**
 * [描述]:
 * <p>
 * 使用FeignClient访问微服务时,将access_token放入参数或者header<br>
 * 任选其一即可<br>
 * 如token为xxx<br>
 * 参数形式就是access_token=xxx<br>
 * header的话就是Authorization:Bear xxx<br>
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-06 21:38:19
 */
@Configuration
public class FeignInterceptorConfig {
  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication instanceof OAuth2Authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        template.header("Authorization", OAuth2AccessToken.BEARER_TYPE + " " + details.getTokenValue());
      }
    };
  }
}
