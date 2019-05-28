package cn.aijiamuyingfang.server.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private PasswordEncoder encoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RecieveAddressRepository recieveaddressRepository;

  /**
   * 根据openid获取用户(因为逻辑上User和openid是一一对应的)
   * 
   * @param openid
   * @return
   */
  public User getUserByOpenid(String openid) {
    if (StringUtils.isEmpty(openid)) {
      return null;
    }
    return userRepository.findByOpenid(openid);
  }

  /**
   * 注册用户(该方法只能ADMIN调用,用来注册SENDER)
   * 
   * @param request
   */
  public User registerUser(User request) {
    if (StringUtils.isEmpty(request.getJscode())) {
      throw new IllegalArgumentException("register failed,because not provide jscode as openid");
    }
    String jscode = request.getJscode();
    User user = userRepository.findByOpenid(jscode);
    if (user != null) {
      return user;
    }
    request.setOpenid(jscode);
    request.setPassword(encoder.encode(jscode));
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
   * @param userid
   *          用户id
   * @return
   */
  public User getUser(String userid) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, userid);
    }
    return user;
  }

  /**
   * 获取用户手机号
   * 
   * @param userid
   *          用户id
   * @return
   */
  public GetUserPhoneResponse getUserPhone(String userid) {
    User user = getUser(userid);
    GetUserPhoneResponse response = new GetUserPhoneResponse();
    response.setPhone(user.getPhone());
    return response;
  }

  /**
   * 更新用户信息
   * 
   * @param userid
   *          用户id
   * @param updateUser
   * @return
   */
  public User updateUser(String userid, User updateUser) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, userid);
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
   * @param userid
   *          用户id
   * @return
   */
  public List<RecieveAddress> getUserRecieveAddressList(String userid) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, userid);
    }
    return recieveaddressRepository.findByUserid(userid);
  }

  /**
   * 给用户添加收件地址
   * 
   * @param userid
   *          用户id
   * @param recieveAddress
   * @return
   */
  public RecieveAddress addUserRecieveAddress(String userid, RecieveAddress recieveAddress) {
    User user = userRepository.findOne(userid);
    if (null == user) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, userid);
    }
    if (null == recieveAddress) {
      return null;
    }
    recieveAddress.setUserid(userid);
    if (recieveAddress.isDef()) {
      recieveaddressRepository.setAllRecieveAddressNotDef();
    }
    recieveaddressRepository.saveAndFlush(recieveAddress);
    return recieveAddress;
  }

  /**
   * 获取收件地址
   * 
   * @param userid
   *          用户id
   * @param addressid
   * @return
   */
  public RecieveAddress getRecieveAddress(String userid, String addressid) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressid);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressid);
    }
    if (userid.equals(recieveAddress.getUserid())) {
      return recieveAddress;
    } else {
      throw new AccessDeniedException("no permission get other user's recieve address");
    }

  }

  /**
   * 更新收件地址信息
   * 
   * 
   * @param userid
   *          用户id
   * @param addressid
   * @param updateRecieveAddress
   * @return
   */
  public RecieveAddress updateRecieveAddress(String userid, String addressid, RecieveAddress updateRecieveAddress) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressid);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressid);
    }
    if (!userid.equals(recieveAddress.getUserid())) {
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
   * @param userid
   *          用户id
   * @param addressid
   */
  public void deprecateRecieveAddress(String userid, String addressid) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressid);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressid);
    }

    if (!userid.equals(recieveAddress.getUserid())) {
      throw new AccessDeniedException("no permission deprecate other user's recieve address");
    }

    recieveAddress.setDeprecated(true);
    recieveaddressRepository.saveAndFlush(recieveAddress);
  }

}
