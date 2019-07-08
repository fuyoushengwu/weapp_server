package cn.aijiamuyingfang.vo.store;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import cn.aijiamuyingfang.vo.ImageSource;
import lombok.Data;

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
@Data
public class Store implements Parcelable {

  /**
   * 门店Id
   */
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
  private WorkTime workTime;

  /**
   * 封面图片
   */
  private ImageSource coverImg;

  /**
   * 详细图片地址
   */
  private List<ImageSource> detailImgList = new ArrayList<>();

  /**
   * 门店地址
   */
  private StoreAddress storeAddress;

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
    dest.writeParcelable(coverImg, flags);
    dest.writeParcelableArray(detailImgList.toArray(new ImageSource[detailImgList.size()]), flags);
    dest.writeParcelable(storeAddress, flags);
  }

  public Store() {
  }

  private Store(Parcel in) {
    id = in.readString();
    deprecated = in.readByte() != 0;
    name = in.readString();
    workTime = in.readParcelable(WorkTime.class.getClassLoader());
    coverImg = in.readParcelable(ImageSource.class.getClassLoader());
    detailImgList = new ArrayList<>();
    for (Parcelable p : in.readParcelableArray(ImageSource.class.getClassLoader())) {
      detailImgList.add((ImageSource) p);
    }
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
