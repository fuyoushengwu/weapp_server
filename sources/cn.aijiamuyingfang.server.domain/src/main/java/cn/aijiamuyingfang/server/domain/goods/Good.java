package cn.aijiamuyingfang.server.domain.goods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import cn.aijiamuyingfang.server.domain.coupon.GoodVoucher;

/**
 * [描述]:
 * <p>
 * 商品
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:50
 */
@Entity
public class Good {
	/**
	 * 商品Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 该商品是否废弃(该字段用于删除商品:当需要删除商品时,设置该字段为true)
	 */
	private boolean deprecated;

	/**
	 * 商品名
	 */
	private String name;

	/**
	 * 商品数量
	 */
	private int count;

	/**
	 * 商品销量
	 */
	private int salecount;

	/**
	 * 商品价格
	 */
	private double price;

	/**
	 * 超时零售价
	 */
	private double marketprice;

	/**
	 * 商品封面
	 */
	private String coverImg;

	/**
	 * 商品包装(听,盒,袋)
	 */
	private String pack;

	/**
	 * 商品等级(例如:奶粉:一段,二段,三段,四段,儿童,中老年)
	 */
	private String level;

	/**
	 * 商品条形码
	 */
	private String barcode;

	/**
	 * 购买商品可以获得的积分
	 */
	private int score;

	/**
	 * 购买商品可以获得的兑换券
	 */
	@ManyToOne
	private GoodVoucher voucher;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSalecount() {
		return salecount;
	}

	public void setSalecount(int salecount) {
		this.salecount = salecount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMarketprice() {
		return marketprice;
	}

	public void setMarketprice(double marketprice) {
		this.marketprice = marketprice;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public GoodVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(GoodVoucher voucher) {
		this.voucher = voucher;
	}

}
