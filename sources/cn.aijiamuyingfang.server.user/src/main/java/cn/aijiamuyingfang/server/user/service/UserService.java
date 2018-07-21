package cn.aijiamuyingfang.server.user.service;

import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import cn.aijiamuyingfang.server.domain.address.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.domain.exception.AuthException;
import cn.aijiamuyingfang.server.domain.exception.UserException;
import cn.aijiamuyingfang.server.domain.user.User;
import cn.aijiamuyingfang.server.domain.user.db.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * 更新用户信息
   * 
   * @param userid
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
    recieveaddressRepository.saveAndFlush(recieveAddress);
    return recieveAddress;
  }

  /**
   * 获取收件地址
   * 
   * @param userid
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
      throw new AuthException("403", "no permission get other user's recieve address");
    }

  }

  /**
   * 更新收件地址信息
   * 
   * 
   * @param userid
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
      throw new AuthException("403", "no permission change other user's recieve address");
    }

    recieveAddress.update(updateRecieveAddress);
    recieveaddressRepository.saveAndFlush(recieveAddress);
    return recieveAddress;
  }

  /**
   * 废弃收件地址
   * 
   * @param userid
   * @param addressid
   */
  public void deprecateRecieveAddress(String userid, String addressid) {
    RecieveAddress recieveAddress = recieveaddressRepository.findOne(addressid);
    if (null == recieveAddress) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressid);
    }

    if (!userid.equals(recieveAddress.getUserid())) {
      throw new AuthException("403", "no permission deprecate other user's recieve address");
    }

    recieveAddress.setDeprecated(true);
    recieveaddressRepository.saveAndFlush(recieveAddress);
  }

}
