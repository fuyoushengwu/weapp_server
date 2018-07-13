package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.exception.UserException;
import cn.aijiamuyingfang.server.domain.user.GetMessagesListResponse;
import cn.aijiamuyingfang.server.domain.user.UserMessage;
import cn.aijiamuyingfang.server.domain.user.UserMessageRequest;
import cn.aijiamuyingfang.server.domain.util.ConverterService;
import cn.aijiamuyingfang.server.user.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  private ConverterService converterService;

  /**
   * 获得用户未读消息数量
   * 
   * @param headerUserId
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/message/unread/count")
  public int getUserUnReadMessageCount(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get user unread message count");
    }
    return usermessageService.getUserUnReadMessageCount(userid);
  }

  /**
   * 分页获取用户消息
   * 
   * @param headerUserId
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/message")
  public GetMessagesListResponse getUserMessageList(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestParam("currentpage") int currentpage,
      @RequestParam("pagesize") int pagesize) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission");
    }
    return usermessageService.getUserMessageList(userid, currentpage, pagesize);
  }

  /**
   * 为用户创建消息
   * 
   * @param headerUserId
   * @param userid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PostMapping(value = "/user/{userid}/message")
  public UserMessage createMessage(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @RequestBody UserMessageRequest request) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission");
    }
    if (null == request) {
      throw new UserException("400", "usermessage request body is null");
    }
    if (StringUtils.isEmpty(request.getTitle())) {
      throw new UserException("400", "usermessage title is empty");
    }
    if (null == request.getType()) {
      throw new UserException("400", "usermessage type is null");
    }
    return usermessageService.createMessage(userid, converterService.from(request));
  }

  /**
   * 删除消息
   * 
   * @param headerUserId
   * @param userid
   * @param messageid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @DeleteMapping(value = "/user/{userid}/message/{messageid}")
  public void deleteMessage(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @PathVariable("messageid") String messageid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission delete other user's message");
    }
    usermessageService.deleteMessage(userid, messageid);
  }
}
