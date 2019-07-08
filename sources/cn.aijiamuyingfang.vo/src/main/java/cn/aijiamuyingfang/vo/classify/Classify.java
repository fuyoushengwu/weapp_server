package cn.aijiamuyingfang.vo.classify;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.ImageSource;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 条目
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:32
 */
@Data
public class Classify implements Parcelable {
  /**
   * 条目Id
   */
  private String id;

  /**
   * 条目名
   */
  private String name;

  /**
   * 条目层级(最顶层的条目为1,顶层条目下的子条目为2)
   */
  private int level;

  /**
   * 条目封面
   */
  private ImageSource coverImg;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeInt(level);
    dest.writeParcelable(coverImg, flags);
  }

  public Classify() {
  }

  private Classify(Parcel in) {
    id = in.readString();
    name = in.readString();
    level = in.readInt();
    coverImg = in.readParcelable(Classify.class.getClassLoader());
  }

  public static final Parcelable.Creator<Classify> CREATOR = new Parcelable.Creator<Classify>() {
    @Override
    public Classify createFromParcel(Parcel in) {
      return new Classify(in);
    }

    @Override
    public Classify[] newArray(int size) {
      return new Classify[size];
    }
  };
}
