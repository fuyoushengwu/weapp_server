package cn.aijiamuyingfang.commons.domain.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 保质期
 */
public class ShelfLife {
  /**
   * 保质期-开始时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date start;

  /**
   * 保质期-结束时间
   */
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date end;

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

}