package cn.aijiamuyingfang.server.rest.controller.bean;

/**
 * [描述]:
 * <p>
 * 返回码
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 05:04:23
 */
public enum ResponseCode {
	/**
	 * 门店不存在
	 */
	STORE_NOT_EXIST("404", "Store[%s] not exist"),
	/**
	 * 条目不存在
	 */
	CLASSIFY_NOT_EXIST("404", "Classify[%s] not exist"),
	/**
	 * 商品不存在
	 */
	GOOD_NOT_EXIST("404", "Good[%s] not exist"),

	/**
	 * 商品详细信息不存在
	 */
	GOODDETAIL_NOT_EXIST("404", "GoodDetail[%s] not exist"),

	/**
	 * 连接微信服务器失败
	 */
	USER_SESSION_WEIXIN_NET_ERR("006", "连接微信服务器失败"),

	/**
	 * CODE无效
	 */
	USER_SESSION_WEIXIN_CODE_ERR("007", "jscode无效"),

	/**
	 * 微信返回值错误
	 */
	USER_SESSION_RETURN_ERR("008", " 微信返回值错误"),
	/**
	 * 运行时异常
	 */
	RUNTIME_EXCEPTION("500", "%s"),

	/**
	 * 请求正常
	 */
	OK("200", "SUCCESS");

	private String code;

	private String message;

	ResponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage(Object... args) {
		return String.format(this.message, args);
	}
}
