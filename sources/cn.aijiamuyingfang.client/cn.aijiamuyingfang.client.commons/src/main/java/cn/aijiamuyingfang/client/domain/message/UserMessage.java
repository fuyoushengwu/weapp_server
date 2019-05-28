package cn.aijiamuyingfang.client.domain.message;

import java.util.Date;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户消息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 17:11:10
 */
@Data
public class UserMessage {

  private String id;

  /**
   * 用户消息:用户Id;系统消息:-1
   */
  private String userid;

  /**
   * 用户消息类型
   */
  private UserMessageType type;

  /**
   * 消息标题
   */
  private String title;

  /**
   * 消息摘要
   */
  private String roundup;

  /**
   * 消息内容
   */
  private String content;

  /**
   * 消息创建时间
   */
  private Date createTime = new Date();

  /**
   * 消息结束时间
   */
  private Date finishTime;

  /**
   * 是否已读
   */
  private boolean readed;

}