package cn.aijiamuyingfang.commons.domain.user;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@Entity
public class User extends UserRequest implements UserDetails {
  private static final long serialVersionUID = -7352852942199443591L;

  /**
   * 用户的Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 该小程序中用户的唯一Id
   */
  @JsonIgnore
  private String openid;

  /**
   * 密码
   */
  @JsonIgnore
  private String password;

  /**
   * 用户角色
   */
  @JsonIgnore
  @ElementCollection(fetch = FetchType.EAGER)
  private List<UserAuthority> authorityList = new ArrayList<>();

  /**
   * 用户所在的APP ID
   */
  @JsonIgnore
  private String appid;

  /**
   * 性别
   */
  private Gender gender;

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
   * 使用提供的updateUser更新用户信息
   * 
   * @param updateUser
   */
  public void update(User updateUser) {
    if (null == updateUser) {
      return;
    }
    if (StringUtils.hasContent(updateUser.nickname)) {
      this.nickname = updateUser.nickname;
    }
    if (updateUser.gender != null) {
      this.gender = updateUser.gender;
    }
    if (StringUtils.hasContent(updateUser.avatar)) {
      this.avatar = updateUser.avatar;
    }
    if (StringUtils.hasContent(updateUser.phone)) {
      this.phone = updateUser.phone;
    }
    if (updateUser.lastReadMsgTime != null) {
      this.lastReadMsgTime = updateUser.lastReadMsgTime;
    }
    if (updateUser.genericScore != 0) {
      this.genericScore = updateUser.genericScore;
    }
    if (!CollectionUtils.isEmpty(updateUser.authorityList)) {
      this.authorityList = updateUser.authorityList;
    }
  }

  /**
   * 增加用户通用积分
   * 
   * @param score
   */
  public void increaseGenericScore(int score) {
    this.genericScore += score;
  }

  /**
   * 减少用户通用积分
   * 
   * @param score
   */
  public void decreaseGenericScore(int score) {
    this.genericScore -= score;
  }

  public void addAuthority(UserAuthority authority) {
    synchronized (this) {
      if (null == this.authorityList) {
        this.authorityList = new ArrayList<>();
      }
    }
    if (authority != null) {
      this.authorityList.add(authority);
    }
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorityList;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  @Override
  @JsonIgnore
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Date getLastReadMsgTime() {
    return lastReadMsgTime;
  }

  public void setLastReadMsgTime(Date lastReadMsgTime) {
    this.lastReadMsgTime = lastReadMsgTime;
  }

  public int getGenericScore() {
    return genericScore;
  }

  public void setGenericScore(int score) {
    this.genericScore = score;
  }

}
