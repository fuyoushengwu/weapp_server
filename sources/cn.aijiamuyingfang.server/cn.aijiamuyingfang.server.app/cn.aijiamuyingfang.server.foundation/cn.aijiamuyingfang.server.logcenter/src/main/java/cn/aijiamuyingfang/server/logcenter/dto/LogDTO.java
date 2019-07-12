package cn.aijiamuyingfang.server.logcenter.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 日志信息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-09 23:28:55
 */
@Entity(name = "log")
@Data
public class LogDTO {
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  private String username;

  private String module;

  private String params;

  private String remark;

  private Boolean flag;

  private Date createTime;
}
