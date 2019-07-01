package cn.aijiamuyingfang.server.it.data.qinsilk;

import java.util.List;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 秦丝返回数据
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-30 20:13:47
 */
@Data
public class ResponseBean<R> {
  private int page;

  private int pageSize;

  private int records;

  private int total;

  private List<R> rows;
}
