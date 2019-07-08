package cn.aijiamuyingfang.vo.address;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 行政区<br>
 * 该类是为了修正代码检查中的Province、City、County、Town出现的代码重复问题,<br>
 * 把它们的公共部分抽取到该类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 02:57:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class District implements Parcelable {
  /**
   * 行政区名
   */
  protected String name;

  /**
   * 行政区编码
   */
  protected String code;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(code);
  }
}
