package cn.aijiamuyingfang.server.it.dto.filecenter;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import lombok.Data;

@Entity
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

  @JsonDeserialize(using = FileSourceDeserializer.class)
  private FileSourceDTO source;

  private static class FileSourceDeserializer extends JsonDeserializer<FileSourceDTO> {

    @Override
    public FileSourceDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return FileSourceDTO.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return FileSourceDTO.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return FileSourceDTO.LOCAL;
    }

  }

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
}
