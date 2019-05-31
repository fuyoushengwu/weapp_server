package cn.aijiamuyingfang.server.feign.domain.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.server.domain.Gender;
import lombok.Data;

@Data
public class User implements UserDetails {
  private static final long serialVersionUID = -9072277778135596157L;

  /**
   * 用户的Id
   */
  private String id;

  /**
   * 该小程序中用户的唯一Id
   */
  private String openid;

  /**
   * 密码
   */
  private String password;
  
  /**
   * 头像
   */
  private String avatar;

  /**
   * 性别
   */
  private Gender gender;
  /**
   * 昵称
   */
  private String nickname;
  /**
   * 用户所在的APP ID
   */
  private String appid;
  /**
   * 用户角色
   */
  @JsonDeserialize(contentUsing = AuthorityListDeserializer.class)
  private List<GrantedAuthority> authorityList = new ArrayList<>();

  private static class AuthorityListDeserializer extends JsonDeserializer<GrantedAuthority> {
    @Override
    public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
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
   * 用户通用积分
   */
  private int genericScore = 0;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorityList;
  }

  @Override
  public String getUsername() {
    return this.id;
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

}
