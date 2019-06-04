package cn.aijiamuyingfang.server.domain.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 分页请求的返回类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 16:55:59
 */
@Getter
@Setter
public abstract class PageResponse<T> {
  /**
   * 当前页的数据
   */
  private List<T> dataList = new ArrayList<>();

  /**
   * 当前页数
   */
  private int currentPage;

  /**
   * 总页数
   */
  private int totalpage;

}
