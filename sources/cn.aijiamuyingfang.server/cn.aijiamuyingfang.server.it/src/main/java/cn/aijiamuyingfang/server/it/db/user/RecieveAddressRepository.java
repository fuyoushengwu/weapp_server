package cn.aijiamuyingfang.server.it.db.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.dto.user.RecieveAddressDTO;

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
public interface RecieveAddressRepository extends JpaRepository<RecieveAddressDTO, String> {

  @Override
  @Query(value = "select r from RecieveAddress r where r.id=:id and r.deprecated=false")
  RecieveAddressDTO findOne(@Param("id") String id);

  /**
   * 获取用户的收件地址
   * 
   * @param username
   *          用户id
   * @return 获取用户的收件地址
   */
  @Query(value = "select * from recieve_address where username=:username and deprecated=false", nativeQuery = true)
  List<RecieveAddressDTO> findByUsername(@Param("username") String username);

  /**
   * 设置所有收件地址非默认
   */
  @Modifying
  @Transactional
  @Query(value = "update recieve_address set def=false", nativeQuery = true)
  void setAllRecieveAddressNotDef();
}
