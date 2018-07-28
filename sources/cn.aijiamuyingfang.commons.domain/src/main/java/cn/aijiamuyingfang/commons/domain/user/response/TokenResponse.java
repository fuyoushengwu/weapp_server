package cn.aijiamuyingfang.commons.domain.user.response;

/**
 * [描述]:
 * <p>
 * 获取JWT的返回类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-08 18:47:43
 */
public class TokenResponse {

  private String userid;

  private String token;

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

}