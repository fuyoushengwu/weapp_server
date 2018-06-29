package cn.aijiamuyingfang.server.wxservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.UserSession;
import cn.aijiamuyingfang.server.wxservice.service.UserSessionService;

/**
 * [描述]:
 * <p>
 * 用户会话的Controller
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:57:30
 */
@RestController
public class UserSessionController {
	@Autowired
	private UserSessionService userSessionService;

	/**
	 * 根据jscode获得用户会话信息
	 * 
	 * @param jscode
	 * @return
	 */
	@GetMapping(value = "/user/wxservice/wxsession")
	public UserSession jscode2Session(@RequestParam("jscode") String jscode) {
		return userSessionService.jscode2Session(jscode);
	}
}
