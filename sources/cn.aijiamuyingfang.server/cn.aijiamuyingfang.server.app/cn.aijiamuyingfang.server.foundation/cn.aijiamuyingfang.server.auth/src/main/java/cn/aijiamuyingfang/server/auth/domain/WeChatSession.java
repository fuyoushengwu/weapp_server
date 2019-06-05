package cn.aijiamuyingfang.server.auth.domain;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户会话
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 20:36:08
 */
@Data
public class WeChatSession {

  /**
   * 用户会话的Key,可以用来对用户的数据进行解密
   */
  private String sessionKey;

  /**
   * 小程序用戶沒有username,使用openid作爲username
   */
  private String username;

  /**
   * 用户在小程序中的唯一Id
   */
  private String unionid;
}