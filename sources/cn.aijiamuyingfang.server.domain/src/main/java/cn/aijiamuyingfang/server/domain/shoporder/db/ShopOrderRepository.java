package cn.aijiamuyingfang.server.domain.shoporder.db;

import cn.aijiamuyingfang.server.domain.shoporder.PreOrderGood;
import cn.aijiamuyingfang.server.domain.shoporder.SendType;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.server.domain.shoporder.ShopOrderStatus;
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
  @Query(
      value = "select count(*) from shop_order where userid=:userid and status in :statusList and sendtype in "
          + ":sendtypeList",
      nativeQuery = true)
  int countByUseridAndStatusInAndSendtypeIn(@Param("userid") String userid,
      @Param("statusList") List<ShopOrderStatus> statusList, @Param("sendtypeList") List<SendType> sendtypeList);

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
      value = "select sum(si.count) as count, good.* from shop_order_item si inner join good on si.good_id=good.id "
          + "inner join shop_order s on si.id=s.id where s.status=0 group by si.good_id order by ?#{#pageable}",
      countQuery = "select count(*) from shop_order_item si inner join good on si.good_id=good.id inner join "
          + "shop_order s on si.id=s.id where s.status=0 group by si.good_id order by ?#{#pageable}",
      nativeQuery = true)
  Page<PreOrderGood> findPreOrder(Pageable pageable);

  /**
   * 查找包含某件商品的预约单
   * 
   * @param goodid
   * @return
   */
  @Query(
      value = "select s.* from shop_order_item si inner join shop_order s on si.id=s.id where si.good_id=:good_id "
          + "and s.status=0",
      nativeQuery = true)
  List<ShopOrder> findPreOrderContainsGoodid(@Param("good_id") String goodid);
}
