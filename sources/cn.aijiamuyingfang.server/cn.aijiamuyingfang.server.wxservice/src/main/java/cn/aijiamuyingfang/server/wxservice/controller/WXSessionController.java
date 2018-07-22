package cn.aijiamuyingfang.server.wxservice.controller;

import cn.aijiamuyingfang.commons.controller.bean.wxservice.WXSession;
import cn.aijiamuyingfang.server.wxservice.service.WXSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [描述]:
 * <p>
 * 微信会话的Controller
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:57:30
 */
@RestController
public class WXSessionController {
  @Autowired
  private WXSessionService userSessionService;

  /**
   * 根据jscode获得用户会话信息
   * 
   * @param jscode
   * @return
   */
  @GetMapping(value = "/wxservice/wxsession")
  public WXSession jscode2Session(@RequestParam("jscode") String jscode) {
    return userSessionService.jscode2Session(jscode);
  }
}
