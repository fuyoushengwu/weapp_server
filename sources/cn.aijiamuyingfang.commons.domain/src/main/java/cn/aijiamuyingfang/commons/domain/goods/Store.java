package cn.aijiamuyingfang.commons.domain.goods;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.commons.domain.address.StoreAddress;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * [描述]:
 * <p>
 * 店铺
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:21
 */
@Entity
public class Store implements Parcelable {

  /**
   * 门店Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 门店是否废弃(该字段用于删除门店:当需要删除门店时,设置该字段为true)
   */
  private boolean deprecated;

  /**
   * 门店名
   */
  private String name;

  /**
   * 营业时间
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "start", column = @Column(name = "start_worktime")),
      @AttributeOverride(name = "end", column = @Column(name = "end_worktime")) })
  private WorkTime workTime;

  /**
   * 封面图片地址
   */
  private String coverImg;

  /**
   * 详细图片地址
   */
  @ElementCollection
  private List<String> detailImgList = new ArrayList<>();

  /**
   * 门店地址
   */
  @OneToOne(cascade = CascadeType.ALL)
  private StoreAddress storeAddress;

  /**
   * 根据提供的Store更新本门店的信息
   * 
   * @param updateStore
   */
  public void update(Store updateStore) {
    if (null == updateStore) {
      return;
    }
    if (StringUtils.hasContent(updateStore.name)) {
      this.name = updateStore.name;
    }
    if (updateStore.workTime != null) {
      this.workTime.update(updateStore.workTime);
    }
    if (StringUtils.hasContent(updateStore.coverImg)) {
      this.coverImg = updateStore.coverImg;
    }
    if (updateStore.detailImgList != null && !updateStore.detailImgList.isEmpty()) {
      this.detailImgList = updateStore.detailImgList;
    }
    if (updateStore.storeAddress != null) {
      this.storeAddress = updateStore.storeAddress;
    }
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

  public WorkTime getWorkTime() {
    return workTime;
  }

  public void setWorkTime(WorkTime workTime) {
    this.workTime = workTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCoverImg() {
    return coverImg;
  }

  public void setCoverImg(String coverImg) {
    this.coverImg = coverImg;
  }

  public List<String> getDetailImgList() {
    return detailImgList;
  }

  public void setDetailImgList(List<String> detailImgList) {
    this.detailImgList = detailImgList;
  }

  public StoreAddress getStoreAddress() {
    return storeAddress;
  }

  public void setStoreAddress(StoreAddress storeAddress) {
    this.storeAddress = storeAddress;
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
    dest.writeParcelable(workTime, flags);
    dest.writeString(coverImg);
    dest.writeStringList(detailImgList);
    dest.writeParcelable(storeAddress, flags);
  }

  public Store() {
  }

  private Store(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    name = in.readString();
    workTime = in.readParcelable(WorkTime.class.getClassLoader());
    coverImg = in.readString();
    in.readStringList(detailImgList);
    storeAddress = in.readParcelable(StoreAddress.class.getClassLoader());
  }

  public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
    @Override
    public Store createFromParcel(Parcel in) {
      return new Store(in);
    }

    @Override
    public Store[] newArray(int size) {
      return new Store[size];
    }
  };
}
