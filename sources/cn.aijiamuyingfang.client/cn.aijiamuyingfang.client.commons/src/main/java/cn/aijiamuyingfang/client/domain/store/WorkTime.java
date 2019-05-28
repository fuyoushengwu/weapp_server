package cn.aijiamuyingfang.client.domain.store;

import lombok.Data;

/**
 * 营业时间
 */
@Data
public class WorkTime {
  /**
   * 开始时间
   */
  private String start;

  /**
   * 结束时间
   */
  private String end;
}
