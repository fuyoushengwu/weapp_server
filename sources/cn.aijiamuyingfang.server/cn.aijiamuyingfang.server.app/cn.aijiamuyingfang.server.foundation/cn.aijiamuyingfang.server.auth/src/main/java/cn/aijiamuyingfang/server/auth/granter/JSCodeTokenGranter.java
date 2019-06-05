package cn.aijiamuyingfang.server.auth.granter;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.server.auth.domain.WeChatSession;
import cn.aijiamuyingfang.server.auth.service.OAuth2Service;
import cn.aijiamuyingfang.server.domain.Gender;
import cn.aijiamuyingfang.server.exception.OAuthException;
import cn.aijiamuyingfang.server.exception.WeChatServiceException;
import cn.aijiamuyingfang.server.feign.domain.user.User;
import lombok.extern.slf4j.Slf4j;

/**
 * [描述]:
 * <p>
 * 根据微信提供的js_code，生产access_token
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-30 08:58:27
 */
@Slf4j
public class JSCodeTokenGranter implements TokenGranter {
  private static final String GRANT_TYPE = "jscode";

  private final OAuth2Service oauth2Service;

  private final AuthenticationManager authenticationManager;

  private final AuthorizationServerTokenServices tokenServices;

  private final ClientDetailsService clientDetailsService;

  private final OAuth2RequestFactory requestFactory;

  public JSCodeTokenGranter(OAuth2Service oauth2Service, AuthenticationManager authenticationManager,
      AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory) {
    this.oauth2Service = oauth2Service;
    this.authenticationManager = authenticationManager;
    this.tokenServices = tokenServices;
    this.clientDetailsService = clientDetailsService;
    this.requestFactory = requestFactory;
  }

  public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
    if (!GRANT_TYPE.equals(grantType)) {
      return null;
    }
    User user = getUser(tokenRequest);
    ClientDetails clientDetails = getClientDetails(grantType, tokenRequest);
    return getAccessToken(clientDetails, user, tokenRequest);
  }

  private User getUser(TokenRequest tokenRequest) {
    Map<String, String> parameters = tokenRequest.getRequestParameters();
    String jscode = parameters.get("jscode");
    String password = parameters.get("password");
    String nickname = parameters.get("nickname");
    String avatar = parameters.get("avatar");
    Gender gender = Gender.fromValue(NumberUtils.toInt(parameters.get("gender"), Gender.UNKNOW.getValue()));
    User user;
    try {
      WeChatSession wechatSession = oauth2Service.jscode2Session(jscode);
      // 小程序用户没有密码的概念,使用openid代替
      password = wechatSession.getUsername();
      user = oauth2Service.getOrCreateUserIfAbsent(wechatSession.getUsername(), password, nickname, avatar, gender);
    } catch (WeChatServiceException e) {
      if ("40029".equals(e.getCode())) {
        log.error("JSCode[" + jscode + "] not a wechat user", e);
        user = oauth2Service.getOrCreateUserIfAbsent(jscode, password, nickname, avatar, gender);
      } else {
        throw e;
      }
    }
    if (null == user) {
      throw new OAuthException("400", "User[jscode='" + jscode + "'] not found");
    }
    // 因为从user-service获取的password是数据库中已经加密过的,所以这里需要重置一下
    user.setPassword(password);
    return user;
  }

  private ClientDetails getClientDetails(String grantType, TokenRequest tokenRequest) {
    Map<String, String> parameters = tokenRequest.getRequestParameters();
    String clientId = parameters.get("client_id");
    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
    if (null == clientDetails) {
      throw new OAuthException("400", "Client[" + clientId + "] not registered");
    }
    log.debug("Getting access token for: " + clientId);
    validateGrantType(grantType, clientDetails);
    return clientDetails;
  }

  private void validateGrantType(String grantType, ClientDetails clientDetails) {
    Collection<String> authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
    if (authorizedGrantTypes != null && !authorizedGrantTypes.isEmpty() && !authorizedGrantTypes.contains(grantType)) {
      throw new InvalidClientException("Unauthorized grant type: " + grantType);
    }
  }

  private OAuth2AccessToken getAccessToken(ClientDetails client, User user, TokenRequest tokenRequest) {
    return tokenServices.createAccessToken(getOAuth2Authentication(client, user, tokenRequest));
  }

  private OAuth2Authentication getOAuth2Authentication(ClientDetails client, User user, TokenRequest tokenRequest) {
    Authentication userAuth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    ((AbstractAuthenticationToken) userAuth).setDetails(tokenRequest.getRequestParameters());
    try {
      userAuth = authenticationManager.authenticate(userAuth);
    } catch (AccountStatusException ase) {
      // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
      throw new InvalidGrantException(ase.getMessage());
    } catch (BadCredentialsException e) {
      // If the username/password are wrong the spec says we should send 400/invalid grant
      throw new InvalidGrantException(e.getMessage());
    }
    if (userAuth == null || !userAuth.isAuthenticated()) {
      throw new InvalidGrantException("Could not authenticate user: " + user.getUsername());
    }

    OAuth2Request storedOAuth2Request = requestFactory.createOAuth2Request(client, tokenRequest);
    return new OAuth2Authentication(storedOAuth2Request, userAuth);
  }
}
