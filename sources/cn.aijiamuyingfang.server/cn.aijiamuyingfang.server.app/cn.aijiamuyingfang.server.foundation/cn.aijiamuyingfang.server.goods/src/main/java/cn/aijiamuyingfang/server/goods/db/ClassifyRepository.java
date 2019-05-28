package cn.aijiamuyingfang.server.goods.db;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.goods.domain.Classify;

/**
 * [描述]:
 * <p>
 * 商品条目的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface ClassifyRepository extends JpaRepository<Classify, String> {

  /**
   * 分页查找所有顶层条目
   * 
   * @param pageable
   *          分页信息
   * @return
   */
  @Query(value = "select * from classify where level=1 order by ?#{#pageable}",
      countQuery = "select count(*) from classify where level=1 order by ?#{#pageable}", nativeQuery = true)
  Page<Classify> findTopClassifyList(Pageable pageable);

  /**
   * 查找某一等级下的所有条目
   * 
   * @param level
   * @return
   */
  @Query(value = "select * from classify where level=:level", nativeQuery = true)
  List<Classify> findByLevel(@Param("level") int level);

  /**
   * 根据名称查找条目
   * 
   * @param name
   * @return
   */
  @Query(value = "select * from classify where name=:name", nativeQuery = true)
  List<Classify> findByName(@Param("name") String name);

  /**
   * 移除条目下的商品
   * 
   * @param goodid
   *          商品id
   */
  @Modifying
  @Transactional
  @Query(value = "delete from classify_good_list where good_list_id=:goodid", nativeQuery = true)
  public void removeClassifyGood(@Param("goodid") String goodid);

}
