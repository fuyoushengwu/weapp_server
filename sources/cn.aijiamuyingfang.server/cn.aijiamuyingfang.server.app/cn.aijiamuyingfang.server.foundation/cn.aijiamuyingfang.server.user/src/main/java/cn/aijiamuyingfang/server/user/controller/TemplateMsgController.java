package cn.aijiamuyingfang.server.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.server.user.service.TemplateMsgService;
import cn.aijiamuyingfang.vo.message.TemplateMsg;
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

  /**
   * 发送预定信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/preorder")
  public void sendPreOrderMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendPreOrderMsg(username, msgData);
  }

  /**
   * 发送自提信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/pickup")
  public void sendPickupMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendPickupMsg(username, msgData);
  }

  /**
   * 发送第三方配送信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/thirdsend")
  public void sendThirdSendMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendThirdSendMsg(username, msgData);
  }

  /**
   * 发送自己配送信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/ownsend")
  public void sendOwnSendMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendOwnSendMsg(username, msgData);
  }

  /**
   * 发送订单超时信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/overtime")
  public void sendOrderOverTimeMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendOrderOverTimeMsg(username, msgData);
  }

  /**
   * 发送订单确认信息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/orderconfirm")
  public void sendOrderConfirmMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData)
      throws WxErrorException {
    service.sendOrderConfirmMsg(username, msgData);
  }
}
