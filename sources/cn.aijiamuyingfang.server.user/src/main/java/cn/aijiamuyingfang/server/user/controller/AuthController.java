package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.client.rest.api.impl.WXSessionControllerClient;
import cn.aijiamuyingfang.commons.constants.AuthConstants;
import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.WXServiceException;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.domain.user.response.TokenResponse;
import cn.aijiamuyingfang.commons.domain.wxservice.WXSession;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.user.service.AuthService;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [描述]:
 * <p>
 * 用户鉴权相关服务的Controller层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-07 02:37:30
 */
@RestController
public class AuthController {
  private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

  @Autowired
  private AuthService authService;

  @Autowired
  private WXSessionControllerClient wxsessionControllerClient;

  /**
   * 获取JWT
   * 
   * @param jscode
   * @param nickname
   * @param avatar
   * @return
   * @throws IOException
   */
  @GetMapping(value = AuthConstants.GET_TOKEN_URL)
  public TokenResponse getToken(@RequestParam("jscode") String jscode,
      @RequestParam(value = "nickname", required = false) String nickname,
      @RequestParam(value = "avatar", required = false) String avatar) throws IOException {
    try {
      WXSession wxsession = wxsessionControllerClient.jscode2Session(jscode);
      if (StringUtils.isEmpty(wxsession.getOpenid())) {
        throw new AuthException(ResponseCode.GET_OPENID_NULL);
      }
      return authService.getNormalUserToken(wxsession.getOpenid(), nickname, avatar);
    } catch (WXServiceException e) {
      if ("40029".equals(e.getCode())) {
        LOGGER.error(e);
        return authService.getSystemUserToken(jscode);
      }
      throw e;
    }

  }

  /**
   * 注册用户
   * 
   * @param user
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping("/user/register")
  public User registerUser(@RequestBody User user) {
    return authService.registerUser(user);
  }

  /**
   * 刷新用户token
   * 
   * @param token
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = AuthConstants.GET_TOKEN_URL)
  public TokenResponse refreshToken(@RequestHeader(value = AuthConstants.HEADER_STRING) String token) {
    return authService.refreshToken(token);
  }

}
