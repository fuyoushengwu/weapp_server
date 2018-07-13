package cn.aijiamuyingfang.server.domain.address;

import javax.persistence.MappedSuperclass;

/**
 * [描述]:
 * <p>
 * 行政区<br>
 * 该类是为了修正代码检查中的Province、City、County、Town出现的代码重复问题,<br>
 * 把它们的公共部分抽取到该类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 02:57:25
 */
@MappedSuperclass
public abstract class District {
  /**
   * 行政区名
   */
  protected String name;

  /**
   * 行政区编码
   */
  protected String code;

  public District() {
  }

  public District(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
