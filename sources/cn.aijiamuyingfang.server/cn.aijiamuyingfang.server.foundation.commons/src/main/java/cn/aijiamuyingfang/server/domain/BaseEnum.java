package cn.aijiamuyingfang.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * [描述]:
 * <p>
 * Enum要实现的接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:38:50
 */
public interface BaseEnum {
  @JsonValue
  int getValue();
}
