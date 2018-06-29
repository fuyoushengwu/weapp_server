package cn.aijiamuyingfang.server.wxservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.TemplateMsg;
import cn.aijiamuyingfang.server.wxservice.service.TemplateMsgService;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * [描述]:
 * <p>
 * 模板消息Controller
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:04:22
 */
@RestController
public class TemplateMsgController {
	@Autowired
	private TemplateMsgService service;

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/preorder")
	public void sendPreOrderMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendPreOrderMsg(openid, msgData);
	}

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/pickup")
	public void sendPickupMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendPickupMsg(openid, msgData);
	}

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/thirdsend")
	public void sendThirdSendMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendThirdSendMsg(openid, msgData);
	}

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/ownsend")
	public void sendOwnSendMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendOwnSendMsg(openid, msgData);
	}

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/overtime")
	public void sendOrderOverTimeMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendOrderOverTimeMsg(openid, msgData);
	}

	@PostMapping(value = "/user/{openid}/wxservice/templatemsg/orderconfirm")
	public void sendOrderConfirmMsg(@PathVariable(name = "openid") String openid, @RequestBody TemplateMsg msgData)
			throws WxErrorException {
		service.sendOrderConfirmMsg(openid, msgData);
	}
}
