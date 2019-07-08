package cn.aijiamuyingfang.vo.goods;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * 保质期
 */
@Data
public class ShelfLife implements Parcelable {
  /**
   * 保质期-开始时间
   */
  private String start;

  /**
   * 保质期-结束时间
   */
  private String end;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(start);
    dest.writeString(end);
  }

  public ShelfLife() {
  }

  private ShelfLife(Parcel in) {
    this.start = in.readString();
    this.end = in.readString();
  }

  public static final Parcelable.Creator<ShelfLife> CREATOR = new Parcelable.Creator<ShelfLife>() {
    @Override
    public ShelfLife createFromParcel(Parcel in) {
      return new ShelfLife(in);
    }

    @Override
    public ShelfLife[] newArray(int size) {
      return new ShelfLife[size];
    }
  };
}