package cn.aijiamuyingfang.server.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.user.dto.RecieveAddress;
import cn.aijiamuyingfang.server.user.dto.User;
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
   * @param username
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{username}")
  public User getUser(@PathVariable("username") String username) {
    return getUserInternal(username);
  }

  /**
   * 获取用户
   * 
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user")
  public User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof OAuth2Authentication)) {
      throw new IllegalArgumentException("username is empty");
    }
    Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
    Object principal = userAuthentication.getPrincipal();
    if (null == principal) {
      throw new IllegalArgumentException("username is empty");
    }
    return userService.getUser(principal.toString());
  }

  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping("/user/register")
  public User registerUser(@RequestBody User user) {
    return userService.registerUser(user);
  }

  /**
   * 获取用户( 供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param username
   * @return
   */
  @GetMapping(value = "/users-anon/internal/user")
  public User getUserInternal(@RequestParam(value = "username") String username) {
    return userService.getUser(username);
  }

  /**
   * 注册用户(供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param user
   * @return
   */
  @PostMapping(value = "/users-anon/internal/user")
  public User registerUserInternal(@RequestBody User user) {
    return userService.registerUser(user);
  }

  /**
   * 获取用户手机号
   *
   * @param username
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/user/{username}/phone", produces = { "application/json" })
  public String getUserPhone(@PathVariable("username") String username) {
    return userService.getUserPhone(username);
  }

  /**
   * 更新用户信息
   * 
   * @param username
   * @param user
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}")
  public User updateUser(@PathVariable("username") String username, @RequestBody User user) {
    if (null == user) {
      throw new IllegalArgumentException("update user request body is null");
    }
    return userService.updateUser(username, user);
  }

  /**
   * 获取用户收件地址
   * 
   * @param username
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{username}/recieveaddress")
  public List<RecieveAddress> getUserRecieveAddressList(@PathVariable("username") String username) {
    return userService.getUserRecieveAddressList(username);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param username
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{username}/recieveaddress")
  public RecieveAddress addUserRecieveAddress(@PathVariable("username") String username,
      @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("add user recieveaddress request body is null");
    }
    if (StringUtils.isEmpty(request.getPhone())) {
      throw new IllegalArgumentException("recieveaddress phone is empty");
    }
    return userService.addUserRecieveAddress(username, request);
  }

  /**
   * 获取收件地址
   * 
   * @param username
   * @param addressId
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#username.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{username}/recieveaddress/{address_id}")
  public RecieveAddress getRecieveAddress(@PathVariable("username") String username,
      @PathVariable("address_id") String addressId) {
    return userService.getRecieveAddress(username, addressId);
  }

  /**
   * 更新收件地址信息
   * 
   * @param username
   * @param addressId
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{username}/recieveaddress/{address_id}")
  public RecieveAddress updateRecieveAddress(@PathVariable("username") String username,
      @PathVariable("address_id") String addressId, @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("update recieveaddress request body is null");
    }
    return userService.updateRecieveAddress(username, addressId, request);
  }

  /**
   * 废弃收件地址
   * 
   * @param username
   * @param addressId
   */
  @PreAuthorize(value = "isAuthenticated() and #username.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{username}/recieveaddress/{address_id}")
  public void deprecateRecieveAddress(@PathVariable("username") String username,
      @PathVariable("address_id") String addressId) {
    userService.deprecateRecieveAddress(username, addressId);
  }
}
