package cn.aijiamuyingfang.client.domain.filecenter;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.client.domain.BaseEnum;

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

}
