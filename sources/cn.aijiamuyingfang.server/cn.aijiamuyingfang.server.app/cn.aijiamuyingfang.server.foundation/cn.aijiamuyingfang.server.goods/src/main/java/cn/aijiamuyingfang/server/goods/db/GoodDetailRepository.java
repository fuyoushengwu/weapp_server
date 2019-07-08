package cn.aijiamuyingfang.server.goods.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.goods.dto.GoodDetailDTO;

/**
 * [描述]:
 * <p>
 * 商品详细信息的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface GoodDetailRepository extends JpaRepository<GoodDetailDTO, String> {

}
