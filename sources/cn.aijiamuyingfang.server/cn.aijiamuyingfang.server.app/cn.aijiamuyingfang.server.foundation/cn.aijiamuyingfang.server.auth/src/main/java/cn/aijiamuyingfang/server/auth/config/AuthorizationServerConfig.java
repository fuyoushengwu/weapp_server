package cn.aijiamuyingfang.server.auth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import cn.aijiamuyingfang.server.auth.granter.JSCodeTokenGranter;
import cn.aijiamuyingfang.server.auth.service.OAuth2Service;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  /**
   * 认证管理器
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private OAuth2Service oauth2Service;

  @Value("${security.oauth2.resource.jwt.key-value}")
  private String jwtKey;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(jwtKey);
    return converter;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.allowFormAuthenticationForClients();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.reuseRefreshTokens(false);
    endpoints.authenticationManager(authenticationManager);
    endpoints.accessTokenConverter(accessTokenConverter());
    JSCodeTokenGranter jscodeTokenGranter = new JSCodeTokenGranter(oauth2Service, authenticationManager,
        endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
    final TokenGranter delegate = endpoints.getTokenGranter();
    endpoints.tokenGranter((grantType, tokenRequest) -> {
      OAuth2AccessToken result = null;
      if (delegate != null) {
        result = delegate.grant(grantType, tokenRequest);
      }
      if (null == result) {
        result = jscodeTokenGranter.grant(grantType, tokenRequest);
      }
      return result;

    });
  }

}
