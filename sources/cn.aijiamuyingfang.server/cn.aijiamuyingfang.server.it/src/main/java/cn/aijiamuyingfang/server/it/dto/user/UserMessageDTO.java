package cn.aijiamuyingfang.server.it.dto.user;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.server.it.domain.UserMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
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
  @JsonDeserialize(using = TypeDeserializer.class)
  private UserMessageType type;

  private static class TypeDeserializer extends JsonDeserializer<UserMessageType> {

    @Override
    public UserMessageType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return UserMessageType.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return UserMessageType.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return UserMessageType.UNKNOW;
    }

  }

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

}