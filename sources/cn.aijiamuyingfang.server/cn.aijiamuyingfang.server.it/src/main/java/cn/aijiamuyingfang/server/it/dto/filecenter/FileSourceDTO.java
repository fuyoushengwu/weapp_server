package cn.aijiamuyingfang.server.it.dto.filecenter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.server.it.dto.BaseEnum;

@Convert(converter = FileSourceDTO.FileSourceConverter.class)
public enum FileSourceDTO implements BaseEnum {
  /**
   * 未知类型
   */
  @SerializedName("0")
  UNKNOW(0),

  /**
   * 本地
   */
  @SerializedName("1")
  LOCAL(1);

  private int value;

  private FileSourceDTO(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static FileSourceDTO fromValue(int source) {
    for (FileSourceDTO fileSource : FileSourceDTO.values()) {
      if (fileSource.getValue() == source) {
        return fileSource;
      }
    }
    return LOCAL;
  }

  class FileSourceConverter implements AttributeConverter<FileSourceDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FileSourceDTO attribute) {
      return attribute.getValue();
    }

    @Override
    public FileSourceDTO convertToEntityAttribute(Integer dbData) {
      return FileSourceDTO.fromValue(dbData);
    }
  }
}
