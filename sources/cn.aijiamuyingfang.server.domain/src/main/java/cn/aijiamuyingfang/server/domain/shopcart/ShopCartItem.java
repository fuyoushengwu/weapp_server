package cn.aijiamuyingfang.server.domain.shopcart;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import cn.aijiamuyingfang.server.domain.goods.Good;

/**
 * [描述]:
 * <p>
 * 购物车中的选购项
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:36:05
 */
@Entity
public class ShopCartItem {
	/**
	 * 购物车-货单ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 购物车关联用户Id
	 */
	private long userid;

	/**
	 * 关联商品
	 */
	@OneToOne
	private Good good;

	/**
	 * 该项是否被选中
	 */
	private boolean ischecked = true;

	/**
	 * 商品数量
	 */
	private int count;

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

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
