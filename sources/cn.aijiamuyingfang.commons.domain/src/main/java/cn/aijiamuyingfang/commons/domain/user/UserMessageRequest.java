package cn.aijiamuyingfang.commons.domain.user;

import cn.aijiamuyingfang.commons.domain.UserMessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 用户消息相关请求的RequestBean
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-05 00:01:56
 */
@MappedSuperclass
public class UserMessageRequest {
  /**
   * 用户消息:用户Id;系统消息:-1
   */
  protected String userid;

  /**
   * 用户消息类型
   */
  protected UserMessageType type;

  /**
   * 消息标题
   */
  protected String title;

  /**
   * 消息摘要
   */
  protected String roundup;

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
