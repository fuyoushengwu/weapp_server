package cn.aijiamuyingfang.server.dto.address;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 位置坐标
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 17:50:27
 */
@Data
public class CoordinateDTO {
  /**
   * 位置-纬度
   */
  private double latitude;

  /**
   * 位置-经度
   */
  private double longitude;
}
