package cn.aijiamuyingfang.server.user.controller;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.address.RecieveAddressRequest;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.exception.UserException;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.UserRequest;
import cn.aijiamuyingfang.server.domain.util.ConverterService;
import cn.aijiamuyingfang.server.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  private ConverterService converterService;

  /**
   * 获取用户
   * 
   * @param headerUserId
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}")
  public User getUser(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's info");
    }
    return userService.getUser(userid);
  }

  /**
   * 更新用户信息
   * 
   * @param headerUserId
   * @param userid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}")
  public User updateUser(@RequestHeader("userid") String headerUserId, @PathVariable("userid") String userid,
      @RequestBody UserRequest request) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission change other user's info");
    }
    if (null == request) {
      throw new UserException("400", "update user request body is null");
    }
    return userService.updateUser(userid, converterService.from(request));
  }

  /**
   * 获取用户收件地址
   * 
   * @param headerUserId
   * @param userid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/recieveaddress")
  public List<RecieveAddress> getUserRecieveAddressList(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's recieve addresses");
    }
    return userService.getUserRecieveAddressList(userid);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param headerUserId
   * @param userid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PostMapping(value = "/user/{userid}/recieveaddress")
  public RecieveAddress addUserRecieveAddress(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @RequestBody RecieveAddressRequest request) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission add recieve address to other user");
    }
    if (null == request) {
      throw new UserException("400", "add user recieveaddress request body is null");
    }
    if (StringUtils.isEmpty(request.getPhone())) {
      throw new UserException("400", "recieveaddress phone is empty");
    }
    return userService.addUserRecieveAddress(userid, converterService.from(request));
  }

  /**
   * 获取收件地址
   * 
   * @param headerUserId
   * @param userid
   * @param addressid
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @GetMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public RecieveAddress getRecieveAddress(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("addressid") String addressid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission get other user's recieve address");
    }
    return userService.getRecieveAddress(userid, addressid);
  }

  /**
   * 更新收件地址信息
   * 
   * @param headerUserId
   * @param userid
   * @param addressid
   * @param request
   * @return
   */
  @PreAuthorize(value = "isAuthenticated()")
  @PutMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public RecieveAddress updateRecieveAddress(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("addressid") String addressid,
      @RequestBody RecieveAddressRequest request) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission change other user's recieve address");
    }
    if (null == request) {
      throw new UserException("400", "update recieveaddress request body is null");
    }
    return userService.updateRecieveAddress(userid, addressid, converterService.from(request));
  }

  /**
   * 废弃收件地址
   * 
   * @param headerUserId
   * @param userid
   * @param addressid
   */
  @PreAuthorize(value = "isAuthenticated()")
  @DeleteMapping(value = "/user/{userid}/recieveaddress/{addressid}")
  public void deprecateRecieveAddress(@RequestHeader("userid") String headerUserId,
      @PathVariable("userid") String userid, @PathVariable("addressid") String addressid) {
    if (!userid.equals(headerUserId)) {
      throw new AuthException("403", "no permission deprecate other user's recieve address");
    }
    userService.deprecateRecieveAddress(userid, addressid);
  }
}
