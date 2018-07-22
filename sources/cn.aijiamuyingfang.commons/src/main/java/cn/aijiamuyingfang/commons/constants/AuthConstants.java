package cn.aijiamuyingfang.commons.constants;

/**
 * [描述]:
 * <p>
 * 用户鉴权模块相关的常量
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-08 06:17:23
 */
public final class AuthConstants {

  /**
   * 获取JWT的地址
   */
  public static final String GET_TOKEN_URL = "/user/token";

  /**
   * 获取微信会话信息的地址
   */
  public static final String WXSESSION_URL = "/wxservice/wxsession";

  /**
   * JWT的有效时间
   */
  public static final long EXPIRATION_TIME = 864_000_000; // 10 days

  /**
   * JWT Header的前缀
   */
  public static final String TOKEN_PREFIX = "Bearer ";

  /**
   * 传递JWT的Header
   */
  public static final String HEADER_STRING = "Authorization";

  /**
   * 生成JWT时,保存username的key
   */
  public static final String CLAIM_KEY_USERNAME = "sub";

  /**
   * 生成JWT时,保存createTime的key
   */
  public static final String CLAIM_KEY_CREATED = "created";

  private AuthConstants() {
  }

}
