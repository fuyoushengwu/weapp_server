package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.UserMessageType;
import cn.aijiamuyingfang.server.client.AbstractTestAction;
import java.io.IOException;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-20 15:41:19
 */
@Service
public class UserTestActions extends AbstractTestAction {

  public String senderoneMessageId;

  public UserMessage createSenderOneMessage() throws IOException {
    UserMessage message = new UserMessage();
    message.setContent("content");
    message.setCreateTime(new Date());
    message.setFinishTime(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L));
    message.setUserid(senderoneId);
    message.setTitle("title");
    message.setType(UserMessageType.NOTICE);
    UserMessage userMessage = usermessageControllerClient.createMessage(senderoneToken, senderoneId, message);
    if (userMessage != null) {
      senderoneMessageId = userMessage.getId();
    }
    return userMessage;
  }

  @Override
  public void clearData() {
    super.clearData();
    senderoneMessageId = null;
  }

}
