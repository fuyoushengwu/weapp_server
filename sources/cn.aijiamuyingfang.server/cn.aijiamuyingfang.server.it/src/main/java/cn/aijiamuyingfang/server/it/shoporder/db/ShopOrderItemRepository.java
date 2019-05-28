package cn.aijiamuyingfang.server.it.shoporder.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.shoporder.ShopOrderItem;

/**
 * [描述]:
 * <p>
 * 订单商品项的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface ShopOrderItemRepository extends JpaRepository<ShopOrderItem, String> {

}
