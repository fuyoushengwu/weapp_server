package cn.aijiamuyingfang.server.domain.shoporder;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import cn.aijiamuyingfang.server.domain.address.RecieveAddress;

/**
 * [描述]:
 * <p>
 * 预览的订单,在系统中一个用户只能有一个预览订单
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:54:00
 */
@Entity
public class PreviewOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 订单所属用户
	 */
	private long userid;

	/**
	 * 收货地址
	 */
	@OneToOne
	private RecieveAddress recieveAddress;

	/**
	 * 预览的商品项
	 */
	@ElementCollection
	@OneToMany(cascade = CascadeType.ALL)
	private List<PreviewOrderItem> orderItemList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public RecieveAddress getRecieveAddress() {
		return recieveAddress;
	}

	public void setRecieveAddress(RecieveAddress recieveAddress) {
		this.recieveAddress = recieveAddress;
	}

	public List<PreviewOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<PreviewOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

}
