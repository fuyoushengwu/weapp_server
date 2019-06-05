package cn.aijiamuyingfang.client.domain.filecenter;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.client.rest.utils.NumberUtils;
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

  @JsonDeserialize(using = FileSourceDeserializer.class)
  private FileSource source;

  private static class FileSourceDeserializer extends JsonDeserializer<FileSource> {

    @Override
    public FileSource deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonToken currentToken = p.currentToken();
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        return FileSource.fromValue(p.getIntValue());
      } else if (currentToken == JsonToken.VALUE_STRING) {
        return FileSource.fromValue(NumberUtils.toInt(p.getValueAsString(), 0));
      }
      return FileSource.LOCAL;
    }

  }

  private Date createTime;
}
