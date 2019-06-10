package cn.aijiamuyingfang.client.domain.user;

import java.util.ArrayList;
import java.util.List;

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
public class User {

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
   * 性别
   */
  private Gender gender;

  /**
   * 用户角色
   */
  private List<UserAuthority> authorityList = new ArrayList<>();

  /**
   * 用户通用积分
   */
  private int genericScore = 0;

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
}
