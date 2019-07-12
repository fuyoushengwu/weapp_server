package cn.aijiamuyingfang.server.logstarter.utils;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.utils.JsonUtils;
import lombok.experimental.UtilityClass;

/**
 * [描述]:
 * <p>
 * User工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-10 06:20:26
 */
@UtilityClass
@SuppressWarnings("rawtypes")
public class UserUtils {
  /**
   * 获得当前用户
   * 
   * @return
   */
  public User currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2Authentication) {
      OAuth2Authentication oauth2Auth = (OAuth2Authentication) authentication;
      authentication = oauth2Auth.getUserAuthentication();

      if (authentication instanceof UsernamePasswordAuthenticationToken) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        Map map = (Map) authenticationToken.getDetails();
        map = (Map) map.get("principal");

        return JsonUtils.json2Bean(JsonUtils.bean2Json(map), User.class);
      }
    }
    return null;
  }
}
