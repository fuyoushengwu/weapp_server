package cn.aijiamuyingfang.server.domain.shopcart.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;

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
public interface ShopCartItemRepository extends JpaRepository<ShopCartItem, Long> {

	/**
	 * 查找用户在购物车中某一项商品
	 * 
	 * @param userid
	 * @param goodId
	 * @return
	 */
	ShopCartItem findByUseridAndGoodId(long userid, long goodId);

	/**
	 * 查找用户在购物车中的某几项商品
	 * 
	 * @param userid
	 * @param goodIdList
	 * @return
	 */
	List<ShopCartItem> findByUseridAndGoodIdIn(long userid, List<Long> goodIdList);

	/**
	 * 分页查找用户购物车中的所有商品
	 * 
	 * @param userid
	 * @param pageable
	 * @return
	 */
	Page<ShopCartItem> findByUserid(String userid, Pageable pageable);

}
