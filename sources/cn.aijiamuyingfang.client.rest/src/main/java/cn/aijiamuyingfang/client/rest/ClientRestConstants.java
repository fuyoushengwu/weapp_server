package cn.aijiamuyingfang.client.rest;

/**
 * [描述]:
 * <p>
 * 客户端常量
 * </p>
 * 
 * @version 1.0.0
 * @author shiweideyouxiang@sina.cn
 * @date 2018-07-25 03:31:29
 */
public final class ClientRestConstants {
  /**
   * 读超时时间
   */
  public static final int DEFAULT_READ_TIMEOUT = 60;

  /**
   * 写超时时间
   */
  public static final int DEFAULT_WRITE_TIMEOUT = 60;

  /**
   * 连接超时时间
   */
  public static final int DEFAULT_CONNECT_TIMEOUT = 60;

  /**
   * 默认域名
   */
  public static final String DEFAULT_HOST_NAME = "www.aijiamuyingfang.cn";

  private ClientRestConstants() {
  }

}
