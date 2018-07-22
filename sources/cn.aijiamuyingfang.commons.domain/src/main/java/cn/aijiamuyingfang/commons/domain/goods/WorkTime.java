package cn.aijiamuyingfang.commons.domain.goods;

import cn.aijiamuyingfang.commons.utils.StringUtils;

/**
 * 营业时间
 */
public class WorkTime {
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
    if (StringUtils.hasContent(updateWorktime.start)) {
      this.start = updateWorktime.start;
    }
    if (StringUtils.hasContent(updateWorktime.end)) {
      this.end = updateWorktime.end;
    }
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

}
