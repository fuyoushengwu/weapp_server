package cn.aijiamuyingfang.server.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.user.domain.UserMessage;
import cn.aijiamuyingfang.server.user.domain.response.GetMessagesListResponse;
import cn.aijiamuyingfang.server.user.service.UserMessageService;

/**
 * [描述]:
 * <p>
 * 提供消息服务的Controller
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-01 14:54:27
 */
@RestController
public class UserMessageController {
  @Autowired
  private UserMessageService usermessageService;

  /**
   * 获得用户未读消息数量
   * 
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/message/unread/count")
  public int getUserUnReadMessageCount(@PathVariable("userid") String userid) {
    return usermessageService.getUserUnReadMessageCount(userid);
  }

  /**
   * 分页获取用户消息
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/message")
  public GetMessagesListResponse getUserMessageList(@PathVariable("userid") String userid,
      @RequestParam("currentpage") int currentpage, @RequestParam("pagesize") int pagesize) {
    return usermessageService.getUserMessageList(userid, currentpage, pagesize);
  }

  /**
   * 为用户创建消息
   * 
   * @param userid
   * @param usermessage
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PostMapping(value = "/user/{userid}/message")
  public UserMessage createMessage(@PathVariable("userid") String userid, @RequestBody UserMessage usermessage) {
    if (null == usermessage) {
      throw new IllegalArgumentException("usermessage request body is null");
    }
    if (StringUtils.isEmpty(usermessage.getTitle())) {
      throw new IllegalArgumentException("usermessage title is empty");
    }
    if (null == usermessage.getType()) {
      throw new IllegalArgumentException("usermessage type is null");
    }
    return usermessageService.createMessage(userid, usermessage);
  }

  /**
   * 删除消息
   * 
   * @param userid
   * @param messageid
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{userid}/message/{messageid}")
  public void deleteMessage(@PathVariable("userid") String userid, @PathVariable("messageid") String messageid) {
    usermessageService.deleteMessage(userid, messageid);
  }
}
