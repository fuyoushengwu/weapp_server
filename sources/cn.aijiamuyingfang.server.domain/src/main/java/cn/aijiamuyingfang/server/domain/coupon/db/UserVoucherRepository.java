package cn.aijiamuyingfang.server.domain.coupon.db;

import cn.aijiamuyingfang.server.domain.coupon.UserVoucher;
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
 * 用户获得的兑换券的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, String> {
  // TODO:要设置条件:deprecated=false
  /**
   * 分页查询用户的兑换券
   * 
   * @param userid
   * @param pagable
   * @return
   */
  @Query(value = "select * from user_voucher where userid=:userid and deprecated=false order by ?#{#pageable}",
      countQuery = "select count(*) from user_voucher where userid=:userid and deprecated=false order by ?#{#pageable}",
      nativeQuery = true)
  Page<UserVoucher> findByUserid(@Param("userid") String userid, Pageable pageable);

  /**
   * 查询用户的兑换券
   * 
   * @param userid
   * @return
   */
  @Query(value = "select * from user_voucher where userid=:userid and deprecated=false", nativeQuery = true)
  List<UserVoucher> findByUserid(@Param("userid") String userid);

  /**
   * 查找用户领取过的某类兑换券信息
   * 
   * @param userid
   * @param goodvoucherId
   * @return
   */
  @Query(value = "select * from user_voucher where userid=:userid and good_voucher_id="
      + ":good_vourcher_id and deprecated=false", nativeQuery = true)
  UserVoucher findByUseridAndGoodVoucher(@Param("userid") String userid,
      @Param("good_vourcher_id") String goodvoucherId);

  /**
   * 查询goodvoucher对应的UserVoucher
   * 
   * @return
   */
  @Query(value = "select * from user_voucher where good_voucher_id=:good_voucher_id and deprecated=false",
      nativeQuery = true)
  UserVoucher findByGoodVoucherId(@Param("good_voucher_id") String goodvoucherId);

}
