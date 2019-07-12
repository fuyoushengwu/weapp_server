package cn.aijiamuyingfang.server.it.dto.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 用户
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 16:38:56
 */
@Entity(name = "user")
@Data
public class UserDTO {

  /**
   * 小程序用戶沒有username,使用openid作爲username,
   */
  @Id
  private String username;

  /**
   * 密码
   */
  private String password;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;

  /**
   * 联系电话
   */
  private String phone;

  /**
   * 用户角色
   */
  @ElementCollection(fetch = FetchType.EAGER, targetClass = UserAuthorityDTO.class)
  @Enumerated(EnumType.ORDINAL)
  private List<UserAuthorityDTO> authorityList = new ArrayList<>();

  /**
   * 用户所在的APP ID
   */
  private String appid;

  /**
   * 性别
   */
  private GenderDTO gender;

  /**
   * 最后一次消息读取时间
   */
  private Date lastReadMsgTime = new Date(0);

  /**
   * 用户通用积分
   */
  private int genericScore = 0;
}
