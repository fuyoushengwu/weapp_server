package cn.aijiamuyingfang.server.it.dto.filecenter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert(converter = FileSourceDTO.FileSourceConverter.class)
public enum FileSourceDTO {
  /**
   * 未知类型
   */
  UNKNOW(0),

  /**
   * 本地
   */
  LOCAL(1);

  private int value;

  private FileSourceDTO(int value) {
    this.value = value;
  }

  public static FileSourceDTO fromValue(int value) {
    for (FileSourceDTO source : FileSourceDTO.values()) {
      if (source.value == value) {
        return source;
      }
    }
    return LOCAL;
  }

  class FileSourceConverter implements AttributeConverter<FileSourceDTO, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FileSourceDTO source) {
      return source.value;
    }

    @Override
    public FileSourceDTO convertToEntityAttribute(Integer value) {
      return FileSourceDTO.fromValue(value);
    }
  }
}
