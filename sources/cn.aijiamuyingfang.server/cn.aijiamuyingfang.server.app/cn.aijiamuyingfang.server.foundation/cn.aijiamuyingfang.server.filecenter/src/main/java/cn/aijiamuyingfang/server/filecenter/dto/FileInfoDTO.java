package cn.aijiamuyingfang.server.filecenter.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity(name = "file_info")
@Data
public class FileInfoDTO {

  @Id
  private String id;

  private String name;

  private Boolean image;

  private String contentType;

  private Long size;

  private String path;

  private String url;

  private FileSourceDTO source;

  private Date createTime;
}
