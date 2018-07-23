package cn.aijiamuyingfang.commons.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

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
@Entity
public class UserMessage {
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
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
  @Lob
  @Column(length = 100000)
  private String content;

  /**
   * 消息创建时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime = new Date();

  /**
   * 消息结束时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date finishTime;

  /**
   * 是否已读
   */
  private boolean readed;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public UserMessageType getType() {
    return type;
  }

  public void setType(UserMessageType type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRoundup() {
    return roundup;
  }

  public void setRoundup(String roundup) {
    this.roundup = roundup;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
  }

  public boolean isReaded() {
    return readed;
  }

  public void setReaded(boolean readed) {
    this.readed = readed;
  }

}