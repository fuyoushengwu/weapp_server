package cn.aijiamuyingfang.server.feign.domain.filecenter;

import java.io.IOException;

import javax.persistence.Id;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.aijiamuyingfang.commons.utils.NumberUtils;
import cn.aijiamuyingfang.server.domain.FileSource;
import lombok.Data;

@Data
public class FileInfo {
  @Id
  private String id;

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

}
