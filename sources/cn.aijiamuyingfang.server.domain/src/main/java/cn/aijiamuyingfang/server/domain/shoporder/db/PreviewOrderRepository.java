package cn.aijiamuyingfang.server.domain.shoporder.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.shoporder.PreviewOrder;

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
public interface PreviewOrderRepository extends JpaRepository<PreviewOrder, Long> {
	/**
	 * 查找用户的预览订单
	 * 
	 * @param userid
	 * @return
	 */
	List<PreviewOrder> findByUserid(long userid);
}
