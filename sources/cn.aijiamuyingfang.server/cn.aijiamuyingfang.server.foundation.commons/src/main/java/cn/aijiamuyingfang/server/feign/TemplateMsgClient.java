package cn.aijiamuyingfang.server.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.message.TemplateMsg;

@FeignClient(name = "user-service")
public interface TemplateMsgClient {
  /**
   * 发送预定信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/preorder")
  ResponseBean<Void> sendPreOrderMsg(@PathVariable(name = "username") String username,
      @RequestBody TemplateMsg msgData);

  /**
   * 发送自提信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/pickup")
  ResponseBean<Void> sendPickupMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData);

  /**
   * 发送第三方配送信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/thirdsend")
  ResponseBean<Void> sendThirdSendMsg(@PathVariable(name = "username") String username,
      @RequestBody TemplateMsg msgData);

  /**
   * 发送自己配送信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/ownsend")
  ResponseBean<Void> sendOwnSendMsg(@PathVariable(name = "username") String username, @RequestBody TemplateMsg msgData);

  /**
   * 发送订单超时信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/overtime")
  ResponseBean<Void> sendOrderOverTimeMsg(@PathVariable(name = "username") String username,
      @RequestBody TemplateMsg msgData);

  /**
   * 发送订单确认信息
   * 
   * @param username
   * @param msgData
   */
  @PostMapping(value = "/users-anon/internal/user/{username}/wxservice/templatemsg/orderconfirm")
  ResponseBean<Void> sendOrderConfirmMsg(@PathVariable(name = "username") String username,
      @RequestBody TemplateMsg msgData);
}
