package cn.aijiamuyingfang.server.domain.address;

import javax.persistence.Entity;

/**
 * [描述]:
 * <p>
 * 收货地址
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 20:38:53
 */
@Entity
public class RecieveAddress extends Address {

	/**
	 * 收货地址-关联用户
	 */
	private long userId;

	/**
	 * 收货地址-联系电话
	 */
	private String phone;

	/**
	 * 收货地址-收件人
	 */
	private String reciever;

	/**
	 * 收货地址-默认收货地址
	 */
	private boolean def;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public boolean isDef() {
		return def;
	}

	public void setDef(boolean def) {
		this.def = def;
	}

}
