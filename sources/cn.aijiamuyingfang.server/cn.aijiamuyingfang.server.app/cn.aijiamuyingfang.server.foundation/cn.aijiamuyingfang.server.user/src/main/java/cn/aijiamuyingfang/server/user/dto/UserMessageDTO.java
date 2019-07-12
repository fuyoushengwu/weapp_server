package cn.aijiamuyingfang.server.user.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

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
@Entity(name = "user_message")
@Data
public class UserMessageDTO {

  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 用户消息:用户Id;系统消息:-1
   */
  private String username;

  /**
   * 用户消息类型
   */
  private UserMessageTypeDTO type;

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
  @Lob
  @Column(length = 100000)
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