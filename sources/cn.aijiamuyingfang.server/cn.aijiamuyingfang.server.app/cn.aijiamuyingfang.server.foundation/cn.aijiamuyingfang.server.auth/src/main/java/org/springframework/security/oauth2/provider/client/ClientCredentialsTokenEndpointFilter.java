/*
 * Copyright 2006-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.security.oauth2.provider.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import cn.aijiamuyingfang.commons.utils.StringUtils;

/**
 * <p>
 * 因为小程序客户端只提供了'client_id'，没有提供'client_secret'.所以我们要自己获得'client_secret'。
 * 获取方式:根据'client_id',获取数据库中的'client_secret'。让后利用'client_id'和'client_secret'去微信
 * 验证'jscode'的有效性.通过，则认为请求的确是从'client_id'传递过来的,使用数据库中的'client_secret'
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-31 07:08:33
 */
public class ClientCredentialsTokenEndpointFilter extends AbstractAuthenticationProcessingFilter {

  private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

  private boolean allowOnlyPost = false;

  public ClientCredentialsTokenEndpointFilter() {
    this("/oauth/token");
  }

  public ClientCredentialsTokenEndpointFilter(String path) {
    super(path);
    setRequiresAuthenticationRequestMatcher(new ClientCredentialsRequestMatcher(path));
    // If authentication fails the type is "Form"
    ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint).setTypeName("Form");
  }

  public void setAllowOnlyPost(boolean allowOnlyPost) {
    this.allowOnlyPost = allowOnlyPost;
  }

  /**
   * @param authenticationEntryPoint
   *          the authentication entry point to set
   */
  public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  public void afterPropertiesSet() {
    super.afterPropertiesSet();
    setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
      public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
          AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
          exception = new BadCredentialsException(exception.getMessage(), new BadClientCredentialsException());
        }
        authenticationEntryPoint.commence(request, response, exception);
      }
    });
    setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {
        // no-op - just allow filter chain to continue to token endpoint
      }
    });
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    if (allowOnlyPost && !"POST".equalsIgnoreCase(request.getMethod())) {
      throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" });
    }

    String clientId = request.getParameter("client_id");
    String clientSecret = request.getParameter("client_secret");
    if (StringUtils.isEmpty(clientSecret)) {
      // String jscode = request.getParameter("jscode");
      // TODO 通过jscode验证client_secret
      clientSecret = "02bc75f7860d5bd54e5302942a09b639";
    }

    // If the request is already authenticated we can assume that this
    // filter is not needed
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication;
    }

    if (clientId == null) {
      throw new BadCredentialsException("No client credentials presented");
    }

    if (clientSecret == null) {
      clientSecret = "";
    }

    clientId = clientId.trim();
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId, clientSecret);

    return this.getAuthenticationManager().authenticate(authRequest);

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }

  protected static class ClientCredentialsRequestMatcher implements RequestMatcher {

    private String path;

    public ClientCredentialsRequestMatcher(String path) {
      this.path = path;

    }

    @Override
    public boolean matches(HttpServletRequest request) {
      String uri = request.getRequestURI();
      int pathParamIndex = uri.indexOf(';');

      if (pathParamIndex > 0) {
        // strip everything after the first semi-colon
        uri = uri.substring(0, pathParamIndex);
      }

      String clientId = request.getParameter("client_id");

      if (clientId == null) {
        // Give basic auth a chance to work instead (it's preferred anyway)
        return false;
      }

      if ("".equals(request.getContextPath())) {
        return uri.endsWith(path);
      }

      return uri.endsWith(request.getContextPath() + path);
    }

  }

}
