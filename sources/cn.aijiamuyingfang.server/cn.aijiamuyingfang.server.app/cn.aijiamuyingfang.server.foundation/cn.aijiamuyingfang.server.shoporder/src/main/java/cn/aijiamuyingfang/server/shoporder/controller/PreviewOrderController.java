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

import cn.aijiamuyingfang.server.shoporder.service.PreviewOrderService;
import cn.aijiamuyingfang.vo.review.PreviewOrder;
import cn.aijiamuyingfang.vo.review.PreviewOrderItem;

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
   * @param username
   * @param previewItemId
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}/previeworder/item/{preview_item_id}")
  public PreviewOrderItem updatePreviewOrderItem(@PathVariable("username") String username,
      @PathVariable("preview_item_id") String previewItemId, @RequestBody PreviewOrderItem request) {
    if (null == request) {
      throw new IllegalArgumentException("update previeworderitem request is null");
    }
    return previeworderService.updatePreviewOrderItem(username, previewItemId, request);
  }

  /**
   * 删除预览的商品项
   * 
   * @param username
   * @param previewItemId
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{username}/previeworder/item/{previewItemId}")
  public void deletePreviewOrderItem(@PathVariable("username") String username,
      @PathVariable("previewItemId") String previewItemId) {
    previeworderService.deletePreviewOrderItem(username, previewItemId);
  }

  /**
   * 生成用户的预览订单
   * 
   * @param username
   * @param goodIdList
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{username}/previeworder")
  public PreviewOrder generatePreviewOrder(@PathVariable("username") String username,
      @RequestParam(name = "good_id", required = false) List<String> goodIdList) {
    return previeworderService.generatePreviewOrder(username, goodIdList);
  }
}
