package cn.aijiamuyingfang.server.domain.shoporder.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.shoporder.SendType;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrderStatus;

/**
 * [描述]:
 * <p>
 * 门店地址的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
	/**
	 * 根据订单状态、配送方式分页查找用户的订单,并将结果根据订单号降序排列
	 * 
	 * @param userid
	 * @param statusList
	 * @param sendtypeList
	 * @param pageable
	 * @return
	 */
	Page<ShopOrder> findByUseridAndStatusInAndSendtypeInOrderByOrderNoDesc(long userid,
			List<ShopOrderStatus> statusList, List<SendType> sendtypeList, Pageable pageable);

	/**
	 * 根据订单状态、配送方式分页查找用户的订单,并将结果根据完成时间降序排列
	 * 
	 * @param userid
	 * @param statusList
	 * @param sendtypeList
	 * @param pageable
	 * @return
	 */
	Page<ShopOrder> findByUseridAndStatusInAndSendtypeInOrderByFinishTimeDesc(long userid,
			List<ShopOrderStatus> statusList, List<SendType> sendtypeList, Pageable pageable);

	/**
	 * 根据订单状态、配送方式分页查找所有订单,并将结果根据订单号降序排列
	 * 
	 * @param statusList
	 * @param sendtypeList
	 * @param pageable
	 * @return
	 */
	Page<ShopOrder> findByStatusInAndSendtypeInOrderByOrderNoDesc(List<ShopOrderStatus> statusList,
			List<SendType> sendtypeList, Pageable pageable);

	/**
	 * 根据订单状态、配送方式分页查找所有订单,并将结果根据完成时间降序排列
	 * 
	 * @param statusList
	 * @param sendtypeList
	 * @param pageable
	 * @return
	 */
	Page<ShopOrder> findByStatusInAndSendtypeInOrderByFinishTimeDesc(List<ShopOrderStatus> statusList,
			List<SendType> sendtypeList, Pageable pageable);

	/**
	 * 根据转台查找订单
	 * 
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<ShopOrder> findByStatus(ShopOrderStatus status, Pageable pageable);
}
