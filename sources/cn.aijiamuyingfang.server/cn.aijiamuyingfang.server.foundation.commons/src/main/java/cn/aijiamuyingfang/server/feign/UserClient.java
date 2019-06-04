package cn.aijiamuyingfang.server.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.user.RecieveAddress;
import cn.aijiamuyingfang.server.feign.domain.user.User;

@FeignClient("user-service")
public interface UserClient {

  /**
   * 查询用户信息( 供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param userId
   * @param openid
   * @return
   */
  @GetMapping("/users-anon/internal/user")
  ResponseBean<User> getUserInternal(@RequestParam(value = "user_id", required = false) String userId,
      @RequestParam(value = "openid", required = false) String openid);

  /**
   * 注册用户(供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param user
   * @return
   */
  @PostMapping(value = "/users-anon/internal/user")
  ResponseBean<User> registerUserInternal(@RequestBody User user);

  /**
   * 更新用户信息
   * 
   * @param userId
   * @param user
   * @return
   */
  @PutMapping(value = "/user/{user_id}")
  ResponseBean<User> updateUser(@PathVariable("user_id") String userId, @RequestBody User user);

  /**
   * 获取用户收件地址
   * 
   * @param userId
   * @return
   */
  @GetMapping(value = "/user/{user_id}/recieveaddress")
  ResponseBean<List<RecieveAddress>> getUserRecieveAddressList(@PathVariable("user_id") String userId);

  /**
   * 获取收件地址
   * 
   * @param userId
   * @param addressId
   * @return
   */
  @GetMapping(value = "/user/{user_id}/recieveaddress/{address_id}")
  ResponseBean<RecieveAddress> getRecieveAddress(@PathVariable("user_id") String userId,
      @PathVariable("address_id") String addressId);
}
