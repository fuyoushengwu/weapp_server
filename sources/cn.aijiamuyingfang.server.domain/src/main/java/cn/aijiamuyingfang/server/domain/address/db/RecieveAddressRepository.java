package cn.aijiamuyingfang.server.domain.address.db;

import cn.aijiamuyingfang.server.domain.address.RecieveAddress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * [描述]:
 * <p>
 * 收货地址的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:35
 */
@Repository
public interface RecieveAddressRepository extends JpaRepository<RecieveAddress, String> {
  // TODO:要设置条件:deprecated=false

  @Query(value = "select r from RecieveAddress r where r.id=:id and r.deprecated=false")
  RecieveAddress findOne(@Param("id") String id);

  /**
   * 获取用户的收件地址
   * 
   * @param userid
   * @return
   */
  @Query(value = "select * from recieve_address where userid=:userid and deprecated=false", nativeQuery = true)
  List<RecieveAddress> findByUserid(@Param("userid") String userid);
}
