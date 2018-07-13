package cn.aijiamuyingfang.server.domain.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * [描述]:
 * <p>
 * 用户权限
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-07 00:54:17
 */
public enum UserAuthority implements GrantedAuthority {

  /**
   * 管理员权限
   */
  ADMIN("admin"),
  /**
   * 送货员权限
   */
  SENDER("sender"),
  /**
   * 普通用户权限
   */
  NORMAL("normal");
  /**
   * 权限名
   */
  private String authority;

  private UserAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }

}
