package cn.aijiamuyingfang.server.domain.user;

import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 用户相关请求的RequestBean
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 23:51:04
 */
@MappedSuperclass
public class UserRequest {
  /**
   * 昵称
   */
  protected String nickname;

  /**
   * 头像
   */
  protected String avatar;

  /**
   * 联系电话
   */
  protected String phone;

  /**
   * 小程序调用wx.login() 获取的临时登录凭证code
   */
  protected String jscode;

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getJscode() {
    return jscode;
  }

  public void setJscode(String jscode) {
    this.jscode = jscode;
  }

}
