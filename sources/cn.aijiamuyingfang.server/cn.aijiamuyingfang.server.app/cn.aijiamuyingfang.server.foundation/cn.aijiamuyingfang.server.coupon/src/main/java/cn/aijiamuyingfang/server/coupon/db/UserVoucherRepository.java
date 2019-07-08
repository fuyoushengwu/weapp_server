package cn.aijiamuyingfang.server.coupon.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.coupon.dto.UserVoucherDTO;

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
public interface UserVoucherRepository extends JpaRepository<UserVoucherDTO, String> {
  /**
   * 分页查询用户的兑换券
   * 
   * @param username
   *          用户id
   * @param pageable
   *          分页信息
   * @return
   */
  @Query(value = "select * from user_voucher where username=:username and deprecated=false order by ?#{#pageable}",
      countQuery = "select count(*) from user_voucher where username=:username and deprecated=false order by ?#{#pageable}",
      nativeQuery = true)
  Page<UserVoucherDTO> findByUsername(@Param("username") String username, Pageable pageable);

  /**
   * 查询用户的兑换券
   * 
   * @param username
   *          用户id
   * @return
   */
  @Query(value = "select * from user_voucher where username=:username and deprecated=false", nativeQuery = true)
  List<UserVoucherDTO> findByUsername(@Param("username") String username);

  /**
   * 查找用户领取过的某类兑换券信息
   * 
   * @param username
   *          用户id
   * @param goodvoucherId
   * @return
   */
  @Query(value = "select * from user_voucher where username=:username and good_voucher_id="
      + ":good_vourcher_id and deprecated=false", nativeQuery = true)
  UserVoucherDTO findByUsernameAndGoodVoucher(@Param("username") String username,
      @Param("good_vourcher_id") String goodvoucherId);

  /**
   * 查询goodvoucher对应的UserVoucher
   * 
   * @return
   */
  @Query(value = "select * from user_voucher where good_voucher_id=:good_voucher_id and deprecated=false",
      nativeQuery = true)
  UserVoucherDTO findByGoodVoucherId(@Param("good_voucher_id") String goodvoucherId);

}
