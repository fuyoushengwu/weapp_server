package cn.aijiamuyingfang.server.goods.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.goods.dto.ImageSourceDTO;

/**
 * [描述]:
 * <p>
 * ImageSource仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-18 03:38:29
 */
@Repository
public interface ImageSourceRepository extends JpaRepository<ImageSourceDTO, String> {

}
