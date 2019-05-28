package cn.aijiamuyingfang.server.it.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.google.gson.annotations.SerializedName;

@Convert(converter = FileSource.FileSourceConverter.class)
public enum FileSource implements BaseEnum {
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

  private FileSource(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static FileSource fromValue(int source) {
    for (FileSource fileSource : FileSource.values()) {
      if (fileSource.getValue() == source) {
        return fileSource;
      }
    }
    return LOCAL;
  }

  class FileSourceConverter implements AttributeConverter<FileSource, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FileSource attribute) {
      return attribute.getValue();
    }

    @Override
    public FileSource convertToEntityAttribute(Integer dbData) {
      return FileSource.fromValue(dbData);
    }
  }
}
