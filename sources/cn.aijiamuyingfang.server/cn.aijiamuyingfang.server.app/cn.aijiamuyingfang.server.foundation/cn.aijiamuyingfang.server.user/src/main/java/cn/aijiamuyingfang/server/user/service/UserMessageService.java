package cn.aijiamuyingfang.server.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.domain.UserAuthority;
import cn.aijiamuyingfang.server.user.db.UserMessageRepository;
import cn.aijiamuyingfang.server.user.db.UserRepository;
import cn.aijiamuyingfang.server.user.domain.User;
import cn.aijiamuyingfang.server.user.domain.UserMessage;
import cn.aijiamuyingfang.server.user.domain.response.GetMessagesListResponse;

/**
 * [描述]:
 * <p>
 * 提供消息服务的Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-01 15:00:01
 */
@Service
public class UserMessageService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMessageRepository userMessageRepository;

  /**
   * 获得用户未读消息数量
   * 
   * @param userId
   *          用户id
   * @return
   */
  public int getUserUnReadMessageCount(String userId) {
    List<String> adminUserIdList = userRepository.findUsersByAuthority(UserAuthority.MANAGER_PERMISSION.getValue());
    if (adminUserIdList.isEmpty()) {
      adminUserIdList.add(userId);
    }
    return userMessageRepository.getUNReadMessageCount(userId, adminUserIdList);
  }

  /**
   * 分页获取用户消息
   * 
   * @param userId
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetMessagesListResponse getUserMessageList(String userId, int currentPage, int pageSize) {
    List<String> userIdList = new ArrayList<>();
    userIdList.add(userId);
    userIdList.addAll(userRepository.findUsersByAuthority(UserAuthority.MANAGER_PERMISSION.getValue()));
    GetMessagesListResponse response = getMessageList(userIdList, currentPage, pageSize);
    User user = userRepository.findOne(userId);
    if (user != null) {
      user.setLastReadMsgTime(new Date());
      userRepository.saveAndFlush(user);
    }
    return response;
  }

  /**
   * 分页获取消息
   * 
   * @param userIdList
   *          用户idList
   * @param currentPage
   * @param pageSize
   * @return
   */
  private GetMessagesListResponse getMessageList(List<String> userIdList, int currentPage, int pageSize) {
    // 在查询之前先把所有过期的消息清除
    cleanOvertimeMessage();
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.DESC, "createTime");
    Page<UserMessage> page = userMessageRepository.findByUserIdIn(userIdList, pageRequest);
    GetMessagesListResponse response = new GetMessagesListResponse();
    response.setCurrentPage(page.getNumber() + 1);
    response.setDataList(page.getContent());
    response.setTotalpage(page.getTotalPages());
    return response;
  }

  /**
   * 清除过期的消息
   */
  private void cleanOvertimeMessage() {
    userMessageRepository.cleanOvertimeMessage();
  }

  /**
   * 为用户创建消息
   * 
   * @param userId
   *          用户id
   * @param message
   * @return
   */
  public UserMessage createMessage(String userId, UserMessage message) {
    if (message != null) {
      message.setUserId(userId);
      userMessageRepository.saveAndFlush(message);
    }
    return message;
  }

  /**
   * 删除用户消息消息
   * 
   * @param userId
   *          用户id
   * @param messageId
   */
  public void deleteMessage(String userId, String messageId) {
    UserMessage message = userMessageRepository.findOne(messageId);
    if (null == message) {
      return;
    }
    if (userId.equals(message.getUserId())) {
      userMessageRepository.delete(message);
    } else {
      throw new AccessDeniedException("no permission delete other user's message");
    }
  }

}
