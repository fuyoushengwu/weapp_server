package cn.aijiamuyingfang.server.wxservice.service;

import cn.aijiamuyingfang.commons.domain.exception.WXServiceException;
import cn.aijiamuyingfang.commons.domain.wxservice.WXSession;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class WXSessionService {
  @Autowired
  private WxMaUserService userService;

  /**
   * 根据jscode获得用户会话信息
   * 
   * @param jscode
   * @return
   */
  public WXSession jscode2Session(String jscode) {
    try {
      WxMaJscode2SessionResult result = userService.getSessionInfo(jscode);
      WXSession userSession = new WXSession();
      userSession.setOpenid(result.getOpenid());
      userSession.setSessionKey(result.getSessionKey());
      userSession.setUnionid(result.getUnionid());
      return userSession;
    } catch (WxErrorException e) {
      WxError error = e.getError();
      if (error != null) {
        throw new WXServiceException(error.getErrorCode() + "", error.getErrorMsg());
      }
      throw new WXServiceException("500", e.getMessage());
    }
  }
}
