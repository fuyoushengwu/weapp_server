package cn.aijiamuyingfang.server.coupon.db;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.coupon.dto.GoodVoucherDTO;

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
public interface GoodVoucherRepository extends JpaRepository<GoodVoucherDTO, String> {

  @Override
  @Query(value = "select * from good_voucher where id=:voucher_id and deprecated=false", nativeQuery = true)
  GoodVoucherDTO findOne(@Param("voucher_id") String voucherId);

  @Override
  @Query(value = "select * from good_voucher where deprecated=false order by ?#{#pageable}",
      countQuery = "select count(*) from good_voucher where deprecated=false order by ?#{#pageable}",
      nativeQuery = true)
  Page<GoodVoucherDTO> findAll(Pageable pageable);

  /**
   * 删除GoodVoucher对某VoucherItem的引用
   * 
   * @param voucherItemId
   */
  @Modifying
  @Transactional
  @Query(value = "delete from good_voucher_voucher_item_id_list where voucher_item_id_list=:voucher_item_id",
      nativeQuery = true)
  void deprecateVoucherItem(@Param("voucher_item_id") String voucherItemId);
}
