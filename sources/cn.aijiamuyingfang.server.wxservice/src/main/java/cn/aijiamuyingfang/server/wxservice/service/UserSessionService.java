package cn.aijiamuyingfang.server.wxservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.UserSession;
import cn.aijiamuyingfang.server.rest.exception.WXServiceException;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * [描述]:
 * <p>
 * 用户会话的Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 02:11:02
 */
@Service
public class UserSessionService {
	@Autowired
	private WxMaUserService userService;

	/**
	 * 根据jscode获得用户会话信息
	 * 
	 * @param jscode
	 * @return
	 */
	public UserSession jscode2Session(String jscode) {
		try {
			WxMaJscode2SessionResult result = userService.getSessionInfo(jscode);
			UserSession userSession = new UserSession();
			userSession.setOpenid(result.getOpenid());
			userSession.setSessionKey(result.getSessionKey());
			userSession.setUnionid(result.getUnionid());
			return userSession;
		} catch (WxErrorException e) {
			throw new WXServiceException(e);
		}
	}
}
