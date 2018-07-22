package cn.aijiamuyingfang.server.rest.auth.service;

import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 使用User作为验证的用户表
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-06 04:44:07
 */
@Service
public class CustomUserService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userid) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new UsernameNotFoundException(userid);
    }
    return user;
  }
}