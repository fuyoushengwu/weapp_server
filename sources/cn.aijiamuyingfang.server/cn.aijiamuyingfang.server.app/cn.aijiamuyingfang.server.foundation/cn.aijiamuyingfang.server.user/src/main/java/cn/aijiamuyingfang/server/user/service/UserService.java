package cn.aijiamuyingfang.server.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.user.db.RecieveAddressRepository;
import cn.aijiamuyingfang.server.user.db.UserRepository;
import cn.aijiamuyingfang.server.user.dto.RecieveAddressDTO;
import cn.aijiamuyingfang.server.user.dto.UserDTO;
import cn.aijiamuyingfang.server.user.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.exception.UserException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.user.UserAuthority;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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

    UserDTO userDTO = userRepository.findOne(username);
    if (userDTO != null) {
      return ConvertUtils.convertUserDTO(userDTO);
    }
    request.setPassword(encoder.encode(password));
    request.addAuthority(UserAuthority.SENDER_PERMISSION);
    return ConvertUtils.convertUserDTO(userRepository.saveAndFlush(ConvertUtils.convertUser(request)));
  }

  /**
   * 获取用户
   * 
   * @param username
   *          用户id
   * @return
   */
  public User getUser(String username) {
    UserDTO userDTO = userRepository.findOne(username);
    if (null == userDTO) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    return ConvertUtils.convertUserDTO(userDTO);
  }

  /**
   * 获取用户手机号
   * 
   * @param username
   *          用户id
   * @return
   */
  public String getUserPhone(String username) {
    User user = getUser(username);
    return user.getPhone();
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
    UserDTO userDTO = userRepository.findOne(username);
    if (null == userDTO) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    if (null == updateUser) {
      return ConvertUtils.convertUserDTO(userDTO);
    }
    userDTO.update(updateUser);
    userRepository.saveAndFlush(userDTO);
    return ConvertUtils.convertUserDTO(userDTO);
  }

  /**
   * 获取用户收件地址
   * 
   * @param username
   *          用户id
   * @return
   */
  public List<RecieveAddress> getUserRecieveAddressList(String username) {
    UserDTO userDTO = userRepository.findOne(username);
    if (null == userDTO) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    return ConvertUtils.convertRecieveAddressDTOList(recieveaddressRepository.findByUsername(username));
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
    UserDTO userDTO = userRepository.findOne(username);
    if (null == userDTO) {
      throw new UserException(ResponseCode.USER_NOT_EXIST, username);
    }
    if (null == recieveAddress) {
      return null;
    }
    recieveAddress.setUsername(username);
    if (recieveAddress.isDef()) {
      recieveaddressRepository.setAllRecieveAddressNotDef();
    }
    RecieveAddressDTO recieveAddressDTO = recieveaddressRepository
        .saveAndFlush(ConvertUtils.convertRecieveAddress(recieveAddress));
    return ConvertUtils.convertRecieveAddressDTO(recieveAddressDTO);
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
    RecieveAddressDTO recieveAddressDTO = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddressDTO) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }
    if (username.equals(recieveAddressDTO.getUsername())) {
      return ConvertUtils.convertRecieveAddressDTO(recieveAddressDTO);
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
    RecieveAddressDTO recieveAddressDTO = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddressDTO) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }
    if (!username.equals(recieveAddressDTO.getUsername())) {
      throw new AccessDeniedException("no permission change other user's recieve address");
    }

    recieveAddressDTO.update(updateRecieveAddress);
    if (recieveAddressDTO.isDef()) {
      recieveaddressRepository.setAllRecieveAddressNotDef();
    }
    recieveaddressRepository.saveAndFlush(recieveAddressDTO);
    return ConvertUtils.convertRecieveAddressDTO(recieveAddressDTO);
  }

  /**
   * 废弃收件地址
   * 
   * @param username
   *          用户id
   * @param addressId
   */
  public void deprecateRecieveAddress(String username, String addressId) {
    RecieveAddressDTO recieveAddressDTO = recieveaddressRepository.findOne(addressId);
    if (null == recieveAddressDTO) {
      throw new UserException(ResponseCode.RECIEVEADDRESS_NOT_EXIST, addressId);
    }

    if (!username.equals(recieveAddressDTO.getUsername())) {
      throw new AccessDeniedException("no permission deprecate other user's recieve address");
    }

    recieveAddressDTO.setDeprecated(true);
    recieveaddressRepository.saveAndFlush(recieveAddressDTO);
  }

}
