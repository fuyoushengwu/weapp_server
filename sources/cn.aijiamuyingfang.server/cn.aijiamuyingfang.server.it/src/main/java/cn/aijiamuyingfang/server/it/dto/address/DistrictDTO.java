package cn.aijiamuyingfang.server.it.dto.address;

import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class DistrictDTO {
  /**
   * 行政区名
   */
  protected String name;

  /**
   * 行政区编码
   */
  protected String code;
}
