package cn.aijiamuyingfang.vo.filecenter;

import java.util.Date;

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

  private Date createTime;
}
