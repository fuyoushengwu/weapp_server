package cn.aijiamuyingfang.server.domain.shoporder.db;

import cn.aijiamuyingfang.commons.domain.SendType;
import cn.aijiamuyingfang.commons.domain.ShopOrderStatus;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * [描述]:
 * <p>
 * 订单的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, String> {

  /**
   * 计算不同状态和送货方式下订单总数
   * 
   * @param userid
   * @param statusList
   * @param sendtypeList
   * @return
   */
  int countByUseridAndStatusInAndSendtypeIn(String userid, List<ShopOrderStatus> statusList,
      List<SendType> sendtypeList);

  /**
   * 根据订单状态、配送方式分页查找用户的订单
   * 
   * @param userid
   * @param statusList
   * @param sendtypeList
   * @param pageable
   * @return
   */
  Page<ShopOrder> findByUseridAndStatusInAndSendtypeIn(String userid, List<ShopOrderStatus> statusList,
      List<SendType> sendtypeList, Pageable pageable);

  /**
   * 根据订单状态、配送方式分页查找所有订单
   * 
   * @param statusList
   * @param sendtypeList
   * @param pageable
   * @return
   */
  Page<ShopOrder> findByStatusInAndSendtypeIn(List<ShopOrderStatus> statusList, List<SendType> sendtypeList,
      Pageable pageable);

  /**
   * 根据状态和分页条件,查找订单
   * 
   * @param status
   * @param pageable
   * @return
   */
  Page<ShopOrder> findByStatus(ShopOrderStatus status, Pageable pageable);

  /**
   * 根据状态查找订单(非分页)
   * 
   * @param status
   * @return
   */
  List<ShopOrder> findByStatus(ShopOrderStatus status);

  /**
   * 分页查找预定的商品
   * 
   * @param pageable
   * @return
   */
  @Query(
      value = "select sum(shop_order_item.count),good.id from shop_order_item inner join good on shop_order_item.good_id=good.id where "
          + "shop_order_item.count>good.count and shop_order_item.id in(select order_item_list_id from shop_order right join shop_order_order_item_list "
          + "on shop_order.id=shop_order_order_item_list.shop_order_id where shop_order.status=0) group by good.id order by ?#{#pageable}",
      countQuery = "select count(*) from shop_order_item inner join good on shop_order_item.good_id=good.id where shop_order_item.count>good.count "
          + "and shop_order_item.id in(select order_item_list_id from shop_order right join shop_order_order_item_list on "
          + "shop_order.id=shop_order_order_item_list.shop_order_id where shop_order.status=0) group by good.id order by ?#{#pageable}",
      nativeQuery = true)
  Page<Object[]> findPreOrder(Pageable pageable);

  /**
   * 查找包含某件商品的预约单
   * 
   * @param goodid
   * @return
   */
  @Query(
      value = "select * from shop_order where status=0 and id in (select shop_order_id from shop_order_order_item_list where "
          + "order_item_list_id in (select id from shop_order_item  where good_id=:good_id))",
      nativeQuery = true)
  List<ShopOrder> findPreOrderContainsGoodid(@Param("good_id") String goodid);
}
