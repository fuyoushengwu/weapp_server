package cn.aijiamuyingfang.vo.message;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
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
public class UserMessage implements Parcelable {

  private String id;

  /**
   * 用户消息:用户Id;系统消息:-1
   */
  private String username;

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(username);
    dest.writeParcelable(type, flags);
    dest.writeString(title);
    dest.writeString(roundup);
    dest.writeString(content);
    dest.writeLong(createTime != null ? createTime.getTime() : -1);
    dest.writeLong(finishTime != null ? finishTime.getTime() : -1);
    dest.writeByte((byte) (readed ? 1 : 0));
  }

  public UserMessage() {
  }

  private UserMessage(Parcel in) {
    id = in.readString();
    username = in.readString();
    type = in.readParcelable(UserMessageType.class.getClassLoader());
    title = in.readString();
    roundup = in.readString();
    content = in.readString();
    long createTimeValue = in.readLong();
    if (createTimeValue != -1) {
      createTime = new Date(createTimeValue);
    }
    long finishTimeValue = in.readLong();
    if (finishTimeValue != -1) {
      finishTime = new Date(finishTimeValue);
    }
    readed = in.readByte() != 0;
  }

  public static final Parcelable.Creator<UserMessage> CREATOR = new Parcelable.Creator<UserMessage>() {
    @Override
    public UserMessage createFromParcel(Parcel in) {
      return new UserMessage(in);
    }

    @Override
    public UserMessage[] newArray(int size) {
      return new UserMessage[size];
    }
  };

}