package cn.aijiamuyingfang.server.domain.user.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.domain.user.UserMessage;

/**
 * [描述]:
 * <p>
 * User数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:14:03
 */
@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
	/**
	 * 分页查找用户消息
	 * 
	 * @param useridList
	 * @param pageRequest
	 * @return
	 */
	Page<UserMessage> findByUseridIn(List<Long> useridList, Pageable pageRequest);

	/**
	 * 查找用户消息(非分页)
	 * 
	 * @param useridList
	 * @return
	 */
	List<UserMessage> findByUseridIn(List<Long> useridList);

	/**
	 * 删除超时的消息
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from user_message where datediff(now(),finish_time)>0", nativeQuery = true)
	void deleteOvertimeMessage();
}
