package cn.aijiamuyingfang.server.commons.controller.bean.wxservice;

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
public class UserSession {

	/**
	 * 用户会话的Key,可以用来对用户的数据进行解密
	 */
	private String sessionKey;

	/**
	 * 用户在微信中的唯一Id
	 */
	private String openid;

	/**
	 * 用户在小程序中的唯一Id
	 */
	private String unionid;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}
