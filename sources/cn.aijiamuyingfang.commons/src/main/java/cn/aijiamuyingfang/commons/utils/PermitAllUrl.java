package cn.aijiamuyingfang.commons.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * 需要放开权限的url
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 19:30:52
 */
@UtilityClass
public class PermitAllUrl {
  private static final String[] ENDPOINTS = { "/health", "/env", "/mappings", "/metrics", "/trace", "/dump", "/info",
      "/heapdump", "/loggers", "/v2/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**",
      "/logs-anon/internal", "/users-anon/internal/**", "/favicon.ico", "/css/**", "/js/**", "/fonts/**", "/layui/**",
      "/img/**", "/pages/**", "/*.html" };

  /**
   * 需要开放的url
   * 
   * @param urls
   * @return
   */
  public static String[] permitAllUrl(String... urls) {
    if (null == urls || urls.length == 0) {
      return ENDPOINTS;
    }
    List<String> list = new ArrayList<>();
      Collections.addAll(list, ENDPOINTS);

      Collections.addAll(list, urls);

    return list.toArray(new String[0]);
  }
}
