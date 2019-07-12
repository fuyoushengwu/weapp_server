package cn.aijiamuyingfang.vo.filecenter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FileInfo {

  private String id;

  private String name;

  private Boolean image;

  private String contentType;

  private Long size;

  private String path;

  private String url;

  private FileSource source;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
}
