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

import org.hibernate.annotations.GenericGenerator;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
public class Store {

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
   * 封面图片
   */
  @ManyToOne
  private ImageSource coverImg;

  /**
   * 详细图片地址
   */
  @ManyToMany
  private List<ImageSource> detailImgList = new ArrayList<>();

  /**
   * 门店地址
   */
  @OneToOne(cascade = CascadeType.ALL)
  private StoreAddress storeAddress;

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
    if (StringUtils.hasContent(updateStore.name)) {
      this.name = updateStore.name;
    }
    if (updateStore.workTime != null) {
      this.workTime.update(updateStore.workTime);
    }
    if (updateStore.coverImg != null) {
      this.coverImg.update(updateStore.coverImg);
    }
    if (updateStore.detailImgList != null && !updateStore.detailImgList.isEmpty()) {
      this.detailImgList = updateStore.detailImgList;
    }
    if (updateStore.storeAddress != null) {
      this.storeAddress = updateStore.storeAddress;
    }
  }
}
