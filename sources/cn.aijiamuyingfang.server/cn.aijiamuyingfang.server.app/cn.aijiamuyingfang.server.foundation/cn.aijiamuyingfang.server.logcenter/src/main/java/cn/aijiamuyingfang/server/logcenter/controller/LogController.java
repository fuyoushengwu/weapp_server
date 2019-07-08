package cn.aijiamuyingfang.server.logcenter.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.commons.constants.LogConstants;
import cn.aijiamuyingfang.server.logcenter.domain.response.PagableLogList;
import cn.aijiamuyingfang.server.logcenter.dto.Log;
import cn.aijiamuyingfang.server.logcenter.service.LogService;

@RestController
public class LogController {
  @Autowired
  private LogService logService;

  /**
   * 保存日志记录到数据库
   * 
   * @param log
   */
  @PostMapping("/logs-anon/internal")
  public void save(@RequestBody Log log) {
    logService.save(log);
  }

  /**
   * 日志模块
   * 
   * @return
   */
  @GetMapping("/logs-modules")
  public Map<String, String> logModule() {
    return LogConstants.getModules();
  }

  /**
   * 日志查询
   * 
   * @param params
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @GetMapping("/logs")
  public PagableLogList getLogList(@RequestParam Map<String, String> params) {
    return logService.getLogList(params);
  }

}
