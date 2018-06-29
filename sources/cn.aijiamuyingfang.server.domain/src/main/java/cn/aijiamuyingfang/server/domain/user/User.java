package cn.aijiamuyingfang.server.domain.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Entity
public class User {
	/**
	 * 用户的Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String openid;

	/**
	 * 用户所在的APP ID
	 */
	private String appid;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 性别
	 */
	private Gender gender;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 最后一次消息读取时间
	 */
	@JsonIgnore
	private Date lastReadMsgTime;

	/**
	 * 用户积分
	 */
	private int score = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getLastReadMsgTime() {
		return lastReadMsgTime;
	}

	public void setLastReadMsgTime(Date lastReadMsgTime) {
		this.lastReadMsgTime = lastReadMsgTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
