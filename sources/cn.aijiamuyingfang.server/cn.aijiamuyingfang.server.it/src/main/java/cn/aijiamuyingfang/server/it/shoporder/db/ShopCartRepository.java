package cn.aijiamuyingfang.server.it.shoporder.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.shoporder.ShopCart;

/**
 * [描述]:
 * <p>
 * 购物车的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, String> {

  /**
   * 查找用户在购物车中某一项商品
   * 
   * @param userId
   *          用户id
   * @param goodId
   *          商品id
   */
  ShopCart findByUserIdAndGoodId(String userId, String goodId);

  /**
   * 查找用户在购物车中的某几项商品
   * 
   * @param userId
   *          用户id
   * @param goodIdList
   *          商品id列表
   * @return
   */
  List<ShopCart> findByUserIdAndGoodIdIn(String userId, List<String> goodIdList);

  /**
   * 分页查找用户购物车中的所有商品
   * 
   * @param userId
   *          用户id
   * @param pageable
   *          分页信息
   * @return
   */
  Page<ShopCart> findByUserId(String userId, Pageable pageable);

  /**
   * 查找用户所有选中的购车项
   * 
   * @param userId
   *          用户id
   * @param isChecked
   *          是否选中
   * @return
   */
  List<ShopCart> findByUserIdAndIschecked(String userId, boolean isChecked);

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userId
   *          用户id
   * @param isChecked
   */
  @Modifying
  @Transactional
  @Query(value = "update shop_cart set is_checked=:is_checked where user_id=:user_id", nativeQuery = true)
  void checkAllShopCart(@Param("user_id") String userId, @Param("is_checked") boolean isChecked);

  /**
   * 删除购物车中的商品
   * 
   * @param goodId
   *          商品id
   */
  @Modifying
  @Transactional
  @Query(value = "delete from shop_cart where good_id=:good_id", nativeQuery = true)
  void deleteGood(@Param("good_id") String goodId);

}
