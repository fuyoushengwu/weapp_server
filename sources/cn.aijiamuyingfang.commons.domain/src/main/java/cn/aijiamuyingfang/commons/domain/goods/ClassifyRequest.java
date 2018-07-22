package cn.aijiamuyingfang.commons.domain.goods;

import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 条目请求的请求体
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 20:07:03
 */
@MappedSuperclass
public class ClassifyRequest {

  /**
   * 条目名
   */
  protected String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
