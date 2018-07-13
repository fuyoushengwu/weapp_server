package cn.aijiamuyingfang.server.rest.auth.filter;

import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.rest.auth.service.JwtTokenService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenService jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String authHeader = request.getHeader(AuthConstants.HEADER_STRING);

    if (authHeader == null || !authHeader.startsWith(AuthConstants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
    mutableRequest.putHeader("userid", authentication != null ? authentication.getName() : "");
    chain.doFilter(mutableRequest, response);
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String authHeader = request.getHeader(AuthConstants.HEADER_STRING);
    String authToken = authHeader.replace(AuthConstants.TOKEN_PREFIX, "");
    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    if (StringUtils.isEmpty(username)) {
      return null;
    }

    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
    if (!jwtTokenUtil.validateToken(authToken, userDetails)) {
      return null;
    }

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
  }
}
