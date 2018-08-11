package cn.aijiamuyingfang.commons.domain.coupon;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

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
@Entity
public class GoodVoucher implements Parcelable {
  /**
   * 兑换券-Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  private boolean deprecated;

  /**
   * 兑换券名称
   */
  private String name;

  /**
   * 兑换券可以兑换的项目
   */
  @ElementCollection
  private List<String> voucheritemIdList = new ArrayList<>();

  /**
   * 兑换券描述
   */
  private String description;

  /**
   * 兑换券中可用的兑换值
   */
  private int score;

  /**
   * 使用提供的GoodVoucher更新本商品兑换券信息
   * 
   * @param updateVoucher
   */
  public void update(GoodVoucher updateVoucher) {
    if (null == updateVoucher) {
      return;
    }
    if (StringUtils.hasContent(updateVoucher.name)) {
      this.name = updateVoucher.name;
    }
    if (StringUtils.hasContent(updateVoucher.description)) {
      this.description = updateVoucher.description;
    }
    if (updateVoucher.score != 0) {
      this.score = updateVoucher.score;
    }
    if (!CollectionUtils.isEmpty(updateVoucher.voucheritemIdList)) {
      this.voucheritemIdList = updateVoucher.voucheritemIdList;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getVoucheritemIdList() {
    return voucheritemIdList;
  }

  public void setVoucheritemIdList(List<String> voucheritemIdList) {
    this.voucheritemIdList = voucheritemIdList;
  }

  public void addVoucheritemId(String voucheritemId) {
    if (StringUtils.hasContent(voucheritemId)) {
      this.voucheritemIdList.add(voucheritemId);
    }

  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GoodVoucher other = (GoodVoucher) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
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
    dest.writeStringList(voucheritemIdList);
    dest.writeString(description);
    dest.writeInt(score);
  }

  public GoodVoucher() {

  }

  protected GoodVoucher(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    name = in.readString();
    in.readStringList(voucheritemIdList);
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
