package cn.aijiamuyingfang.vo;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 图片资源
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-17 18:28:31
 */
@Data
public class ImageSource implements Parcelable {
  private String id;

  private String url;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(url);
  }

  public ImageSource() {
  }

  private ImageSource(Parcel in) {
    id = in.readString();
    url = in.readString();
  }

  public ImageSource(String id, String url) {
    this.id = id;
    this.url = url;
  }

  public static final Parcelable.Creator<ImageSource> CREATOR = new Creator<ImageSource>() {

    @Override
    public ImageSource[] newArray(int size) {
      return new ImageSource[size];
    }

    @Override
    public ImageSource createFromParcel(Parcel source) {
      return new ImageSource(source);
    }
  };
}
