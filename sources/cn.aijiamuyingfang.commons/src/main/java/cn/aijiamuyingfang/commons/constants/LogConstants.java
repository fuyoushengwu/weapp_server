package cn.aijiamuyingfang.commons.constants;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * 日志相关的常量
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-03 17:22:04
 */
@UtilityClass
public class LogConstants {

  /**
   * 接收日志的队列名
   */
  public static final String LOG_QUEUE = "system.log.queue";

  private static final Map<String, String> MODULES = new HashMap<>();

  public static final String LOGIN = "LOGIN";

  public static final String LOGOUT = "LOGOUT";

  public static final String FILE_UPLOAD = "FILE_UPLOAD";

  public static final String FILE_DELETE = "FILE_DELETE";
  static {
    MODULES.put(LOGIN, "登陆");
    MODULES.put(LOGOUT, "退出");

    MODULES.put(FILE_UPLOAD, "文件上传");
    MODULES.put(FILE_DELETE, "文件删除");
  }

  /**
   * 返回 Module信息
   * 
   * @return
   */
  public static Map<String, String> getModules() {
    return MODULES;
  }
}
