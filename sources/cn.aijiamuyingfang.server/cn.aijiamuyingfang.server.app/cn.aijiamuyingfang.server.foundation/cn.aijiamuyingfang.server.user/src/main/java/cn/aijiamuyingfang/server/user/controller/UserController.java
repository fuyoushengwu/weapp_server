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
import org.springframework.web.bind.annotation.RequestParam;
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
   * @param userId
   * @return
   */
  @PreAuthorize(
      value = "isAuthenticated() and (#userId.equals(getAuthentication().getName()) or hasAnyAuthority('permission:manager:*','permission:sender:*'))")
  @GetMapping(value = "/user/{user_id}")
  public User getUser(@PathVariable("user_id") String userId) {
    return getUserInternal(userId, null);
  }

  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping("/user/register")
  public User registerUser(@RequestBody User user) {
    return userService.registerUser(user);
  }

  /**
   * 获取用户( 供系统内部其它服务使用的,不需要鉴权)
   * 
   * @param userId
   * @return
   */
  @GetMapping(value = "/users-anon/internal/user")
  public User getUserInternal(@RequestParam(value = "user_id", required = false) String userId,
      @RequestParam(value = "openid", required = false) String openid) {
    if (StringUtils.hasContent(userId)) {
      return userService.getUser(userId);
    }
    if (StringUtils.hasContent(openid)) {
      return userService.getUserByOpenid(openid);
    }
    throw new IllegalArgumentException("userId or openid must provided one");
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
   * @param userId
   * @return
   */
  @PreAuthorize(value = "hasAnyAuthority('permission:manager:*','permission:sender:*')")
  @GetMapping(value = "/user/{user_id}/phone")
  public GetUserPhoneResponse getUserPhone(@PathVariable("user_id") String userId) {
    return userService.getUserPhone(userId);
  }

  /**
   * 更新用户信息
   * 
   * @param userId
   * @param user
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}")
  public User updateUser(@PathVariable("user_id") String userId, @RequestBody User user) {
    if (null == user) {
      throw new IllegalArgumentException("update user request body is null");
    }
    return userService.updateUser(userId, user);
  }

  /**
   * 获取用户收件地址
   * 
   * @param userId
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/recieveaddress")
  public List<RecieveAddress> getUserRecieveAddressList(@PathVariable("user_id") String userId) {
    return userService.getUserRecieveAddressList(userId);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param userId
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PostMapping(value = "/user/{user_id}/recieveaddress")
  public RecieveAddress addUserRecieveAddress(@PathVariable("user_id") String userId,
      @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("add user recieveaddress request body is null");
    }
    if (StringUtils.isEmpty(request.getPhone())) {
      throw new IllegalArgumentException("recieveaddress phone is empty");
    }
    return userService.addUserRecieveAddress(userId, request);
  }

  /**
   * 获取收件地址
   * 
   * @param userId
   * @param addressId
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @GetMapping(value = "/user/{user_id}/recieveaddress/{address_id}")
  public RecieveAddress getRecieveAddress(@PathVariable("user_id") String userId,
      @PathVariable("address_id") String addressId) {
    return userService.getRecieveAddress(userId, addressId);
  }

  /**
   * 更新收件地址信息
   * 
   * @param userId
   * @param addressId
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @PutMapping(value = "/user/{user_id}/recieveaddress/{address_id}")
  public RecieveAddress updateRecieveAddress(@PathVariable("user_id") String userId,
      @PathVariable("address_id") String addressId, @RequestBody RecieveAddress request) {
    if (null == request) {
      throw new IllegalArgumentException("update recieveaddress request body is null");
    }
    return userService.updateRecieveAddress(userId, addressId, request);
  }

  /**
   * 废弃收件地址
   * 
   * @param userId
   * @param addressId
   */
  @PreAuthorize(value = "isAuthenticated() and #userId.equals(getAuthentication().getName())")
  @DeleteMapping(value = "/user/{user_id}/recieveaddress/{address_id}")
  public void deprecateRecieveAddress(@PathVariable("user_id") String userId,
      @PathVariable("address_id") String addressId) {
    userService.deprecateRecieveAddress(userId, addressId);
  }
}
