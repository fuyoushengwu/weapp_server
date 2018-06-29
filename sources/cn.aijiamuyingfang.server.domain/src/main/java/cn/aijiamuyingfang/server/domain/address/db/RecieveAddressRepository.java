package cn.aijiamuyingfang.server.domain.address.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.address.RecieveAddress;

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
public interface RecieveAddressRepository extends JpaRepository<RecieveAddress, Long> {

}
