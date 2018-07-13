package cn.aijiamuyingfang.server.domain;

import java.util.ArrayList;
import java.util.List;

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
public abstract class PageResponse<T> {
  /**
   * 当前页的数据
   */
  private List<T> dataList = new ArrayList<>();

  /**
   * 当前页数
   */
  private int currentpage;

  /**
   * 总页数
   */
  private int totalpage;

  public List<T> getDataList() {
    return dataList;
  }

  public void setDataList(List<T> dataList) {
    this.dataList = dataList;
  }

  public int getCurrentpage() {
    return currentpage;
  }

  public void setCurrentpage(int currentpage) {
    this.currentpage = currentpage;
  }

  public int getTotalpage() {
    return totalpage;
  }

  public void setTotalpage(int totalpage) {
    this.totalpage = totalpage;
  }

}
