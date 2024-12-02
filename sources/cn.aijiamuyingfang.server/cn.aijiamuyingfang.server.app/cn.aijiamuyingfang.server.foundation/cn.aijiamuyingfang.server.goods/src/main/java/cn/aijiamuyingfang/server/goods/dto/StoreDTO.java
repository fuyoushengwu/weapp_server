package cn.aijiamuyingfang.server.goods.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;
import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.utils.StringUtils;
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
@Entity(name = "store")
@Data
public class StoreDTO {

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
  private WorkTimeDTO workTime;

  /**
   * 封面图片
   */
  @ManyToOne
  private ImageSourceDTO coverImg;

  /**
   * 详细图片地址
   */
  @ManyToMany
  private List<ImageSourceDTO> detailImgList = new ArrayList<>();

  /**
   * 门店地址
   */
  @OneToOne(cascade = CascadeType.ALL)
  private StoreAddressDTO storeAddress;

  /**
   * 根据提供的Store更新本门店的信息
   * 
   * @param updateStore
   *          要更新的门店信息
   */
  public void update(Store updateStore) {
    if (null == updateStore) {
      return;
    }
    if (StringUtils.hasContent(updateStore.getName())) {
      this.setName(updateStore.getName());
    }
    if (updateStore.getWorkTime() != null && this.workTime != null) {
      this.workTime.update(updateStore.getWorkTime());
    }
    if (updateStore.getCoverImg() != null) {
      this.setCoverImg(ConvertUtils.convertImageSource(updateStore.getCoverImg()));
    }
    if (CollectionUtils.isNotEmpty(updateStore.getDetailImgList())) {
      this.setDetailImgList(ConvertUtils.convertImageSourceList(updateStore.getDetailImgList()));
    }
    if (updateStore.getStoreAddress() != null) {
      this.setStoreAddress(ConvertUtils.convertStoreAddress(updateStore.getStoreAddress()));
    }
  }
}
