package cn.aijiamuyingfang.server.coupon.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.coupon.domain.VoucherItem;

/**
 * [描述]:
 * <p>
 * 兑换券可选的兑换方式的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface VoucherItemRepository extends JpaRepository<VoucherItem, String> {

  @Override
  @Query(value = "select v from VoucherItem v where v.id=:id and v.deprecated=false")
  VoucherItem findOne(@Param("id") String voucheritemId);

  @Override
  @Query(value = "select v from VoucherItem v  where v.deprecated=false order by ?#{#pageable}",
      countQuery = "select count(v) from VoucherItem v where v.deprecated=false order by ?#{#pageable}")
  Page<VoucherItem> findAll(Pageable pageable);
}
