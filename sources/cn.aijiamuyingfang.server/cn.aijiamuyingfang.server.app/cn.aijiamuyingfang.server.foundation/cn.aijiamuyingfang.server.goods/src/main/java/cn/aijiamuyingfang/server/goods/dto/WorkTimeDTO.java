package cn.aijiamuyingfang.server.goods.dto;

import cn.aijiamuyingfang.vo.store.WorkTime;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;

/**
 * 营业时间
 */
@Data
public class WorkTimeDTO {
  /**
   * 开始时间
   */
  private String start;

  /**
   * 结束时间
   */
  private String end;

  public void update(WorkTime updateWorktime) {
    if (null == updateWorktime) {
      return;
    }
    if (StringUtils.hasContent(updateWorktime.getStart())) {
      this.setStart(updateWorktime.getStart());
    }
    if (StringUtils.hasContent(updateWorktime.getEnd())) {
      this.setEnd(updateWorktime.getEnd());
    }
  }
}
