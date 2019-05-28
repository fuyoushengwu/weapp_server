package cn.aijiamuyingfang.server.shoporder.db;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.shoporder.domain.ShopCart;

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
   * @param userid
   *          用户id
   * @param goodid
   *          商品id
   */
  ShopCart findByUseridAndGoodId(String userid, String goodid);

  /**
   * 查找用户在购物车中的某几项商品
   * 
   * @param userid
   *          用户id
   * @param goodIdList
   *          商品id列表
   * @return
   */
  List<ShopCart> findByUseridAndGoodIdIn(String userid, List<String> goodIdList);

  /**
   * 分页查找用户购物车中的所有商品
   * 
   * @param userid
   *          用户id
   * @param pageable
   *          分页信息
   * @return
   */
  Page<ShopCart> findByUserid(String userid, Pageable pageable);

  /**
   * 查找用户所有选中的购车项
   * 
   * @param userid
   *          用户id
   * @param ischecked
   *          是否选中
   * @return
   */
  List<ShopCart> findByUseridAndIschecked(String userid, boolean ischecked);

  /**
   * 全选/全不选用户下的购物车
   * 
   * @param userid
   *          用户id
   * @param ischecked
   */
  @Modifying
  @Transactional
  @Query(value = "update shop_cart set ischecked=:ischecked where userid=:userid", nativeQuery = true)
  void checkAllShopCart(@Param("userid") String userid, @Param("ischecked") boolean ischecked);

  /**
   * 删除购物车中的商品
   * 
   * @param goodid
   *          商品id
   */
  @Modifying
  @Transactional
  @Query(value = "delete from shop_cart where good_id=:goodid", nativeQuery = true)
  void deleteGood(@Param("goodid") String goodid);

}
