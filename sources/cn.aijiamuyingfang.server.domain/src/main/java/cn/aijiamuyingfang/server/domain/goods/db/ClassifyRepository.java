package cn.aijiamuyingfang.server.domain.goods.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.goods.Classify;

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
public interface ClassifyRepository extends JpaRepository<Classify, Long> {
	/**
	 * 查找某一等级下的所有条目
	 * 
	 * @param level
	 * @return
	 */
	List<Classify> findByLevel(int level);

	/**
	 * 根据名称查找条目
	 * 
	 * @param name
	 * @return
	 */
	List<Classify> findByName(String name);
}
