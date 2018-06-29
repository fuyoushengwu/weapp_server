package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.domain.BaseEnum;

/**
 * [描述]:
 * <p>
 * 订单配送类型
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 15:51:30
 */
public enum SendType implements BaseEnum {
	/**
	 * 自取
	 */
	PICKUP(0),

	/**
	 * 送货
	 */
	OWNSEND(1),

	/**
	 * 快递
	 */
	THIRDSEND(2),

	/**
	 * 未知类型
	 */
	UNKNOW(-1);

	private int value;

	private SendType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}