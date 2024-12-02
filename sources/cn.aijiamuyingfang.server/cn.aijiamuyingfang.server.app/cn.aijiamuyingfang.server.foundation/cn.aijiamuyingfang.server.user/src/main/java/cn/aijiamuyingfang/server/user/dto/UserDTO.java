package cn.aijiamuyingfang.server.user.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import cn.aijiamuyingfang.server.user.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.user.User;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

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

  /**
   * 使用提供的updateUser更新用户信息
   * 
   * @param updateUser
   *          要更新的用户信息
   */
  public void update(User updateUser) {
    if (null == updateUser) {
      return;
    }
    if (StringUtils.hasContent(updateUser.getNickname())) {
      this.setNickname(updateUser.getNickname());
    }
    if (updateUser.getGender() != null) {
      this.setGender(ConvertUtils.convertGender(updateUser.getGender()));
    }
    if (StringUtils.hasContent(updateUser.getAvatar())) {
      this.setAvatar(updateUser.getAvatar());
    }
    if (StringUtils.hasContent(updateUser.getPhone())) {
      this.setPhone(updateUser.getPhone());
    }
    if (updateUser.getLastReadMsgTime() != null) {
      this.setLastReadMsgTime(updateUser.getLastReadMsgTime());
    }
    if (updateUser.getGenericScore() != 0) {
      this.setGenericScore(updateUser.getGenericScore());
    }

    if (CollectionUtils.isNotEmpty(updateUser.getAuthorityList())) {
      this.setAuthorityList(ConvertUtils.convertUserAuthorityList(updateUser.getAuthorityList()));
    }
  }
}
