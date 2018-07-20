package cn.aijiamuyingfang.server.user.service;

import cn.aijiamuyingfang.server.commons.constants.AuthConstants;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.user.TokenResponse;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.UserAuthority;
import cn.aijiamuyingfang.server.domain.user.UserRequest;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import cn.aijiamuyingfang.server.rest.auth.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 用户鉴权相关服务的Service层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-07 02:43:49
 */
@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtTokenService tokenService;

  @Autowired
  private UserDetailsService userDetailService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Value("${wechat.miniapp.appid}")
  private String appid;

  /**
   * 普通用户登录
   * 
   * @param openid
   * @param nickname
   * @param avatar
   * @return
   */
  public TokenResponse getNormalUserToken(String openid, String nickname, String avatar) {
    User user = userRepository.findByOpenid(openid);
    if (null == user) {
      user = new User();
      user.setOpenid(openid);
      user.setPassword(encoder.encode(openid));
      user.setAppid(appid);
      user.setNickname(nickname);
      user.setAvatar(avatar);
      userRepository.save(user);
    }
    return login(user);
  }

  /**
   * 系统用户登录
   * 
   * @param jscode
   * @return
   */
  public TokenResponse getSystemUserToken(String jscode) {
    User user = userRepository.findByOpenid(jscode);
    if (null == user) {
      throw new AuthException("400", "systemuser[" + jscode + "] not exist");
    }
    return login(user);
  }

  private TokenResponse login(User user) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user.getId(),
        user.getOpenid());
    // Perform the security
    Authentication authentication = authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // Reload password post-security so we can generate token
    UserDetails userDetails = userDetailService.loadUserByUsername(user.getId());
    String token = tokenService.generateToken(userDetails);
    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setToken(token);
    return tokenResponse;
  }

  /**
   * 注册用户(该方法只能ADMIN调用,用来注册SENDER)
   * 
   * @param request
   */
  public User registerUser(UserRequest request) {
    if (StringUtils.isEmpty(request.getJscode())) {
      throw new AuthException("400", "register failed,because not provide jscode as openid");
    }
    String jscode = request.getJscode();
    User user = userRepository.findByOpenid(jscode);
    if (user != null) {
      return user;
    }
    user = new User();
    user.setOpenid(jscode);
    user.setPassword(encoder.encode(jscode));
    user.setJscode(request.getJscode());
    user.setPhone(request.getPhone());
    user.setAvatar(request.getAvatar());
    user.setNickname(request.getNickname());
    user.addAuthority(UserAuthority.SENDER);
    userRepository.save(user);
    return user;
  }

  /**
   * 刷新Token
   * 
   * @param oldtoken
   * @return
   */
  public TokenResponse refreshToken(String oldtoken) {
    final String token = oldtoken.substring(AuthConstants.TOKEN_PREFIX.length());
    String username = tokenService.getUsernameFromToken(token);
    UserDetails userDetails = userDetailService.loadUserByUsername(username);
    if (null == userDetails) {
      throw new AuthException("400", "token donot have matched user[" + username + "]");
    }
    if (tokenService.canTokenBeRefreshed(token)) {
      TokenResponse tokenResponse = new TokenResponse();
      tokenResponse.setToken(tokenService.refreshToken(token));
      return tokenResponse;
    }
    throw new AuthException("400", "oldtoken is invalid,you need relogin");
  }
}
