package cn.aijiamuyingfang.server.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.UserAuthority;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.UserException;
import cn.aijiamuyingfang.server.user.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.user.db.UserRepository;
import cn.aijiamuyingfang.server.user.domain.RecieveAddress;
import cn.aijiamuyingfang.server.user.domain.User;
import cn.aijiamuyingfang.server.user.domain.response.GetUserPhoneResponse;

/**
 * [描述]:
 * <p>
 * User的Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 22:06:07
 */
@Service
public class UserService {
  @Autowired
  private BCryptPasswordEncoder encoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RecieveAddressRepository recieveaddressRepository;

  /**
   * 注册用户(该方法只能ADMIN调用,用来注册SENDER)
   * 
   * @param request
   */
  public User registerUser(User request) {
    String username = request.getUsername();
    if (StringUtils.isEmpty(username)) {
      throw new IllegalArgumentException("register failed,because not provide  username");
    }
    String password = request.getPassword();
    if (StringUtils.isEmpty(password)) {
      throw new IllegalArgumentException("register failed,because not provide  password");
    }

    User user = userRepository.findOne(username);
    if (user != null) {
      return user;
    }
    request.setPassword(encoder.encode(password));
    request.addAuthority(UserAuthority.SENDER_PERMISSION);
    userRepository.saveAndFlush(request);
    return request;
  }

  /**
   * 创建用户
   * 
   * @param user
   */
  public void saveUser(User user) {
    if (user != null) {
      userRepository.saveAndFlush(user);
    }
  }

  /**
   * 获取用户
   * 
   * @param username
   *          用户id
   * @return
   */
  public User getUser(String username) {
    User user = userRepository.findOne(username);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    return user;
  }

  /**
   * 获取用户手机号
   * 
   * @param username
   *          用户id
   * @return
   */
  public GetUserPhoneResponse getUserPhone(String username) {
    User user = getUser(username);
    GetUserPhoneResponse response = new GetUserPhoneResponse();
    response.setPhone(user.getPhone());
    return response;
  }

  /**
   * 更新用户信息
   * 
   * @param username
   *          用户id
   * @param updateUser
   * @return
   */
  public User updateUser(String username, User updateUser) {
    User user = userRepository.findOne(username);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    if (null == updateUser) {
      return user;
    }
    user.update(updateUser);
    userRepository.saveAndFlush(user);
    return user;
  }

  /**
   * 获取用户收件地址
   * 
   * @param username
   *          用户id
   * @return
   */
  public List<RecieveAddress> getUserRecieveAddressList(String username) {
    User user = userRepository.findOne(username);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    return recieveaddressRepository.findByUsername(username);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param username
   *          用户id
   * @param recieveAddress
   * @return
   */
  public RecieveAddress addUserRecieveAddress(String username, RecieveAddress recieveAddress) {
    User user = userRepository.findOne(username);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    if (null == recieveAddress) {
      return null;
    }
    recieveAddress.setUsername(username);
    if (recieveAddress.isDef()) {
      recieveaddressRepository.setAllRecieveAddressNotDef();
    }
    recieveaddressRepository.saveAndFlush(recieveAddress);
    return recieveAddress;
  }

  /**
   * 获取收件地址
   * 
   * @param username
   *          用户id
   * @param addressId
   * @return
   */
  public RecieveAddress getRecieveAddress(String username, String addressId) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }
    if (username.equals(recieveAddress.getUsername())) {
      return recieveAddress;
    } else {
      throw new AccessDeniedException("no permission get other user's recieve address");
    }

  }

  /**
   * 更新收件地址信息
   * 
   * 
   * @param username
   *          用户id
   * @param addressId
   * @param updateRecieveAddress
   * @return
   */
  public RecieveAddress updateRecieveAddress(String username, String addressId, RecieveAddress updateRecieveAddress) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }
    if (!username.equals(recieveAddress.getUsername())) {
      throw new AccessDeniedException("no permission change other user's recieve address");
    }

    recieveAddress.update(updateRecieveAddress);
    if (recieveAddress.isDef()) {
      recieveaddressRepository.setAllRecieveAddressNotDef();
    }
    recieveaddressRepository.saveAndFlush(recieveAddress);
    return recieveAddress;
  }

  /**
   * 废弃收件地址
   * 
   * @param username
   *          用户id
   * @param addressId
   */
  public void deprecateRecieveAddress(String username, String addressId) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }

    if (!username.equals(recieveAddress.getUsername())) {
      throw new AccessDeniedException("no permission deprecate other user's recieve address");
    }

    recieveAddress.setDeprecated(true);
    recieveaddressRepository.saveAndFlush(recieveAddress);
  }

}
