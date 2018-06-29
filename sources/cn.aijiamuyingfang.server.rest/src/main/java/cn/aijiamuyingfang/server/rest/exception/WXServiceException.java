package cn.aijiamuyingfang.server.rest.exception;

/**
 * [描述]:
 * <p>
 * WXService服务的异常类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 18:15:42
 */
public class WXServiceException extends RuntimeException {

	private static final long serialVersionUID = -8265484089215443124L;

	/**
	 * 创建一个新的实例 WXServiceException.
	 */
	public WXServiceException() {
		super();
	}

	/**
	 * 创建一个新的实例 WXServiceException.
	 * 
	 * @param message
	 * @param cause
	 */
	public WXServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 创建一个新的实例 WXServiceException.
	 * 
	 * @param message
	 */
	public WXServiceException(String message) {
		super(message);
	}

	/**
	 * 创建一个新的实例 WXServiceException.
	 * 
	 * @param cause
	 */
	public WXServiceException(Throwable cause) {
		super(cause);
	}

}
