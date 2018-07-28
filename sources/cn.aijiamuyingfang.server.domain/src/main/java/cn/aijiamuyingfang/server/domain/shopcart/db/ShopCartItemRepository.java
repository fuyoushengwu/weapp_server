package cn.aijiamuyingfang.server.domain.shopcart.db;

import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
public interface ShopCartItemRepository extends JpaRepository<ShopCartItem, String> {

  /**
   * 查找用户在购物车中某一项商品
   * 
   * @param userid
   * @param goodid
   */
  ShopCartItem findByUseridAndGoodId(String userid, String goodid);

  /**
   * 查找用户在购物车中的某几项商品
   * 
   * @param userid
   * @param goodIdList
   * @return
   */
  List<ShopCartItem> findByUseridAndGoodIdIn(String userid, List<String> goodIdList);

  /**
   * 分页查找用户购物车中的所有商品
   * 
   * @param userid
   * @param pageable
   * @return
   */
  Page<ShopCartItem> findByUserid(String userid, Pageable pageable);

  /**
   * 查找用户所有选中的购车项
   * 
   * @param userid
   * @param ischecked
   * @return
   */
  List<ShopCartItem> findByUseridAndIschecked(String userid, boolean ischecked);

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userid
   * @param ischecked
   */
  @Modifying
  @Transactional
  @Query(value = "update shop_cart_item set ischecked=:ischecked where userid=:userid", nativeQuery = true)
  void checkAllShopCartItem(@Param("userid") String userid, @Param("ischecked") boolean ischecked);

  /**
   * 删除购物车中的商品
   * 
   * @param goodid
   */
  @Modifying
  @Transactional
  @Query(value = "delete from shop_cart_item where good_id=:goodid", nativeQuery = true)
  void deleteGood(@Param("goodid") String goodid);

}
