package cn.aijiamuyingfang.server.user.service;

import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.user.GetMessagesListResponse;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.UserMessage;
import cn.aijiamuyingfang.server.domain.user.db.UserMessageRepository;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
  private UserMessageRepository usermessageRepository;

  /**
   * 获得用户未读消息数量
   * 
   * @param userid
   * @return
   */
  public int getUserUnReadMessageCount(String userid) {
    List<String> useridList = new ArrayList<>();
    useridList.add(userid);
    useridList.add(userRepository.findAdminUserId());
    return usermessageRepository.getUNReadMessageCount(useridList);
  }

  /**
   * 分页获取用户消息
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetMessagesListResponse getUserMessageList(String userid, int currentpage, int pagesize) {
    List<String> useridList = new ArrayList<>();
    useridList.add(userid);
    useridList.add(userRepository.findAdminUserId());
    GetMessagesListResponse response = getMessageList(useridList, currentpage, pagesize);
    User user = userRepository.findOne(userid);
    if (user != null) {
      user.setLastReadMsgTime(new Date());
      userRepository.saveAndFlush(user);
    }
    return response;
  }

  /**
   * 分页获取消息
   * 
   * @param useridList
   * @param currentpage
   * @param pagesize
   * @return
   */
  private GetMessagesListResponse getMessageList(List<String> useridList, int currentpage, int pagesize) {
    // 在查询之前先把所有过期的消息清除
    cleanOvertimeMessage();
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize, Sort.Direction.DESC, "createTime");
    Page<UserMessage> page = usermessageRepository.findByUseridIn(useridList, pageRequest);
    GetMessagesListResponse response = new GetMessagesListResponse();
    response.setCurrentpage(page.getNumber() + 1);
    response.setDataList(page.getContent());
    response.setTotalpage(page.getTotalPages());
    return response;
  }

  /**
   * 清除过期的消息
   */
  private void cleanOvertimeMessage() {
    usermessageRepository.cleanOvertimeMessage();
  }

  /**
   * 为用户创建消息
   * 
   * @param userid
   * @param message
   * @return
   */
  public UserMessage createMessage(String userid, UserMessage message) {
    if (message != null) {
      message.setUserid(userid);
      usermessageRepository.saveAndFlush(message);
    }
    return message;
  }

  /**
   * 删除用户消息消息
   * 
   * @param userid
   * @param messageid
   */
  public void deleteMessage(String userid, String messageid) {
    UserMessage message = usermessageRepository.findOne(messageid);
    if (null == message) {
      return;
    }
    if (userid.equals(message.getUserid())) {
      usermessageRepository.delete(message);
    } else {
      throw new AuthException("403", "no permission delete other user's message");
    }
  }

}
