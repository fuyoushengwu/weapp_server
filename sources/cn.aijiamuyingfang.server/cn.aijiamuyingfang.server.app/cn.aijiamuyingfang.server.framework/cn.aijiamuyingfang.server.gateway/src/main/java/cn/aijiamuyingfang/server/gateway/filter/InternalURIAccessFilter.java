package cn.aijiamuyingfang.server.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * [描述]:
 * <p>
 * 系统内部端口(*-anon/internal*)调用时不需要验证的,但是只限服务间调用,外部不同通过gateway调用
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-18 06:16:31
 */
@Component
public class InternalURIAccessFilter extends ZuulFilter {

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public boolean shouldFilter() {
    RequestContext requestContext = RequestContext.getCurrentContext();
    HttpServletRequest request = requestContext.getRequest();

    return PatternMatchUtils.simpleMatch("*-anon/internal*", request.getRequestURI());
  }

  @Override
  public Object run() {
    RequestContext requestContext = RequestContext.getCurrentContext();
    requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
    requestContext.setResponseBody("Forbidden");
    requestContext.setSendZuulResponse(false);

    return null;
  }

}
