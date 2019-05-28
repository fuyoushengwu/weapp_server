package cn.aijiamuyingfang.client.domain.store;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.client.domain.ImageSource;
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
public class Store {

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

}
