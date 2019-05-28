package cn.aijiamuyingfang.server.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.user.domain.RecieveAddress;
import cn.aijiamuyingfang.server.user.domain.User;
import cn.aijiamuyingfang.server.user.domain.response.GetUserPhoneResponse;
import cn.aijiamuyingfang.server.user.service.UserService;

/**
 * [描述]:
 * <p>
 * 提供用户服务的Controller
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-01 14:54:03
 */
@RestController
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 获取用户
   * 
   * @param userid
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userid.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{userid}")
  public User getUser(@PathVariable("userid") String userid) {
    return getUserById(userid);
  }

  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping("/user/register")
  public User registerUser(@RequestBody User user) {
    return userService.registerUser(user);

  }

  /**
   * 获取用户( 供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param userid
   * @return
   */
  @GetMapping(value = "/users-anon/internal/user/{userid}")
  public User getUserById(@PathVariable("userid") String userid) {
    return userService.getUser(userid);
  }

  /**
   * 获取用户手机号
   * 
   * @param userid
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/user/{userid}/phone")
  public GetUserPhoneResponse getUserPhone(@PathVariable("userid") String userid) {
    return userService.getUserPhone(userid);
  }

  /**
   * 更新用户信息
   * 
   * @param userid
   * @param user
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}")
  public User updateUser(@PathVariable("userid") String userid, @RequestBody User user) {
    if (null == user) {
      throw new IllegalArgumentException("update user request body is null");
    }
    return userService.updateUser(userid, user);
  }

  /**
   * 获取用户收件地址
   * 
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/recieveaddress")
  public List<RecieveAddress> getUserRecieveAddressList(@PathVariable("userid") String userid) {
    return userService.getUserRecieveAddressList(userid);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param userid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{userid}/recieveaddress")
  public RecieveAddress addUserRecieveAddress(@PathVariable("userid") String userid,
      @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("add user recieveaddress request body is null");
    }
    if (StringUtils.isEmpty(request.getPhone())) {
      throw new IllegalArgumentException("recieveaddress phone is empty");
    }
    return userService.addUserRecieveAddress(userid, request);
  }

  /**
   * 获取收件地址
   * 
   * @param userid
   * @param addressid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public RecieveAddress getRecieveAddress(@PathVariable("userid") String userid,
      @PathVariable("addressid") String addressid) {
    return userService.getRecieveAddress(userid, addressid);
  }

  /**
   * 更新收件地址信息
   * 
   * @param userid
   * @param addressid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public RecieveAddress updateRecieveAddress(@PathVariable("userid") String userid,
      @PathVariable("addressid") String addressid, @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("update recieveaddress request body is null");
    }
    return userService.updateRecieveAddress(userid, addressid, request);
  }

  /**
   * 废弃收件地址
   * 
   * @param userid
   * @param addressid
   */
  @PreAuthorize(value = "isAuthenticated() and #userid.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public void deprecateRecieveAddress(@PathVariable("userid") String userid,
      @PathVariable("addressid") String addressid) {
    userService.deprecateRecieveAddress(userid, addressid);
  }
}
