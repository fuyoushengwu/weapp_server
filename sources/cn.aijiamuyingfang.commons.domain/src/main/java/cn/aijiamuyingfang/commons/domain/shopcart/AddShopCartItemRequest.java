package cn.aijiamuyingfang.commons.domain.shopcart;

/**
 * [描述]:
 * <p>
 * <code>POST '/user/{userid}/shopcart'</code>请求的参数
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 21:36:36
 */
public class AddShopCartItemRequest {
  /**
   * 商品ID
   */
  private String goodid;

  /**
   * 商品数量
   */
  private int goodNum;

  public String getGoodid() {
    return goodid;
  }

  public void setGoodid(String goodid) {
    this.goodid = goodid;
  }

  public int getGoodNum() {
    return goodNum;
  }

  public void setGoodNum(int goodNum) {
    this.goodNum = goodNum;
  }

}
