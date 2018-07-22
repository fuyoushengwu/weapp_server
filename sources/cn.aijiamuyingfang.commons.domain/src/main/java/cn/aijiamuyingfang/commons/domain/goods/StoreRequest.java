package cn.aijiamuyingfang.commons.domain.goods;

import cn.aijiamuyingfang.commons.domain.address.StoreAddressRequest;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * [描述]:
 * <p>
 * 创建和更新Store的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 22:57:32
 */
@MappedSuperclass
public class StoreRequest {

  /**
   * 门店名
   */
  protected String name;

  /**
   * 营业时间
   */
  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "start", column = @Column(name = "start_worktime")),
      @AttributeOverride(name = "end", column = @Column(name = "end_worktime")) })
  protected WorkTime workTime;

  /**
   * 门店地址
   */
  @Transient
  protected StoreAddressRequest storeaddressRequest;

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

  public StoreAddressRequest getStoreaddressRequest() {
    return storeaddressRequest;
  }

  public void setStoreaddressRequest(StoreAddressRequest storeaddressRequest) {
    this.storeaddressRequest = storeaddressRequest;
  }

}
