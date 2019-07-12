package cn.aijiamuyingfang.vo.filecenter;

import com.google.gson.annotations.SerializedName;

import cn.aijiamuyingfang.vo.BaseEnum;

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

  public static FileSource fromValue(int value) {
    for (FileSource source : FileSource.values()) {
      if (source.getValue() == value) {
        return source;
      }
    }
    return LOCAL;
  }

}
