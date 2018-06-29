package cn.aijiamuyingfang.server.domain.coupon.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.coupon.UserVoucher;

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
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
	/**
	 * 分页查询用户的兑换券
	 * 
	 * @param userid
	 * @param pagable
	 * @return
	 */
	Page<UserVoucher> findByUserid(long userid, Pageable pageable);

	/**
	 * 查询用户的兑换券
	 * 
	 * @param userid
	 * @return
	 */
	List<UserVoucher> findByUserid(long userid);

	/**
	 * 查找用户领取过的某类兑换券信息
	 * 
	 * @param userid
	 * @param goodVourcherId
	 * @return
	 */
	@Query(value = "select * from user_voucher where userid=:userid and good_voucher_id=:good_vourcher_id", nativeQuery = true)
	UserVoucher findByUseridAndGoodVoucher(@Param("userid") long userid,
			@Param("good_vourcher_id") long goodVourcherId);

}
