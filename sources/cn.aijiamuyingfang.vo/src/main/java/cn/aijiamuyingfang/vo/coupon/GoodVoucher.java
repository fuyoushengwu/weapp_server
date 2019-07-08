package cn.aijiamuyingfang.vo.coupon;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 购买商品可以获得的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:02:06
 */
@Data
public class GoodVoucher implements Parcelable {
  /**
   * 兑换券-Id
   */
  private String id;

  private boolean deprecated;

  /**
   * 兑换券名称
   */
  private String name;

  /**
   * 兑换券可以兑换的项目
   */
  private List<VoucherItem> voucherItemList = new ArrayList<>();

  /**
   * 兑换券描述
   */
  private String description;

  /**
   * 兑换券中可用的兑换值
   */
  private int score;

  /**
   * 添加兑换项
   * 
   * @param voucherItem
   */
  public void addVoucherItem(VoucherItem voucherItem) {
    if (voucherItem != null) {
      this.voucherItemList.add(voucherItem);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeByte((byte) (deprecated ? 1 : 0));
    dest.writeString(name);
    dest.writeParcelableArray(voucherItemList.toArray(new VoucherItem[voucherItemList.size()]), flags);
    dest.writeString(description);
    dest.writeInt(score);
  }

  public GoodVoucher() {
  }

  protected GoodVoucher(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    name = in.readString();
    voucherItemList = new ArrayList<>();
    for (Parcelable p : in.readParcelableArray(VoucherItem.class.getClassLoader())) {
      voucherItemList.add((VoucherItem) p);
    }
    description = in.readString();
    score = in.readInt();
  }

  public static final Creator<GoodVoucher> CREATOR = new Creator<GoodVoucher>() {
    @Override
    public GoodVoucher createFromParcel(Parcel in) {
      return new GoodVoucher(in);
    }

    @Override
    public GoodVoucher[] newArray(int size) {
      return new GoodVoucher[size];
    }
  };
}