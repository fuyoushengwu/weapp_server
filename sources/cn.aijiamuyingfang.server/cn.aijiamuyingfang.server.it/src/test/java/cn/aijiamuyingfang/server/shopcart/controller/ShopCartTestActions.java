package cn.aijiamuyingfang.server.shopcart.controller;

import cn.aijiamuyingfang.commons.domain.shopcart.AddShopCartItemRequest;
import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.server.client.AbstractTestAction;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-19 17:52:51
 */
@Service
public class ShopCartTestActions extends AbstractTestAction {

  public String itemoneId;

  public ShopCartItem addGoodOne10() throws IOException {
    AddShopCartItemRequest request = new AddShopCartItemRequest();
    request.setGoodid(goodoneId);
    request.setGoodNum(10);
    ShopCartItem item = shopcartControllerClient.addShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, request);
    if (item != null) {
      itemoneId = item.getId();
    }
    return item;
  }

  public String itemtwoId;

  public ShopCartItem addGoodTwo10() throws IOException {
    AddShopCartItemRequest request = new AddShopCartItemRequest();
    request.setGoodid(goodtwoId);
    request.setGoodNum(10);
    ShopCartItem item = shopcartControllerClient.addShopCartItem(ADMIN_USER_TOKEN, ADMIN_USER_ID, request);
    if (item != null) {
      itemtwoId = item.getId();
    }
    return item;
  }

  public void deleteItemOne() throws IOException {
    shopcartControllerClient.deleteGood(ADMIN_USER_TOKEN, goodoneId, false);
  }

  @Override
  public void clearData() {
    super.clearData();
    itemoneId = null;
  }
}
