package cn.aijiamuyingfang.server.it.db.goods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.dto.goods.StoreAddressDTO;

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
public interface StoreAddressRepository extends JpaRepository<StoreAddressDTO, String> {
}
