package cn.aijiamuyingfang.commons.domain.goods;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 保质期
 */
public class ShelfLife implements Parcelable {
  /**
   * 保质期-开始时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date start;

  /**
   * 保质期-结束时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date end;

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(start.getTime());
    dest.writeLong(end.getTime());
  }

  public ShelfLife() {

  }

  private ShelfLife(Parcel in) {
    this.start = new Date(in.readLong());
    this.end = new Date(in.readLong());
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