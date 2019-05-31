package cn.aijiamuyingfang.server.auth.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {
  /**
   * 登陆用户信息
   * 
   * @param principal
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping("/user-me")
  public Principal principal(Principal principal) {
    return principal;
  }
}