package cn.aijiamuyingfang.server.shoporder.controller;

import cn.aijiamuyingfang.commons.domain.exception.AuthException;
import cn.aijiamuyingfang.commons.domain.exception.ShopOrderException;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItemRequest;
import cn.aijiamuyingfang.server.domain.util.ConverterService;
import cn.aijiamuyingfang.server.shoporder.service.PreviewOrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [描述]:
 * <p>
 * 预览订单服务Controller层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 23:02:13
 */
@RestController
public class PreviewOrderController {
  @Autowired
  private PreviewOrderService previeworderService;

  @Autowired
  private ConverterService converterService;

  /**
   * 更新预览的商品项
   * 
   * @param headerUserId
   * @param userid
   * @param itemid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/previeworder/item/{itemid}")
  public PreviewOrderItem updatePreviewOrderItem(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("itemid") String itemid,
      @RequestBody PreviewOrderItemRequest request) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission update other user's preview item");
    }
    if (null == request) {
      throw new ShopOrderException("400", "update previeworderitem request is null");
    }
    return previeworderService.updatePreviewOrderItem(userid, itemid, converterService.from(request));
  }

  /**
   * 删除预览的商品项
   * 
   * @param headerUserId
   * @param userid
   * @param itemid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @DeleteMapping(value = "/user/{userid}/previeworder/item/{itemid}")
  public void deletePreviewOrderItem(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("itemid") String itemid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission delete other user's preview item");
    }
    previeworderService.deletePreviewOrderItem(userid, itemid);
  }

  /**
   * 生成用户的预览订单
   * 
   * @param headerUserId
   * @param userid
   * @param goodids
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/previeworder")
  public PreviewOrder generatePreviewOrder(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestParam(name = "goodids", required = false) List<String> goodids) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission generate other user's preview");
    }
    return previeworderService.generatePreviewOrder(userid, goodids);
  }
}
