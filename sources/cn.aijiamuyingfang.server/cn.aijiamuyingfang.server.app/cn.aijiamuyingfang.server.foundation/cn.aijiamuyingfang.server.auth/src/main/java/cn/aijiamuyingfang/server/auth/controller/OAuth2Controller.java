package cn.aijiamuyingfang.server.auth.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.server.auth.domain.WeChatSession;
import cn.aijiamuyingfang.server.auth.service.WeChatSessionService;

@RestController
public class OAuth2Controller {
  @Autowired
  private WeChatSessionService wechatSessionService;

  /**
   * 根据jscode获得用户会话信息
   * 
   * @param jscode
   * @return
   */
  @PreAuthorize("permitAll()")
  @GetMapping(value = "/oauth/session")
  public WeChatSession jscode2Session(@RequestParam("jscode") String jscode) {
    return wechatSessionService.jscode2Session(jscode);
  }

  /**
   * 登陆用户信息
   * 
   * @param principal
   * @return
   */
  @GetMapping("/user-me")
  public Principal principal(Principal principal) {
    return principal;
  }
}