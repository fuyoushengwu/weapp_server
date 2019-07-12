package cn.aijiamuyingfang.vo.coupon;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 兑换券可以选择的兑换方式
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:54
 */
@Data
public class VoucherItem implements Parcelable {
  private String id;

  /**
   * 是否废弃
   */
  private boolean deprecated;

  /**
   * 兑换项名称
   */
  private String name;

  /**
   * 兑换项描述
   */
  private String description;

  /**
   * 兑换券关联的商品ID
   */
  private String goodId;
  
  /**
   * 多少兑换值可以兑换商品
   */
  private int score;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(goodId);
    dest.writeInt(score);
  }

  public VoucherItem() {
  }

  protected VoucherItem(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    name = in.readString();
    description = in.readString();
    goodId = in.readString();
    score = in.readInt();
  }

  public static final Creator<VoucherItem> CREATOR = new Creator<VoucherItem>() {
    @Override
    public VoucherItem createFromParcel(Parcel in) {
      return new VoucherItem(in);
    }

    @Override
    public VoucherItem[] newArray(int size) {
      return new VoucherItem[size];
    }
  };
}
