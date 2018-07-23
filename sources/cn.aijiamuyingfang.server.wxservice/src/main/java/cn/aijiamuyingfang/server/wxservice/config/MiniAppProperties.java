package cn.aijiamuyingfang.server.wxservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * [描述]:
 * <p>
 * 读取application*.properties中设置的weixin-java-miniapp包配置
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 04:12:36
 */
@ConfigurationProperties(prefix = "wechat.miniapp")
public class MiniAppProperties {
  /**
   * 设置微信小程序的appid
   */
  private String appid;

  /**
   * 设置微信小程序的Secret
   */
  private String secret;

  /**
   * 消息格式，XML或者JSON
   */
  private String msgDataFormat;

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getMsgDataFormat() {
    return msgDataFormat;
  }

  public void setMsgDataFormat(String msgDataFormat) {
    this.msgDataFormat = msgDataFormat;
  }
}
