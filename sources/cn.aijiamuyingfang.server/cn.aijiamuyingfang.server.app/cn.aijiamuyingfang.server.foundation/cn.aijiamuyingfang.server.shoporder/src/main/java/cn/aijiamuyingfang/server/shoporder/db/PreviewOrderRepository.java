package cn.aijiamuyingfang.server.shoporder.db;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.shoporder.domain.PreviewOrder;

/**
 * [描述]:
 * <p>
 * 订单预览的数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:15:03
 */
@Repository
public interface PreviewOrderRepository extends JpaRepository<PreviewOrder, String> {
  /**
   * 查找用户的预览订单
   * 
   * @param userId
   * @return
   */
  PreviewOrder findByUserId(String userId);

  /**
   * 删除用户的预览订单
   * 
   * @param userId
   */
  @Transactional
  void deleteByUserId(String userId);
}
