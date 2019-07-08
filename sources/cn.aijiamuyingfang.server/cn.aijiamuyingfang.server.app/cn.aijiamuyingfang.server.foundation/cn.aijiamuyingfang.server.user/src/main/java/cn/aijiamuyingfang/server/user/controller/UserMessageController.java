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
import cn.aijiamuyingfang.server.user.domain.response.PagableUserMessageList;
import cn.aijiamuyingfang.server.user.dto.UserMessageDTO;
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
  private UserMessageService userMessageService;

  /**
   * 获得用户未读消息数量
   * 
   * @param username
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{username}/message/unread/count")
  public int getUserUnReadMessageCount(@PathVariable("username") String username) {
    return userMessageService.getUserUnReadMessageCount(username);
  }

  /**
   * 分页获取用户消息
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{username}/message")
  public PagableUserMessageList getUserMessageList(@PathVariable("username") String username,
      @RequestParam("current_page") int currentPage, @RequestParam("page_size") int pageSize) {
    return userMessageService.getUserMessageList(username, currentPage, pageSize);
  }

  /**
   * 为用户创建消息
   * 
   * @param username
   * @param userMessage
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PostMapping(value = "/user/{username}/message")
  public UserMessageDTO createMessage(@PathVariable("username") String username, @RequestBody UserMessageDTO userMessage) {
    if (null == userMessage) {
      throw new IllegalArgumentException("usermessage request body is null");
    }
    if (StringUtils.isEmpty(userMessage.getTitle())) {
      throw new IllegalArgumentException("usermessage title is empty");
    }
    if (null == userMessage.getType()) {
      throw new IllegalArgumentException("usermessage type is null");
    }
    return userMessageService.createMessage(username, userMessage);
  }

  /**
   * 删除消息
   * 
   * @param username
   * @param messageId
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{username}/message/{message_id}")
  public void deleteMessage(@PathVariable("username") String username, @PathVariable("message_id") String messageId) {
    userMessageService.deleteMessage(username, messageId);
  }
}
