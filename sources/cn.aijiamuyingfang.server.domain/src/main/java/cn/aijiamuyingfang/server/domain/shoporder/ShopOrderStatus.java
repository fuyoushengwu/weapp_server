package cn.aijiamuyingfang.server.domain.shoporder;

import cn.aijiamuyingfang.server.domain.BaseEnum;

/**
 * [描述]:
 * <p>
 * 订单状态
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 16:03:34
 */
public enum ShopOrderStatus implements BaseEnum {
	/**
	 * 预订
	 */
	PREORDER(0),
	/**
	 * 未开始
	 */
	UNSTART(1),

	/**
	 * 进行中
	 */
	DOING(2),

	/**
	 * 已完成
	 */
	FINISHED(3),

	/**
	 * 超时(例如:订单超时未取货就会变为超时的状态)
	 */
	OVERTIME(4),

	/**
	 * 未知类型
	 */
	UNKNOW(-1);

	private int value;

	private ShopOrderStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}