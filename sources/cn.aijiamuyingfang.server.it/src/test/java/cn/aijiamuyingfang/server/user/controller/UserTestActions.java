package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.server.client.AbstractTestAction;
import cn.aijiamuyingfang.server.commons.domain.UserMessageType;
import cn.aijiamuyingfang.server.domain.user.UserMessage;
import cn.aijiamuyingfang.server.domain.user.UserMessageRequest;
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
    UserMessageRequest request = new UserMessageRequest();
    request.setContent("content");
    request.setCreateTime(new Date());
    request.setFinishTime(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L));
    request.setUserid(senderoneId);
    request.setTitle("title");
    request.setType(UserMessageType.NOTICE);
    UserMessage userMessage = usermessageControllerClient.createMessage(senderoneToken, senderoneId, request);
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
