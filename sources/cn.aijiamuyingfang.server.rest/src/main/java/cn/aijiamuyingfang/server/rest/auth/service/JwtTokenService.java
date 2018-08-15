package cn.aijiamuyingfang.server.rest.auth.service;

import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 用来生成、验证和刷新JWT的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-08 06:21:39
 */
@Service
public class JwtTokenService {

  @Value("${weapp_jwt_secret}")
  private String secret;

  public String getUsernameFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject();
  }

  public Date getCreatedDateFromToken(String token) {
    final Claims claims = getClaimsFromToken(token);
    return new Date((Long) claims.get(AuthConstants.CLAIM_KEY_CREATED));
  }

  public Date getExpirationDateFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getExpiration();
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    } catch (RuntimeException e) {
      throw new AuthException("400", "token error,cannot be parsed");
    }
    return claims;

  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(AuthConstants.CLAIM_KEY_USERNAME, userDetails.getUsername());
    claims.put(AuthConstants.CLAIM_KEY_CREATED, new Date());
    return generateToken(claims);
  }

  String generateToken(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  private Date generateExpirationDate() {
    return new Date(AuthConstants.EXPIRATION_TIME);
  }

  public Boolean canTokenBeRefreshed(String token) {
    return !isTokenExpired(token);
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String refreshToken(String token) {
    final Claims claims = getClaimsFromToken(token);
    claims.put(AuthConstants.CLAIM_KEY_CREATED, new Date());
    return generateToken(claims);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    if (StringUtils.isEmpty(token) || null == userDetails) {
      return false;
    }
    User user = (User) userDetails;
    final String username = getUsernameFromToken(token);
    return username.equals(user.getUsername()) && !isTokenExpired(token);
  }
}
