package cn.aijiamuyingfang.server.it.dto.goods;

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
}
