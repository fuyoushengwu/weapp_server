package cn.aijiamuyingfang.server.user.utils;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.server.user.dto.GenderDTO;
import cn.aijiamuyingfang.server.user.dto.RecieveAddressDTO;
import cn.aijiamuyingfang.server.user.dto.UserAuthorityDTO;
import cn.aijiamuyingfang.server.user.dto.UserDTO;
import cn.aijiamuyingfang.server.user.dto.UserMessageDTO;
import cn.aijiamuyingfang.server.user.dto.UserMessageTypeDTO;
import cn.aijiamuyingfang.vo.message.UserMessage;
import cn.aijiamuyingfang.vo.message.UserMessageType;
import cn.aijiamuyingfang.vo.user.Gender;
import cn.aijiamuyingfang.vo.user.RecieveAddress;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.user.UserAuthority;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {
  public static User convertUserDTO(UserDTO userDTO) {
    if (null == userDTO) {
      return null;
    }
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setPassword(userDTO.getPassword());
    user.setNickname(userDTO.getNickname());
    user.setAvatar(userDTO.getAvatar());
    user.setPhone(userDTO.getPhone());
    user.setAuthorityList(convertUserAuthorityDTOList(userDTO.getAuthorityList()));
    user.setAppid(userDTO.getAppid());
    user.setGender(convertGenderDTO(userDTO.getGender()));
    user.setLastReadMsgTime(userDTO.getLastReadMsgTime());
    user.setGenericScore(userDTO.getGenericScore());
    return user;
  }

  public static List<User> convertUserDTOList(List<UserDTO> userDTOList) {
    List<User> userList = new ArrayList<>();
    if (null == userDTOList) {
      return userList;
    }
    for (UserDTO userDTO : userDTOList) {
      User user = convertUserDTO(userDTO);
      if (user != null) {
        userList.add(user);
      }
    }
    return userList;
  }

  public static UserDTO convertUser(User user) {
    if (null == user) {
      return null;
    }
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(user.getUsername());
    userDTO.setPassword(user.getPassword());
    userDTO.setNickname(user.getNickname());
    userDTO.setAvatar(user.getAvatar());
    userDTO.setPhone(user.getPhone());
    userDTO.setAuthorityList(convertUserAuthorityList(user.getAuthorityList()));
    userDTO.setAppid(user.getAppid());
    userDTO.setGender(convertGender(user.getGender()));
    userDTO.setLastReadMsgTime(user.getLastReadMsgTime());
    userDTO.setGenericScore(user.getGenericScore());
    return userDTO;
  }

  public static UserMessage convertUserMessageDTO(UserMessageDTO userMessageDTO) {
    if (null == userMessageDTO) {
      return null;
    }
    UserMessage userMessage = new UserMessage();
    userMessage.setId(userMessageDTO.getId());
    userMessage.setUsername(userMessageDTO.getUsername());
    userMessage.setType(convertUserMessageTypeDTO(userMessageDTO.getType()));
    userMessage.setTitle(userMessageDTO.getTitle());
    userMessage.setRoundup(userMessageDTO.getRoundup());
    userMessage.setContent(userMessageDTO.getContent());
    userMessage.setCreateTime(userMessageDTO.getCreateTime());
    userMessage.setFinishTime(userMessageDTO.getFinishTime());
    userMessage.setReaded(userMessageDTO.isReaded());
    return userMessage;
  }

  public static List<UserMessage> convertUserMessageDTOList(List<UserMessageDTO> userMessageDTOList) {
    List<UserMessage> userMessageList = new ArrayList<>();
    if (null == userMessageDTOList) {
      return userMessageList;
    }
    for (UserMessageDTO userMessageDTO : userMessageDTOList) {
      UserMessage userMessage = convertUserMessageDTO(userMessageDTO);
      if (userMessage != null) {
        userMessageList.add(userMessage);
      }
    }
    return userMessageList;
  }

  public static UserMessageDTO convertUserMessage(UserMessage userMessage) {
    if (null == userMessage) {
      return null;
    }
    UserMessageDTO userMessageDTO = new UserMessageDTO();
    userMessageDTO.setId(userMessage.getId());
    userMessageDTO.setUsername(userMessage.getUsername());
    userMessageDTO.setType(convertUserMessageType(userMessage.getType()));
    userMessageDTO.setTitle(userMessage.getTitle());
    userMessageDTO.setRoundup(userMessage.getRoundup());
    userMessageDTO.setContent(userMessage.getContent());
    userMessageDTO.setCreateTime(userMessage.getCreateTime());
    userMessageDTO.setFinishTime(userMessage.getFinishTime());
    userMessageDTO.setReaded(userMessage.isReaded());
    return userMessageDTO;
  }

  public static UserMessageType convertUserMessageTypeDTO(UserMessageTypeDTO userMessageTypeDTO) {
    if (null == userMessageTypeDTO) {
      return UserMessageType.UNKNOW;
    }
    for (UserMessageType userMessage : UserMessageType.values()) {
      if (userMessage.getValue() == userMessageTypeDTO.getValue()) {
        return userMessage;
      }
    }
    return UserMessageType.UNKNOW;
  }

  public static UserMessageTypeDTO convertUserMessageType(UserMessageType userMessage) {
    if (null == userMessage) {
      return UserMessageTypeDTO.UNKNOW;
    }
    for (UserMessageTypeDTO userMessageTypeDTO : UserMessageTypeDTO.values()) {
      if (userMessage.getValue() == userMessageTypeDTO.getValue()) {
        return userMessageTypeDTO;
      }
    }
    return UserMessageTypeDTO.UNKNOW;
  }

  public static Gender convertGenderDTO(GenderDTO genderDTO) {
    if (null == genderDTO) {
      return Gender.UNKNOW;
    }
    for (Gender gender : Gender.values()) {
      if (gender.getValue() == genderDTO.getValue()) {
        return gender;
      }
    }
    return Gender.UNKNOW;
  }

  public static GenderDTO convertGender(Gender gender) {
    if (null == gender) {
      return GenderDTO.UNKNOW;
    }
    for (GenderDTO genderDTO : GenderDTO.values()) {
      if (gender.getValue() == genderDTO.getValue()) {
        return genderDTO;
      }
    }
    return GenderDTO.UNKNOW;
  }

  public static UserAuthority convertUserAuthorityDTO(UserAuthorityDTO userAuthorityDTO) {
    if (null == userAuthorityDTO) {
      return UserAuthority.UNKNOW;
    }
    for (UserAuthority userAuthority : UserAuthority.values()) {
      if (userAuthority.getValue() == userAuthorityDTO.getValue()) {
        return userAuthority;
      }
    }
    return UserAuthority.UNKNOW;
  }

  public static List<UserAuthority> convertUserAuthorityDTOList(List<UserAuthorityDTO> userAuthorityDTOList) {
    List<UserAuthority> userAuthorityList = new ArrayList<>();
    if (null == userAuthorityDTOList) {
      return userAuthorityList;
    }
    for (UserAuthorityDTO userAuthorityDTO : userAuthorityDTOList) {
      UserAuthority userAuthority = convertUserAuthorityDTO(userAuthorityDTO);
      if (userAuthority != null) {
        userAuthorityList.add(userAuthority);
      }
    }
    return userAuthorityList;
  }

  public static UserAuthorityDTO convertUserAuthority(UserAuthority userAuthority) {
    if (null == userAuthority) {
      return UserAuthorityDTO.UNKNOW;
    }
    for (UserAuthorityDTO userAuthorityDTO : UserAuthorityDTO.values()) {
      if (userAuthority.getValue() == userAuthorityDTO.getValue()) {
        return userAuthorityDTO;
      }
    }
    return UserAuthorityDTO.UNKNOW;
  }

  public static List<UserAuthorityDTO> convertUserAuthorityList(List<UserAuthority> userAuthorityList) {
    List<UserAuthorityDTO> userAuthorityDTOList = new ArrayList<>();
    if (null == userAuthorityList) {
      return userAuthorityDTOList;
    }
    for (UserAuthority userAuthority : userAuthorityList) {
      UserAuthorityDTO userAuthorityDTO = convertUserAuthority(userAuthority);
      if (userAuthorityDTO != null) {
        userAuthorityDTOList.add(userAuthorityDTO);
      }
    }
    return userAuthorityDTOList;
  }

  public static RecieveAddress convertRecieveAddressDTO(RecieveAddressDTO recieveAddressDTO) {
    if (null == recieveAddressDTO) {
      return null;
    }
    RecieveAddress recieveAddress = new RecieveAddress();
    recieveAddress.setId(recieveAddressDTO.getId());
    recieveAddress.setDeprecated(recieveAddressDTO.isDeprecated());
    recieveAddress
        .setProvince(cn.aijiamuyingfang.server.utils.ConvertUtils.convertProvinceDTO(recieveAddressDTO.getProvince()));
    recieveAddress.setCity(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCityDTO(recieveAddressDTO.getCity()));
    recieveAddress
        .setCounty(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCountyDTO(recieveAddressDTO.getCounty()));
    recieveAddress.setTown(cn.aijiamuyingfang.server.utils.ConvertUtils.convertTownDTO(recieveAddressDTO.getTown()));
    recieveAddress.setDetail(recieveAddressDTO.getDetail());
    recieveAddress.setCoordinate(
        cn.aijiamuyingfang.server.utils.ConvertUtils.convertCoordinateDTO(recieveAddressDTO.getCoordinate()));
    recieveAddress.setUsername(recieveAddressDTO.getUsername());
    recieveAddress.setPhone(recieveAddressDTO.getPhone());
    recieveAddress.setReciever(recieveAddressDTO.getReciever());
    recieveAddress.setDef(recieveAddressDTO.isDef());
    return recieveAddress;
  }

  public static List<RecieveAddress> convertRecieveAddressDTOList(List<RecieveAddressDTO> recieveAddressDTOList) {
    List<RecieveAddress> recieveAddressList = new ArrayList<>();
    if (null == recieveAddressDTOList) {
      return recieveAddressList;
    }
    for (RecieveAddressDTO recieveAddressDTO : recieveAddressDTOList) {
      RecieveAddress recieveAddress = convertRecieveAddressDTO(recieveAddressDTO);
      if (recieveAddress != null) {
        recieveAddressList.add(recieveAddress);
      }
    }
    return recieveAddressList;
  }

  public static RecieveAddressDTO convertRecieveAddress(RecieveAddress recieveAddress) {
    if (null == recieveAddress) {
      return null;
    }
    RecieveAddressDTO recieveAddressDTO = new RecieveAddressDTO();
    recieveAddressDTO.setId(recieveAddress.getId());
    recieveAddressDTO.setDeprecated(recieveAddress.isDeprecated());
    recieveAddressDTO
        .setProvince(cn.aijiamuyingfang.server.utils.ConvertUtils.convertProvince(recieveAddress.getProvince()));
    recieveAddressDTO.setCity(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCity(recieveAddress.getCity()));
    recieveAddressDTO.setCounty(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCounty(recieveAddress.getCounty()));
    recieveAddressDTO.setTown(cn.aijiamuyingfang.server.utils.ConvertUtils.convertTown(recieveAddress.getTown()));
    recieveAddressDTO.setDetail(recieveAddress.getDetail());
    recieveAddressDTO
        .setCoordinate(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCoordinate(recieveAddress.getCoordinate()));
    recieveAddressDTO.setUsername(recieveAddress.getUsername());
    recieveAddressDTO.setPhone(recieveAddress.getPhone());
    recieveAddressDTO.setReciever(recieveAddress.getReciever());
    recieveAddressDTO.setDef(recieveAddress.isDef());
    return recieveAddressDTO;
  }

}
