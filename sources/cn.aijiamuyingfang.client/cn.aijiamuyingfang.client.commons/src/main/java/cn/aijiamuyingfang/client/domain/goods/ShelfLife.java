package cn.aijiamuyingfang.client.domain.goods;

import lombok.Data;

/**
 * 保质期
 */
@Data
public class ShelfLife {
  /**
   * 保质期-开始时间
   */
  private String start;

  /**
   * 保质期-结束时间
   */
  private String end;
}