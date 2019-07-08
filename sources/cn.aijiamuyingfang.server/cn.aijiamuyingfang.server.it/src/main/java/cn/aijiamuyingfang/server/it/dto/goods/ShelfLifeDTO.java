package cn.aijiamuyingfang.server.it.dto.goods;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 保质期
 */
@Getter
@Setter
@NoArgsConstructor
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