package cn.aijiamuyingfang.server.goods.dto;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 营业时间
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkTimeDTO {
  /**
   * 开始时间
   */
  private String start;

  /**
   * 结束时间
   */
  private String end;

  public void update(WorkTimeDTO updateWorktime) {
    if (null == updateWorktime) {
      return;
    }
    if (StringUtils.hasContent(updateWorktime.start)) {
      this.start = updateWorktime.start;
    }
    if (StringUtils.hasContent(updateWorktime.end)) {
      this.end = updateWorktime.end;
    }
  }
}
