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
   * 小程序调用wx.login() 获取的临时登录凭证code
   */
  private String jscode;

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

  public String getUsername() {
    return this.id;
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
