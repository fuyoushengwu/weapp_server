package cn.aijiamuyingfang.server.domain.shoporder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;

/**
 * [描述]:
 * <p>
 * 订单预览中的商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:55:10
 */
@Entity
public class PreviewOrderItem {
	/**
	 * 预览项的ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 商品数量
	 */
	private int count;

	/**
	 * 商品
	 */
	@ManyToOne
	private Good good;

	/**
	 * 关联的购物车项
	 */
	@OneToOne
	@JsonIgnore
	private ShopCartItem shopcartItem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Good getGood() {
		return good;
	}

	public void setGood(Good good) {
		this.good = good;
	}

	public ShopCartItem getShopcartItem() {
		return shopcartItem;
	}

	public void setShopcartItem(ShopCartItem shopcartItem) {
		this.shopcartItem = shopcartItem;
	}

}
