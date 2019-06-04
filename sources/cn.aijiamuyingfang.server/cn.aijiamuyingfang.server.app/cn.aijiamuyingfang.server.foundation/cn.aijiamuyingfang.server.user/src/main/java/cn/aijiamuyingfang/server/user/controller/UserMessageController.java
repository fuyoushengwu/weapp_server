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
  private UserMessageService userMessageService;

  /**
   * 获得用户未读消息数量
   * 
   * @param userId
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/message/unread/count")
  public int getUserUnReadMessageCount(@PathVariable("user_id") String userId) {
    return userMessageService.getUserUnReadMessageCount(userId);
  }

  /**
   * 分页获取用户消息
   * 
   * @param userId
   * @param currentPage
   * @param pageSize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/message")
  public GetMessagesListResponse getUserMessageList(@PathVariable("user_id") String userId,
      @RequestParam("current_page") int currentPage, @RequestParam("page_size") int pageSize) {
    return userMessageService.getUserMessageList(userId, currentPage, pageSize);
  }

  /**
   * 为用户创建消息
   * 
   * @param userId
   * @param userMessage
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userId.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @PostMapping(value = "/user/{user_id}/message")
  public UserMessage createMessage(@PathVariable("user_id") String userId, @RequestBody UserMessage userMessage) {
    if (null == userMessage) {
      throw new IllegalArgumentException("usermessage request body is null");
    }
    if (StringUtils.isEmpty(userMessage.getTitle())) {
      throw new IllegalArgumentException("usermessage title is empty");
    }
    if (null == userMessage.getType()) {
      throw new IllegalArgumentException("usermessage type is null");
    }
    return userMessageService.createMessage(userId, userMessage);
  }

  /**
   * 删除消息
   * 
   * @param userId
   * @param messageId
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{user_id}/message/{message_id}")
  public void deleteMessage(@PathVariable("user_id") String userId, @PathVariable("message_id") String messageId) {
    userMessageService.deleteMessage(userId, messageId);
  }
}
