package cn.aijiamuyingfang.server.shoporder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrder;
import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrderItem;
import cn.aijiamuyingfang.server.shoporder.service.PreviewOrderService;

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

  /**
   * 更新预览的商品项
   * 
   * @param userid
   * @param previewitemId
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/previeworder/item/{previewitemId}")
  public PreviewOrderItem updatePreviewOrderItem(@PathVariable("userid") String userid,
      @PathVariable("previewitemId") String previewitemId, @RequestBody PreviewOrderItem request) {
    if (null == request) {
      throw new IllegalArgumentException("update previeworderitem request is null");
    }
    return previeworderService.updatePreviewOrderItem(userid, previewitemId, request);
  }

  /**
   * 删除预览的商品项
   * 
   * @param userid
   * @param previewItemId
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{userid}/previeworder/item/{previewItemId}")
  public void deletePreviewOrderItem(@PathVariable("userid") String userid,
      @PathVariable("previewItemId") String previewItemId) {
    previeworderService.deletePreviewOrderItem(userid, previewItemId);
  }

  /**
   * 生成用户的预览订单
   * 
   * @param userid
   * @param goodids
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/previeworder")
  public PreviewOrder generatePreviewOrder(@PathVariable("userid") String userid,
      @RequestParam(name = "goodids", required = false) List<String> goodids) {
    return previeworderService.generatePreviewOrder(userid, goodids);
  }
}
