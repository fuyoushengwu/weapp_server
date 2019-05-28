package cn.aijiamuyingfang.server.it.coupon.db;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.coupon.GoodVoucher;

/**
 * [描述]:
 * <p>
 * 商品兑换券的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface GoodVoucherRepository extends JpaRepository<GoodVoucher, String> {

  @Override
  @Query(value = "select g from GoodVoucher g where g.id=:voucherid and g.deprecated=false")
  GoodVoucher findOne(@Param("voucherid") String voucherid);

  @Override
  @Query(value = "select g from GoodVoucher g where g.deprecated=false order by ?#{#pageable}",
      countQuery = "select count(g) from GoodVoucher g where g.deprecated=false order by ?#{#pageable}")
  Page<GoodVoucher> findAll(Pageable pageable);

  /**
   * 删除GoodVoucher对某VoucherItem的引用
   * 
   * @param voucheritemId
   */
  @Modifying
  @Transactional
  @Query(value = "delete from good_voucher_voucheritem_id_list where voucheritem_id_list=:voucheritem_id",
      nativeQuery = true)
  void deprecateVoucherItem(@Param("voucheritem_id") String voucheritemId);
}
