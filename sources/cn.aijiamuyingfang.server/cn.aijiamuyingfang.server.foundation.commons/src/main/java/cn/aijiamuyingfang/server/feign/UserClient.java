package cn.aijiamuyingfang.server.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.user.User;

@FeignClient("user-service")
public interface UserClient {

  /**
   * 查询用户信息( 供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param username
   * @return
   */
  @GetMapping("/users-anon/internal/user")
  ResponseBean<User> getUserInternal(@RequestParam(value = "username") String username);

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
   * @param username
   * @param user
   * @return
   */
  @PutMapping(value = "/user/{username}")
  ResponseBean<User> updateUser(@PathVariable("username") String username, @RequestBody User user);

  /**
   * 获取用户收件地址
   * 
   * @param username
   * @return
   */
  @GetMapping(value = "/user/{username}/recieveaddress")
  ResponseBean<List<RecieveAddress>> getUserRecieveAddressList(@PathVariable("username") String username);

  /**
   * 获取收件地址
   * 
   * @param username
   * @param addressId
   * @return
   */
  @GetMapping(value = "/user/{username}/recieveaddress/{address_id}")
  ResponseBean<RecieveAddress> getRecieveAddress(@PathVariable("username") String username,
      @PathVariable("address_id") String addressId);
}
