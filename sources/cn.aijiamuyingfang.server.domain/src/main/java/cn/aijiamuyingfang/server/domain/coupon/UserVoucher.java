package cn.aijiamuyingfang.server.domain.coupon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * [描述]:
 * <p>
 * 用户获取的兑换券
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 03:11:01
 */
@Entity
public class UserVoucher {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 用户ID
	 */
	private long userid;

	/**
	 * 兑换券
	 */
	@ManyToOne
	private GoodVoucher goodVoucher;

	/**
	 * 用户拥有的该兑换券分值
	 */
	private int score;

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

	public GoodVoucher getGoodVoucher() {
		return goodVoucher;
	}

	public void setGoodVoucher(GoodVoucher goodVoucher) {
		this.goodVoucher = goodVoucher;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
