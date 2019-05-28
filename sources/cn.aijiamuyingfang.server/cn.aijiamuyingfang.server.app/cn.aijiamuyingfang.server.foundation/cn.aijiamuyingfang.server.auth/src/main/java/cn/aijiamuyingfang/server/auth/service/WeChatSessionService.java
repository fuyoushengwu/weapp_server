package cn.aijiamuyingfang.server.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.auth.domain.WeChatSession;
import cn.aijiamuyingfang.server.exception.WeChatServiceException;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * [描述]:
 * <p>
 * 微信会话的Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 02:11:02
 */
@Service
public class WeChatSessionService {
  @Autowired
  private WxMaUserService wxmaxUserService;

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
      userSession.setOpenid(result.getOpenid());
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
}
