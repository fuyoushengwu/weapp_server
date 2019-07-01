package cn.aijiamuyingfang.server.it.data.qinsilk;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 请求商品列表的Request
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-30 20:27:23
 */
@Data
public class GetGoodListRequest {
  /**
   * 状态:1 启用
   */
  private int isOnSale = 1;

  private boolean _search = false;

  private long nd = 1561897590178L;

  private int page = 1;

  private int rows = 100;

  private String sord = "asc";

  private long categoryIdsStr;

  private int operator = 3;
}
