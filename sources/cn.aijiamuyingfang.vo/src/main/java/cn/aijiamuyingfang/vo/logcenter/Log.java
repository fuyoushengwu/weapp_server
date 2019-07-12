package cn.aijiamuyingfang.vo.logcenter;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Log implements Serializable {
  private static final long serialVersionUID = -6643091397136573528L;

  private String id;

  private String username;

  private String module;

  private String params;

  private String remark;

  private Boolean flag;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
}
