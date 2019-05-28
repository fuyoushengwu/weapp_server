package cn.aijiamuyingfang.server.it.user.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.user.UserMessage;

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
public interface UserMessageRepository extends JpaRepository<UserMessage, String> {
  /**
   * 分页查找用户消息
   * 
   * @param useridList
   *          用户idList
   * @param pageable
   *          分页信息
   * @return
   */
  Page<UserMessage> findByUseridIn(List<String> useridList, Pageable pageable);

  /**
   * 清除过期的消息
   */
  @Modifying
  @Transactional
  @Query(value = "delete from user_message where datediff(now(),finish_time)>0", nativeQuery = true)
  void cleanOvertimeMessage();

  /**
   * 获取未读消息数目
   * 
   * @param userid
   * @param adminidList
   * @return
   */
  @Query(value = "select count(*) from user_message where userid in (select id from user where id=:userid or id in :adminid) and "
      + "create_time>(select last_read_msg_time from user where id=:userid)", nativeQuery = true)
  int getUNReadMessageCount(@Param("userid") String userid, @Param("adminid") List<String> adminidList);
}
