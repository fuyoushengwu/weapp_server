package cn.aijiamuyingfang.server.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.user.RecieveAddress;
import cn.aijiamuyingfang.server.feign.domain.user.User;

@FeignClient("user-service")
public interface UserClient {

  /**
   * 查询用户信息
   * 
   * @param userid
   * @return
   */
  @GetMapping("/users-anon/internal/user/{userid}")
  ResponseBean<User> getUserById(@PathVariable("userid") String userid);

  /**
   * 更新用户信息
   * 
   * @param userid
   * @param user
   * @return
   */
  @PutMapping(value = "/user/{userid}")
  ResponseBean<User> updateUser(@PathVariable("userid") String userid, @RequestBody User user);

  /**
   * 获取用户收件地址
   * 
   * @param userid
   * @return
   */
  @GetMapping(value = "/user/{userid}/recieveaddress")
  ResponseBean<List<RecieveAddress>> getUserRecieveAddressList(@PathVariable("userid") String userid);

  /**
   * 获取收件地址
   * 
   * @param userid
   * @param addressid
   * @return
   */
  @GetMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  ResponseBean<RecieveAddress> getRecieveAddress(@PathVariable("userid") String userid,
      @PathVariable("addressid") String addressid);
}
