package cn.aijiamuyingfang.server.logcenter.utils;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.server.logcenter.dto.LogDTO;
import cn.aijiamuyingfang.vo.logcenter.Log;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {
  public static Log convertLogDTO(LogDTO logDTO) {
    if (null == logDTO) {
      return null;
    }
    Log log = new Log();
    log.setId(logDTO.getId());
    log.setUsername(logDTO.getUsername());
    log.setModule(logDTO.getModule());
    log.setParams(logDTO.getParams());
    log.setRemark(logDTO.getRemark());
    log.setFlag(logDTO.getFlag());
    log.setCreateTime(logDTO.getCreateTime());
    return log;
  }

  public static List<Log> convertLogDTOList(List<LogDTO> logDTOList) {
    List<Log> logList = new ArrayList<>();
    if (null == logDTOList) {
      return logList;
    }
    for (LogDTO logDTO : logDTOList) {
      Log log = convertLogDTO(logDTO);
      if (log != null) {
        logList.add(log);
      }
    }
    return logList;

  }

  public static LogDTO convertLog(Log log) {
    if (null == log) {
      return null;
    }
    LogDTO logDTO = new LogDTO();
    logDTO.setId(log.getId());
    logDTO.setUsername(log.getUsername());
    logDTO.setModule(log.getModule());
    logDTO.setParams(log.getParams());
    logDTO.setRemark(log.getRemark());
    logDTO.setFlag(log.getFlag());
    logDTO.setCreateTime(log.getCreateTime());
    return logDTO;
  }

  public static List<LogDTO> convertLogList(List<Log> logList) {
    List<LogDTO> logDTOList = new ArrayList<>();
    if (null == logList) {
      return logDTOList;
    }
    for (Log log : logList) {
      LogDTO logDTO = convertLog(log);
      if (logDTO != null) {
        logDTOList.add(logDTO);
      }
    }
    return logDTOList;
  }
}
