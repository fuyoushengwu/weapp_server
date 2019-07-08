package cn.aijiamuyingfang.server.it.shoporder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.db.shoporder.PreviewOrderRepository;
import cn.aijiamuyingfang.server.it.db.shoporder.ShopOrderRepository;
import cn.aijiamuyingfang.server.it.dto.shoporder.ShopOrderDTO;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-19 20:23:04
 */
@Service
public class ShoporderTestActions extends AbstractTestAction {
  @Autowired
  private ShopOrderRepository shoporderRepository;

  @Autowired
  protected PreviewOrderRepository previeworderRepository;

  @TargetDataSource(name = "weapp-shoporder")
  public void clearShopOrder() {
    shoporderRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-shoporder")
  public void clearPreviewOrder() {
    previeworderRepository.deleteAll();
  }

  @TargetDataSource(name = "weapp-shoporder")
  public ShopOrderDTO getShopOrder(String shoporderId) {
    return shoporderRepository.findOne(shoporderId);

  }

  @TargetDataSource(name = "weapp-shoporder")
  public void updateShopOrder(ShopOrderDTO shoporder) {
    shoporderRepository.saveAndFlush(shoporder);
  }
}
