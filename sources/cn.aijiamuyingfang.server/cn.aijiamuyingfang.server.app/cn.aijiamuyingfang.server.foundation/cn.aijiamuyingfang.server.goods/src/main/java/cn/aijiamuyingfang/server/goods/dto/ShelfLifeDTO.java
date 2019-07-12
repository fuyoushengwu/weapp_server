package cn.aijiamuyingfang.server.goods.dto;

import lombok.Data;

/**
 * 保质期
 */
@Data
public class ShelfLifeDTO {
  /**
   * 保质期-开始时间
   */
  private String start;

  /**
   * 保质期-结束时间
   */
  private String end;
}