package cn.aijiamuyingfang.server.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.auth.domain.WeChatSession;
import cn.aijiamuyingfang.server.domain.Gender;
import cn.aijiamuyingfang.server.exception.WeChatServiceException;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.feign.domain.user.User;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;

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
public class OAuth2Service {
  @Autowired
  private WxMaUserService wxmaxUserService;

  @Autowired
  private UserClient userClient;

  @Value("${wechat.miniapp.appid}")
  private String appid;

  /**
   * 根据jscode获得用户会话信息
   * 
   * @param jscode
   * @return
   */
  public WeChatSession jscode2Session(String jscode) {
    try {
      WxMaJscode2SessionResult result = wxmaxUserService.getSessionInfo(jscode);
      WeChatSession userSession = new WeChatSession();
      userSession.setUsername(result.getOpenid());
      userSession.setSessionKey(result.getSessionKey());
      userSession.setUnionid(result.getUnionid());
      return userSession;
    } catch (WxErrorException e) {
      WxError error = e.getError();
      if (error != null) {
        throw new WeChatServiceException(error.getErrorCode() + "", error.getErrorMsg());
      }
      throw new WeChatServiceException("500", e.getMessage());
    }
  }

  /**
   * 獲取用戶(儅系統中沒有時則創建)
   * 
   * @param username
   * @param password
   * @param nickname
   * @param avatar
   * @param gender
   * @return
   */
  public User getOrCreateUserIfAbsent(String username, String password, String nickname, String avatar, Gender gender) {
    User user = userClient.getUserInternal(username).getData();
    if (null == user) {
      user = new User();
      user.setUsername(username);
      user.setPassword(password);
      user.setAppid(appid);
      user.setNickname(nickname);
      user.setAvatar(avatar);
      user.setGender(gender);
      return userClient.registerUserInternal(user).getData();
    }
    return user;
  }
}
