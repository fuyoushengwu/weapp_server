package cn.aijiamuyingfang.client.domain.store;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.client.commons.utils.StringUtils;
import lombok.Data;

/**
 * 营业时间
 */
@Data
public class WorkTime implements Parcelable {
  /**
   * 开始时间
   */
  private String start;

  /**
   * 结束时间
   */
  private String end;

  public void update(WorkTime updateWorktime) {
    if (null == updateWorktime) {
      return;
    }
    if (StringUtils.hasContent(updateWorktime.start)) {
      this.start = updateWorktime.start;
    }
    if (StringUtils.hasContent(updateWorktime.end)) {
      this.end = updateWorktime.end;
    }
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(start);
    dest.writeString(end);
  }

  public WorkTime() {
  }

  private WorkTime(Parcel in) {
    start = in.readString();
    end = in.readString();
  }

  public static final Parcelable.Creator<WorkTime> CREATOR = new Parcelable.Creator<WorkTime>() {
    @Override
    public WorkTime createFromParcel(Parcel in) {
      return new WorkTime(in);
    }

    @Override
    public WorkTime[] newArray(int size) {
      return new WorkTime[size];
    }
  };

}
