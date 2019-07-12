package cn.aijiamuyingfang.server.it.user.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.api.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.db.user.UserMessageRepository;
import cn.aijiamuyingfang.server.it.dto.user.UserDTO;
import cn.aijiamuyingfang.vo.message.UserMessage;
import cn.aijiamuyingfang.vo.message.UserMessageType;

@Service
public class UserTestActions extends AbstractTestAction {

  private UserMessage userMessage;

  @Autowired
  private UserMessageControllerClient userMessageControllerClient;

  public UserMessage getSenderOneMessage() throws IOException {
    if (null == userMessage) {
      this.userMessage = new UserMessage();
      this.userMessage.setContent("content");
      this.userMessage.setCreateTime(new Date());
      this.userMessage.setFinishTime(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L));
      this.userMessage.setUsername(this.getSenderOne().getUsername());
      this.userMessage.setTitle("title");
      this.userMessage.setType(UserMessageType.NOTICE);
      this.userMessage = this.userMessageControllerClient.createMessage(this.getSenderOne().getUsername(),
          this.userMessage, this.getSenderOneAccessToken());
    }
    return userMessage;
  }

  public void deleteSenderOneMessage() throws IOException {
    if (userMessage != null) {
      userMessageControllerClient.deleteMessage(userMessage.getUsername(), userMessage.getId(),
          getSenderOneAccessToken(), false);
      this.userMessage = null;
    }
  }

  @Autowired
  private UserMessageRepository userMessageRepository;

  @TargetDataSource(name = "weapp-user")
  public void clearUserMessage() {
    userMessageRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-user")
  public void clearUserPhone(String userId) {
    UserDTO userDTO = userRepository.findOne(userId);
    userDTO.setPhone(null);
    userRepository.save(userDTO);
  }
}
