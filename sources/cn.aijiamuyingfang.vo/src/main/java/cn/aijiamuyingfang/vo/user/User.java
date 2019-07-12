package cn.aijiamuyingfang.vo.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.vo.utils.NumberUtils;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 16:38:56
 */
@Data
public class User implements UserDetails {
  private static final long serialVersionUID = 443722768236688870L;

  /**
   * 小程序用戶沒有username,使用openid作爲username,
   */
  private String username;

  /**
   * 密码
   */
  private String password;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;

  /**
   * 联系电话
   */
  private String phone;

  /**
   * 用户角色
   */
  @JsonDeserialize(contentUsing = AuthorityListDeserializer.class)
  private List<UserAuthority> authorityList = new ArrayList<>();

  private static class AuthorityListDeserializer extends JsonDeserializer<UserAuthority> {

    @Override
    public UserAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return UserAuthority.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return UserAuthority.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return UserAuthority.UNKNOW;
    }

  }

  /**
   * 用户所在的APP ID
   */
  private String appid;

  /**
   * 性别
   */
  @JsonDeserialize(using = GenderDeserializer.class)
  private Gender gender;

  private static class GenderDeserializer extends JsonDeserializer<Gender> {

    @Override
    public Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return Gender.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return Gender.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return Gender.UNKNOW;
    }

  }

  /**
   * 最后一次消息读取时间
   */
  @JsonIgnore
  private Date lastReadMsgTime = new Date(0);

  /**
   * 用户通用积分
   */
  private int genericScore = 0;

  /**
   * 增加用户通用积分
   * 
   * @param score
   *          要添加的用户通用积分
   */
  public void increaseGenericScore(int score) {
    this.genericScore += score;
  }

  /**
   * 减少用户通用积分
   * 
   * @param score
   *          要减少的用户通用积分
   */
  public void decreaseGenericScore(int score) {
    this.genericScore -= score;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public void addAuthority(UserAuthority authority) {
    synchronized (this) {
      if (null == this.authorityList) {
        this.authorityList = new ArrayList<>();
      }
      if (authority != null && !this.authorityList.contains(authority)) {
        this.authorityList.add(authority);
      }
    }
  }

  @Override
  @JsonIgnore
  public Collection<UserAuthority> getAuthorities() {
    return authorityList;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
