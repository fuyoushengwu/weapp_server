package cn.aijiamuyingfang.server.domain.coupon;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * [描述]:
 * <p>
 * 购买商品可以获得的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:02:06
 */
@Entity
public class GoodVoucher {
	/**
	 * 兑换券-Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 兑换券名称
	 */
	private String name;

	/**
	 * 兑换券描述
	 */
	private String description;

	/**
	 * 兑换券中可用的兑换值
	 */
	private String score;

	/**
	 * 兑换券可以兑换的项目
	 */
	@ElementCollection
	@ManyToMany
	private List<VoucherItem> voucherItemList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public List<VoucherItem> getVoucherItemList() {
		return voucherItemList;
	}

	public void setVoucherItemList(List<VoucherItem> voucherItemList) {
		this.voucherItemList = voucherItemList;
	}

}
