package cn.aijiamuyingfang.server.rest.auth.filter;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * [描述]:
 * <p>
 * 用来向HttpServletRequest中添加Header
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-09 03:12:32
 */
final class MutableHttpServletRequest extends HttpServletRequestWrapper {
  private final Map<String, String> customHeaders;

  public MutableHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.customHeaders = new HashMap<>();
  }

  public void putHeader(String name, String value) {
    this.customHeaders.put(name, value);
  }

  @Override
  public String getHeader(String name) {
    String headerValue = customHeaders.get(name);
    if (headerValue != null) {
      return headerValue;
    }
    return ((HttpServletRequest) getRequest()).getHeader(name);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    Enumeration<String> superEnum = super.getHeaders(name);
    return new Enumeration<String>() {
      /**
       * hasMoreElements方法是否已访问过customHeaders中的数据
       */
      private boolean hasMoreAccessedOwn = false;

      /**
       * nextElement方法是否已访问过customHeaders中的数据
       */
      private boolean nextEleAccessedOwn = false;

      @Override
      public boolean hasMoreElements() {
        boolean result = false;
        synchronized (this) {
          if (!hasMoreAccessedOwn) {
            result = customHeaders.containsKey(name);
            hasMoreAccessedOwn = true;
          }
        }
        if (!result) {
          result = superEnum.hasMoreElements();
        }
        return result;
      }

      @Override
      public String nextElement() {
        String result = null;
        synchronized (this) {
          if (!nextEleAccessedOwn) {
            result = customHeaders.get(name);
            nextEleAccessedOwn = true;
          }
        }
        if (StringUtils.isEmpty(result)) {
          result = superEnum.nextElement();
        }
        return result;
      }

    };
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    Set<String> set = new HashSet<>(customHeaders.keySet());
    Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
    while (e.hasMoreElements()) {
      String n = e.nextElement();
      set.add(n);
    }
    return Collections.enumeration(set);
  }
}
