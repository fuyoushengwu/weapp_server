package cn.aijiamuyingfang.server.domain.shoporder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cn.aijiamuyingfang.server.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.server.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.server.domain.goods.Good;

/**
 * [描述]:
 * <p>
 * 订单使用的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:41:55
 */
@Entity
public class ShopOrderVoucher {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 订单使用的用户兑换券
	 */
	@ManyToOne
	private UserVoucher userVoucher;

	/**
	 * 订单使用的兑换方式
	 */
	@ManyToOne
	private VoucherItem voucherItem;

	/**
	 * 该兑换项关联的商品
	 */
	@ManyToOne
	private Good good;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserVoucher getUserVoucher() {
		return userVoucher;
	}

	public void setUserVoucher(UserVoucher userVoucher) {
		this.userVoucher = userVoucher;
	}

	public VoucherItem getVoucherItem() {
		return voucherItem;
	}

	public void setVoucherItem(VoucherItem voucherItem) {
		this.voucherItem = voucherItem;
	}

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

}
