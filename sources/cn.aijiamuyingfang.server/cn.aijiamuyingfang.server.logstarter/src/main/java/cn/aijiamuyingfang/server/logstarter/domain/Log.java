package cn.aijiamuyingfang.server.logstarter.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Log implements Serializable {
  private static final long serialVersionUID = -6643091397136573528L;

  private String username;

  private String module;

  private String params;

  private String remark;

  private Boolean flag;

  private Date createTime;
}
