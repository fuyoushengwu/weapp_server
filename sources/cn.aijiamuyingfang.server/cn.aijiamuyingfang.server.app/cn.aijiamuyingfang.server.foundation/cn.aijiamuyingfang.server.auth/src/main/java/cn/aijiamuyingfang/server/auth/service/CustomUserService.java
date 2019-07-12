package cn.aijiamuyingfang.server.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.user.User;

@Service
public class CustomUserService implements UserDetailsService {
  @Autowired
  private UserClient userclient;

  @Override
  public UserDetails loadUserByUsername(String username) {
    ResponseBean<User> response = userclient.getUserInternal(username);
    if (ResponseCode.OK.getCode().equals(response.getCode())) {
      return response.getData();
    }
    throw new AuthenticationCredentialsNotFoundException("用户名不存在");
  }
}
